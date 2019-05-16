package ui;

import data.config.Config;
import data.database.DatabaseModel.AccountTax;
import data.database.DatabaseModel.ProductCategory;
import data.database.ext.AccountTaxExt;
import data.database.ext.ProductCategoryExt;
import data.database.ext.ProductProductExt;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import methods.DataSource;
import methods.Util;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class StockController implements Initializable {

    @FXML
    private Button stock_b_save;
    @FXML
    private Button stock_b_new;
    @FXML
    private Button stock_b_add_category;
    @FXML
    private Button stock_b_add_tax;
    @FXML
    private Label stock_l_name;
    @FXML
    private Label stock_l_description;
    @FXML
    private Label stock_l_category;
    @FXML
    private Label stock_l_tax;
    @FXML
    private Label stock_l_qty;
    @FXML
    private Label stock_l_price;
    @FXML
    private CheckBox stock_cb_active;
    @FXML
    private ComboBox stock_combo_category;
    @FXML
    private ComboBox stock_combo_tax;
    @FXML
    private TableView stock_t_stock;
    @FXML
    private TextField stock_tf_name;
    @FXML
    private TextField stock_tf_qty;
    @FXML
    private TextField stock_tf_price;
    @FXML
    private TextArea stock_ta_description;

    //DataSource and AppStore instances
    private final DataSource ds = DataSource.getDataSource();
    private final AppStore app = AppStore.APP;
    
    public static ArrayList<StockController> WINDOW_LIST=new ArrayList<>();
    
    private Integer id_actual_edition;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //set the components text
        stock_b_add_category.setText(ds.write("stock_add"));
        stock_b_save.setText(ds.write("stock_save"));
        stock_b_new.setText(ds.write("stock_new"));
        stock_b_add_tax.setText(ds.write("stock_add"));
        stock_l_name.setText(ds.write("stock_name"));
        stock_l_description.setText(ds.write("stock_description"));
        stock_l_category.setText(ds.write("stock_category"));
        stock_l_tax.setText(ds.write("stock_tax"));
        stock_l_qty.setText(ds.write("stock_qty"));
        stock_l_price.setText(ds.write("stock_price")+"/"+ds.getConfig().getSetting(Config.SETTING.currency));
        stock_cb_active.setText(ds.write("stock_active"));
        
        //Gives format to the table
        formatTable();
        //fill the table
        stock_t_stock.getItems().clear();
        for (ProductProductExt ppe : DataSource.getDataSource().database().getProductList()) {
            stock_t_stock.getItems().add(ppe);
        }
        //fill combos
        stock_combo_category.getItems().clear();
        stock_combo_category.getItems().addAll(ds.database().getCategoryList());
        stock_combo_tax.getItems().clear();
        stock_combo_tax.getItems().addAll(ds.database().getTaxList());
        //clear fields
        clearFields();
        
        //sets buttons on action event
        stock_b_add_category.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.dialog_add_category);
            }
        });

        stock_b_add_tax.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.dialog_add_tax);
            }
        });

        stock_b_new.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (validateFields()) {
                    ds.database().createProductProduct(stock_tf_name.getText(), stock_ta_description.getText(), stock_cb_active.isSelected(), (AccountTax) stock_combo_tax.getSelectionModel().getSelectedItem(), Util.round2d(Double.parseDouble(stock_tf_qty.getText())), Util.round2d(Double.parseDouble(stock_tf_price.getText())), (ProductCategory) stock_combo_category.getSelectionModel().getSelectedItem());
                    StockController.refreshTable();
                    ShopController.refreshTableProduct();
                    clearFields();
                }
            }
        });

        stock_b_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if(validateFields()){
                    ds.database().updateProductProduct(id_actual_edition,stock_tf_name.getText(), stock_ta_description.getText(), stock_cb_active.isSelected(), (AccountTax) stock_combo_tax.getSelectionModel().getSelectedItem(), Util.round2d(Double.parseDouble(stock_tf_qty.getText())), Util.round2d(Double.parseDouble(stock_tf_price.getText())), (ProductCategory) stock_combo_category.getSelectionModel().getSelectedItem());
                    StockController.refreshTable();
                    ShopController.refreshTableProduct();
                    clearFields();
                } 
            }
        });

        stock_t_stock.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                    ProductProductExt ppe=(ProductProductExt)stock_t_stock.getSelectionModel().getSelectedItem();
                    id_actual_edition=ppe.getId();
                    fillFields(ppe);
                    stock_b_save.setDisable(false);
                }catch(NullPointerException e){
                        
                }
            }
        });
        
        
    }
    
    private void fillFields(ProductProductExt ppe){
        int i=0;
        for(ProductCategoryExt pce:ds.database().getCategoryList()){
            if(pce.getId()==ppe.getCategId()){
                break;
            }
            i++;
        }
        stock_combo_category.getSelectionModel().select(i);
        
        i=0;
        for(AccountTaxExt ate:ds.database().getTaxList()){
            if(ate.getId()==ppe.getTaxId()){
                break;
            }
            i++;
        }
        stock_combo_tax.getSelectionModel().select(i);
        
        stock_tf_name.setText(ppe.getName());
        stock_ta_description.setText(ppe.getDescription());
        stock_tf_qty.setText(Util.format2d(ppe.getQtyAvailable()));
        stock_tf_price.setText(Util.format2d(ppe.getPriceUnit()));
        if(ppe.isActive()){
            stock_cb_active.setSelected(true);
        }else{
            stock_cb_active.setSelected(false);
        }
    }

    private void clearFields() {
        stock_b_save.setDisable(true);
        stock_combo_category.getSelectionModel().select(-1);
        stock_combo_tax.getSelectionModel().select(-1);
        stock_tf_name.setText("");
        stock_ta_description.setText("");
        stock_tf_qty.setText("");
        stock_tf_price.setText("");
        stock_cb_active.setSelected(true);
        id_actual_edition=null;
    }

    private boolean validateFields() {
        boolean v = true;
        if (stock_combo_category.getSelectionModel().getSelectedIndex() == -1) {
            stock_combo_category.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            stock_combo_category.setStyle("");
        }
        if (stock_combo_tax.getSelectionModel().getSelectedIndex() == -1) {
            stock_combo_tax.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            stock_combo_tax.setStyle("");
        }
        if ("".equals(stock_tf_name.getText())) {
            stock_tf_name.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            stock_tf_name.setStyle("");
        }
        if ("".equals(stock_tf_qty.getText())) {
            stock_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            stock_tf_qty.setStyle("");
        }
        stock_tf_qty.setText(Util.replaceCommasWithDots(stock_tf_qty.getText()));
        try {
            Double.parseDouble(stock_tf_qty.getText());
        } catch (NumberFormatException e) {
            stock_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        }
        if ("".equals(stock_tf_price.getText())) {
            stock_tf_price.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            stock_tf_price.setStyle("");
        }
        stock_tf_price.setText(Util.replaceCommasWithDots(stock_tf_price.getText()));
        try {
            Double.parseDouble(stock_tf_price.getText());
        } catch (NumberFormatException e) {
            stock_tf_price.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        }
        return v;
    }

    public static void refreshComboCategory() {
        for(StockController sc:StockController.WINDOW_LIST){
            sc.stock_combo_category.getItems().clear();
            sc.stock_combo_category.getItems().addAll(DataSource.getDataSource().database().getCategoryList());
        }
    }

    public static void refreshComboTax() {
        for(StockController sc:StockController.WINDOW_LIST){
            sc.stock_combo_tax.getItems().clear();
            sc.stock_combo_tax.getItems().addAll(DataSource.getDataSource().database().getTaxList());
        }
    }

    public static void refreshTable() {
        for(StockController sc:StockController.WINDOW_LIST){
            sc.stock_t_stock.getItems().clear();
            for (ProductProductExt ppe : DataSource.getDataSource().database().getProductList()) {
            sc.stock_t_stock.getItems().add(ppe);
        }
        }
    }

    private void formatTable() {
        stock_t_stock.getColumns().clear();
        stock_t_stock.getItems().clear();

        TableColumn column_categ = new TableColumn(ds.write("stock_category"));
        column_categ.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("categoryName"));

        TableColumn column_name = new TableColumn(ds.write("stock_name"));
        column_name.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("name"));

        TableColumn column_description = new TableColumn(ds.write("stock_description"));
        column_description.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("description"));

        TableColumn column_tax = new TableColumn(ds.write("stock_tax"));
        column_tax.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("taxName"));

        TableColumn column_price = new TableColumn(ds.write("stock_price")+"/"+ds.getConfig().getSetting(Config.SETTING.currency));
        column_price.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("price_string"));
        column_price.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        TableColumn column_stock = new TableColumn(ds.write("stock_qty"));
        column_stock.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("qty_string"));
        column_stock.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        stock_t_stock.setEditable(false);
        stock_t_stock.getColumns().addAll(column_categ, column_name, column_description, column_tax, column_price, column_stock);
        stock_t_stock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
    
}
