
package ui;

import data.config.Config;
import data.database.ext.InvoiceExt;
import data.database.ext.InvoiceLineExt;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import methods.DataSource;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class InvoiceController implements Initializable {
    
    @FXML
    private TableView invoice_t_invoice;
    @FXML
    private TableView invoice_t_line;
    @FXML
    private Label invoice_l_name;
    @FXML
    private Label invoice_l_date;
    @FXML
    private Label invoice_l_subtotal;
    @FXML
    private Label invoice_l_tax;
    @FXML
    private Label invoice_l_total;
    @FXML
    private Label invoice_l_subtotal_name;
    @FXML
    private Label invoice_l_tax_name;
    @FXML
    private Label invoice_l_total_name;
    
    //DataSource and AppStore instances
    private final DataSource ds = DataSource.getDataSource();
    private final AppStore app = AppStore.APP;
    
    public static ArrayList<InvoiceController> WINDOW_LIST = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the components text
        invoice_l_name.setText(ds.write("invoice_name"));
        invoice_l_date.setText(ds.write("shop_date"));
        invoice_l_subtotal.setText("");
        invoice_l_tax.setText("");
        invoice_l_total.setText("");
        invoice_l_subtotal_name.setText(ds.write("shop_subtotal"));
        invoice_l_tax_name.setText(ds.write("shop_tax"));
        invoice_l_total_name.setText(ds.write("shop_total"));
        
        //format tables
        formatInvoiceTable();
        formatLineTable();
        
        //refreshInvoiceTable
        refreshInvoiceTableLocal();
        
        invoice_t_invoice.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                InvoiceExt ie=(InvoiceExt)invoice_t_invoice.getSelectionModel().getSelectedItem();
                if(ie!=null){
                    refreshLineTable(ie);
                    invoice_l_name.setText(ds.write("invoice_name")+" "+ie.getName());
                    invoice_l_date.setText(ds.write("shop_date")+" "+ie.getDate_string());
                    invoice_l_subtotal.setText(ie.getAmountUntaxed_string()+" "+ds.getConfig().getSetting(Config.SETTING.currency));
                    invoice_l_tax.setText(ie.getAmountTax_string()+" "+ds.getConfig().getSetting(Config.SETTING.currency));
                    invoice_l_total.setText(ie.getAmountTotal_string()+" "+ds.getConfig().getSetting(Config.SETTING.currency));    
                }
            }
        });
    }
    
    public void refreshLineTable(InvoiceExt ie){
        invoice_t_line.getItems().clear();
        for(InvoiceLineExt ile:ds.database().getInvoiceList(ie)){
            invoice_t_line.getItems().add(ile);
        }
    }
    
    public void refreshInvoiceTableLocal(){
        invoice_t_invoice.getItems().clear();
        for(InvoiceExt ie:DataSource.getDataSource().database().getInvoiceList()){
            invoice_t_invoice.getItems().add(ie);
        }
    }
    
    public static void refreshInvoiceTable(){
        for(InvoiceController ic:InvoiceController.WINDOW_LIST){
            ic.invoice_t_invoice.getItems().clear();
            for(InvoiceExt ie:DataSource.getDataSource().database().getInvoiceList()){
                ic.invoice_t_invoice.getItems().add(ie);
            }
        }
    }
    
    private void formatInvoiceTable(){
        invoice_t_invoice.getColumns().clear();
        invoice_t_invoice.getItems().clear();

        TableColumn column_name = new TableColumn(ds.write("invoice_name"));
        column_name.setCellValueFactory(new PropertyValueFactory<InvoiceExt, String>("name"));

        TableColumn column_date = new TableColumn(ds.write("shop_date"));
        column_date.setCellValueFactory(new PropertyValueFactory<InvoiceExt, String>("date_string"));

        TableColumn column_total = new TableColumn(ds.write("shop_total") + "/" + ds.getConfig().getSetting(Config.SETTING.currency));
        column_total.setCellValueFactory(new PropertyValueFactory<InvoiceExt, String>("amountTotal_string"));
        column_total.setCellFactory(AppStore.STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT);

        invoice_t_invoice.setEditable(false);
        invoice_t_invoice.getColumns().addAll(column_name,column_date,column_total);
        invoice_t_invoice.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    private void formatLineTable(){
        invoice_t_line.getColumns().clear();
        invoice_t_line.getItems().clear();

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

        invoice_t_line.setEditable(false);
        invoice_t_line.getColumns().addAll(column_categName, column_name, column_tax, column_qty, column_price, column_subtotal);
        invoice_t_line.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    
    
}
