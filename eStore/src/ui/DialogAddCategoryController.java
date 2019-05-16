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

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class DialogAddCategoryController implements Initializable {
    
    @FXML
    private Button add_category_b_ok;
    @FXML
    private Button add_category_b_cancel;
    @FXML
    private Label add_category_l_name;
    @FXML
    private TextField add_category_tf_name;
    
    //DataSource and AppStore instances
    private DataSource ds = DataSource.getDataSource();
    private AppStore app = AppStore.APP;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the components text
        add_category_b_ok.setText(ds.write("ok"));
        add_category_b_cancel.setText(ds.write("cancel"));
        add_category_l_name.setText(ds.write("add_dialog_new_category_name"));
        
        add_category_b_cancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                closeWindow();
            }
        });
                
        add_category_b_ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if(validateFields()){
                    ds.database().createProductCategory(add_category_tf_name.getText());
                    StockController.refreshComboCategory();
                    ShopController.refreshComboCategory();
                    closeWindow();
                }      
            }
        });
        
    }
    
    private boolean validateFields(){
        add_category_tf_name.setStyle("");
        if(!"".equals(add_category_tf_name.getText())){ 
            return true;
        }else{
            add_category_tf_name.setStyle(AppStore.STYLE_RED_BACKGROUND);
            return false;
        }
    }
    
    private void closeWindow(){
        ((Stage)add_category_b_cancel.getScene().getWindow()).close();
    }
    
}
