/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import methods.DataSource;
import methods.Util;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class DialogAddTaxController implements Initializable {
    
    @FXML
    private Button add_tax_b_ok;
    @FXML
    private Button add_tax_b_cancel;
    @FXML
    private Label add_tax_l_name;
    @FXML
    private Label add_tax_l_value;
    @FXML
    private TextField add_tax_tf_name;
    @FXML
    private TextField add_tax_tf_value;
    
    //DataSource and AppStore instances
    private DataSource ds = DataSource.getDataSource();
    private AppStore app = AppStore.APP;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        add_tax_b_ok.setText(ds.write("ok"));
        add_tax_b_cancel.setText(ds.write("cancel"));
        add_tax_l_name.setText(ds.write("add_dialog_new_tax_name"));
        add_tax_l_value.setText(ds.write("add_dialog_new_tax_value"));
        
        add_tax_b_cancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                closeWindow();
            }
        });
        
        add_tax_b_ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if(validateFields()){
                    ds.database().createAccountTax(add_tax_tf_name.getText(),Double.parseDouble(Util.replaceCommasWithDots(add_tax_tf_value.getText())));
                    StockController.refreshComboTax();
                    closeWindow();
                }
            }
        });
        
    } 
    
    private boolean validateFields(){
        add_tax_tf_value.setStyle("");
        add_tax_tf_name.setStyle("");
        boolean v=true;
        if("".equals(add_tax_tf_name.getText())){
            add_tax_tf_name.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v=false;
        }
        if("".equals(add_tax_tf_value.getText())){
            add_tax_tf_value.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v=false;
        }
        try{
            Double.parseDouble(Util.replaceCommasWithDots(add_tax_tf_value.getText()));
        }catch(NumberFormatException e){
            add_tax_tf_value.setStyle(AppStore.STYLE_RED_BACKGROUND);
            v=false;
        }
        return v;
    }
    
    private void closeWindow(){
        ((Stage)add_tax_b_cancel.getScene().getWindow()).close();
    }
    
}
