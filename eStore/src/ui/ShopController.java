package ui;

import data.config.Config;
import data.database.ext.InvoiceExt;
import data.database.ext.InvoiceLineExt;
import data.database.ext.ProductCategoryExt;
import data.database.ext.ProductProductExt;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import methods.DataSource;
import methods.Util;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class ShopController implements Initializable {

    @FXML
    private TableView shop_t_line;
    @FXML
    private Button shop_b_ok;
    @FXML
    private ImageView shop_iv_ok;
    @FXML
    private Button shop_b_cancel;
    @FXML
    private ImageView shop_iv_cancel;
    @FXML
    private Label shop_l_datepicker;
    @FXML
    private GridPane shop_p_datepicker;
    @FXML
    private ComboBox shop_combo_category;
    @FXML
    private Label shop_l_category;
    @FXML
    private TableView shop_t_product;
    @FXML
    private Button shop_b_delete;
    @FXML
    private ImageView shop_iv_delete;
    @FXML
    private Button shop_b_add;
    @FXML
    private ImageView shop_iv_add;
    @FXML
    private Label shop_l_total;
    @FXML
    private Label shop_l_subtotal;
    @FXML
    private Label shop_l_product;
    @FXML
    private Label shop_l_qty;
    @FXML
    private TextField shop_tf_price;
    @FXML
    private Button shop_b_minus;
    @FXML
    private TextField shop_tf_qty;
    @FXML
    private Button shop_b_plus;
    @FXML
    private Label shop_l_price;
    @FXML
    private Label shop_l_tax;
    @FXML
    private Label shop_l_subtotal_name;
    @FXML
    private Label shop_l_tax_name;
    @FXML
    private Label shop_l_total_name;
    @FXML
    private TextField shop_tf_product;

    //DataSource and AppStore instances
    private final DataSource ds = DataSource.getDataSource();
    private final AppStore app = AppStore.APP;

    private final static int PRICE_JUMP = 1;

    private DatePicker datePicker;

    public static ArrayList<ShopController> WINDOW_LIST = new ArrayList<>();

    private InvoiceExt invoice;
    private ProductProductExt product;
    private ArrayList<InvoiceLineExt> lineList = new ArrayList<>();
    private Integer indexToDelete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        invoice = ds.database().generateNewInvoice();

        //set the icons
        shop_iv_ok.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "button_ok.png")));
        shop_iv_cancel.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "button_cancel.png")));
        shop_iv_add.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_add.png")));
        shop_iv_delete.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_delete.png")));
        
        //set components text
        shop_l_datepicker.setText(ds.write("shop_date"));
        shop_l_category.setText(ds.write("stock_category"));
        shop_b_delete.setText(ds.write("shop_delete"));
        shop_b_add.setText(ds.write("shop_add"));
        shop_l_product.setText(ds.write("shop_product"));
        shop_l_qty.setText(ds.write("shop_qty"));
        shop_b_minus.setText(ds.write("shop_minus"));
        shop_b_plus.setText(ds.write("shop_plus"));
        shop_l_price.setText(ds.write("stock_price") + "/" + ds.getConfig().getSetting(Config.SETTING.currency));
        shop_l_subtotal_name.setText(ds.write("shop_subtotal"));
        shop_l_tax_name.setText(ds.write("shop_tax"));
        shop_l_total_name.setText(ds.write("shop_total"));
        
        //sets the datepicker
        datePicker = new DatePicker(new Locale(ds.write("language"), ds.write("language0")));
        datePicker.setDateFormat(Util.SDF_DATEPICKER);
        datePicker.setPromptText("-- / -- / ----");
        datePicker.getCalendarView().todayButtonTextProperty().set(ds.write("datepicker_today"));
        datePicker.getCalendarView().setShowWeeks(false);
        datePicker.getStylesheets().add("ui/util/DatePicker.css");
        shop_p_datepicker.add(datePicker, 0, 0);
        ((TextField) datePicker.getChildren().get(0)).setMaxWidth(83);//ajustment of the textfield width
        ((TextField) datePicker.getChildren().get(0)).setEditable(false);//edit in the textfield is not allowed
        ((TextField) datePicker.getChildren().get(0)).setText(Util.SDF_DATEPICKER.format(invoice.getDate()));

        shop_combo_category.getItems().clear();
        shop_combo_category.getItems().addAll(ds.database().getCategoryList());

        shop_combo_category.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                refreshTableProductLocal();
            }
        });

        formatProductTable();
        refreshTableProduct();
        shop_t_product.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try {
                    product = (ProductProductExt) shop_t_product.getSelectionModel().getSelectedItem();
                    fillFields();
                    shop_tf_qty.setStyle("");
                    shop_b_add.setDisable(false);
                    shop_b_delete.setDisable(true);
                } catch (NullPointerException e) {
                    shop_b_add.setDisable(true);
                    clearFields();
                    shop_b_delete.setDisable(true);
                }
            }
        });

        shop_b_plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                sumQty(PRICE_JUMP);
            }
        });

        shop_b_minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                sumQty(-PRICE_JUMP);
            }
        });

        disableButtons();

        shop_b_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (validateFields()) { 
                    lineList.add(new InvoiceLineExt(product.getId(), Double.parseDouble(shop_tf_qty.getText()), product.getPriceUnit(), product.getTaxId(), product.getName(), product.getTaxName(), product.getCategoryName(), product.getTaxAmount()));
                    clearFields();
                    refreshTableLines();
                    shop_b_add.setDisable(true);
                }
            }
        });

        shop_b_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (indexToDelete != null) {
                    lineList.remove((int) indexToDelete);
                    refreshTableLines();
                } 
                shop_b_delete.setDisable(true);
                indexToDelete = null;
            }
        });

        formatLinesTable();
        shop_t_line.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                try{
                    indexToDelete = shop_t_line.getSelectionModel().getSelectedIndex();
                    shop_b_delete.setDisable(false);
                }catch(NullPointerException e){
                    shop_b_delete.setDisable(true);
                    indexToDelete = null;
                }
            }
        });

        shop_b_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if(invoice.getAmountTotal()>0){
                    invoice.setDate(datePicker.getSelectedDate());
                    if(ds.database().createInvoice(invoice, lineList)){
                        StockController.refreshTable();
                        InvoiceController.refreshInvoiceTable();
                        clearAll();
                    }
                }
            }
        });

        shop_b_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                clearAll();
            }
        });

        fillTotals();

    }

    private void clearAll() {
        clearFields();
        disableButtons();
        invoice = ds.database().generateNewInvoice();
        ((TextField) datePicker.getChildren().get(0)).setText(Util.SDF_DATEPICKER.format(invoice.getDate()));
        indexToDelete = null;
        lineList = new ArrayList<>();
        shop_combo_category.getSelectionModel().select(-1);
        shop_t_product.getItems().clear();
        shop_t_line.getItems().clear();
        fillTotals();
    }

    private void clearFields() {
        shop_tf_price.setText("");
        shop_tf_product.setText("");
        shop_tf_qty.setText("");
        shop_tf_qty.setStyle("");
    }

    private boolean validateFields() {
        shop_tf_qty.setStyle("");

        boolean v = true;
        if ("".equals(shop_tf_qty.getText())) {
            shop_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v = false;
        } else {
            try {
                Double.parseDouble(Util.replaceCommasWithDots(shop_tf_qty.getText()));
            } catch (NumberFormatException e) {
                shop_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
                v = false;
            }
        }
        return v;
    }

    private void sumQty(int qty) {
        if (!"".equals(shop_tf_qty.getText())) {
            shop_tf_qty.setStyle("");
            try {
                double oldQty = Double.parseDouble(Util.replaceCommasWithDots(shop_tf_qty.getText()));
                if (oldQty % ((int) oldQty) != 0) {
                    shop_tf_qty.setText(Util.format2d(oldQty + qty));
                } else {
                    shop_tf_qty.setText(String.valueOf((int) oldQty + qty));
                }

            } catch (NumberFormatException e) {
                shop_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
            }
        } else {
            shop_tf_qty.setStyle(AppStore.STYLE_RED_BACKGROUND);
        }
    }

    private void fillFields() {
        shop_tf_product.setText(product.getName());
        shop_tf_price.setText(Util.format2d(product.getPriceUnit()));
        shop_tf_qty.setText(String.valueOf((int) PRICE_JUMP));
    }

    private void disableButtons() {
        shop_b_delete.setDisable(true);
        shop_b_add.setDisable(true);
    }

    private void formatLinesTable() {
        shop_t_line.getColumns().clear();
        shop_t_line.getItems().clear();

        TableColumn column_categName = new TableColumn(ds.write("stock_category"));
        column_categName.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("categName"));

        TableColumn column_name = new TableColumn(ds.write("shop_product"));
        column_name.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("productName"));

        TableColumn column_tax = new TableColumn(ds.write("stock_tax"));
        column_tax.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("taxName"));

        TableColumn column_qty = new TableColumn(ds.write("shop_qty"));
        column_qty.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("qty_string"));

        TableColumn column_price = new TableColumn(ds.write("stock_price") + "/" + ds.getConfig().getSetting(Config.SETTING.currency));
        column_price.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("priceUnit_string"));
        column_price.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        TableColumn column_subtotal = new TableColumn(ds.write("shop_total") + "/" + ds.getConfig().getSetting(Config.SETTING.currency));
        column_subtotal.setCellValueFactory(new PropertyValueFactory<InvoiceLineExt, String>("amountSubtotal_string"));
        column_subtotal.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        shop_t_line.setEditable(false);
        shop_t_line.getColumns().addAll(column_categName, column_name, column_tax, column_qty, column_price, column_subtotal);
        shop_t_line.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void formatProductTable() {
        shop_t_product.getColumns().clear();
        shop_t_product.getItems().clear();

        TableColumn column_name = new TableColumn(ds.write("stock_name"));
        column_name.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("name"));

        TableColumn column_description = new TableColumn(ds.write("stock_description"));
        column_description.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("description"));

        TableColumn column_tax = new TableColumn(ds.write("stock_tax"));
        column_tax.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("taxName"));

        TableColumn column_price = new TableColumn(ds.write("stock_price") + "/" + ds.getConfig().getSetting(Config.SETTING.currency));
        column_price.setCellValueFactory(new PropertyValueFactory<ProductProductExt, String>("price_string"));
        column_price.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        shop_t_product.setEditable(false);
        shop_t_product.getColumns().addAll(column_name, column_description, column_tax, column_price);
        shop_t_product.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillTotals() {
        shop_l_total.setText(Util.format2d(invoice.getAmountTotal()) + " " + ds.getConfig().getSetting(Config.SETTING.currency));
        shop_l_subtotal.setText(Util.format2d(invoice.getAmountUntaxed()) + " " + ds.getConfig().getSetting(Config.SETTING.currency));
        shop_l_tax.setText(Util.format2d(invoice.getAmountTax()) + " " + ds.getConfig().getSetting(Config.SETTING.currency));
    }

    public static void refreshComboCategory() {
        for (ShopController sc : ShopController.WINDOW_LIST) {
            sc.shop_combo_category.getItems().clear();
            sc.shop_combo_category.getItems().addAll(DataSource.getDataSource().database().getCategoryList());
        }
    }

    public static void refreshTableProduct() {
        for (ShopController sc : ShopController.WINDOW_LIST) {
            if (sc.shop_combo_category.getSelectionModel().getSelectedIndex() != -1) {
                sc.shop_t_product.getItems().clear();
                for (ProductProductExt ppe : DataSource.getDataSource().database().getProductList((ProductCategoryExt) sc.shop_combo_category.getSelectionModel().getSelectedItem())) {
                    sc.shop_t_product.getItems().add(ppe);
                }
            }
        }
    }

    private void refreshTableLines() {
        double amountUntaxed = 0;
        double amountTax = 0;
        shop_t_line.getItems().clear();
        for (InvoiceLineExt ile : lineList) {
            amountUntaxed += ile.getProductQty() * ile.getPriceUnit();
            amountTax += ile.getProductQty() * ile.getPriceUnit() * ile.getTaxAmount();
            shop_t_line.getItems().add(ile);
        }
        invoice.setAmountTax(amountTax);
        invoice.setAmountUntaxed(amountUntaxed);
        invoice.setAmountTotal(amountTax + amountUntaxed);
        fillTotals();
    }

    private void refreshTableProductLocal() {
        if (shop_combo_category.getSelectionModel().getSelectedIndex() != -1) {
            shop_t_product.getItems().clear();
            for (ProductProductExt ppe : DataSource.getDataSource().database().getProductList((ProductCategoryExt) shop_combo_category.getSelectionModel().getSelectedItem())) {
                shop_t_product.getItems().add(ppe);
            }
        }
    }

}
