
package ui;

import data.config.Config;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import methods.DataSource;
import methods.Util;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class SettingsController implements Initializable {
    
    @FXML
    private Tab settings_tab_general;
    @FXML
    private Tab settings_tab_database;
    @FXML
    private Tab settings_tab_net;
    @FXML
    private Button settings_b_ok;
    @FXML
    private Button settings_b_cancel;
    @FXML
    private Label settings_general_l_language;
    @FXML
    private ComboBox settings_general_combo_language;
    @FXML
    private Label settings_db_l_plugin;
    @FXML
    private Label settings_db_l_path;
    @FXML
    private Label settings_db_l_name;
    @FXML
    private Label settings_db_l_user;
    @FXML
    private Label settings_db_l_pass;
    @FXML
    private ComboBox settings_db_combo_plugin;
    @FXML
    private TextField settings_db_tf_path;
    @FXML
    private TextField settings_db_tf_name;
    @FXML
    private TextField settings_db_tf_user;
    @FXML
    private PasswordField settings_db_tf_pass;
    @FXML
    private Label settings_net_l_fb;
    @FXML
    private Label settings_net_l_tw;
    @FXML
    private TextField settings_net_tf_fb;
    @FXML
    private TextField settings_net_tf_tw;
    @FXML
    private Label settings_general_l_currency;
    @FXML
    private ComboBox settings_general_combo_currency;
    
    
    //DataSource and AppStore instances
    private final DataSource ds = DataSource.getDataSource();
    private final AppStore app = AppStore.APP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the components text
        settings_tab_general.setText(ds.write("settings_tab_general"));
        settings_general_l_currency.setText(ds.write("settings_currency"));
        settings_tab_database.setText(ds.write("settings_tab_database"));
        settings_tab_net.setText(ds.write("settings_tab_net"));
        settings_b_ok.setText(ds.write("ok"));
        settings_b_cancel.setText(ds.write("cancel"));
        settings_general_l_language.setText(ds.write("settings_language"));
        settings_db_l_plugin.setText(ds.write("settings_dbplugin"));
        settings_db_l_path.setText(ds.write("settings_dbpath"));
        settings_db_l_name.setText(ds.write("settings_dbname"));
        settings_db_l_user.setText(ds.write("settings_dbuser"));
        settings_db_l_pass.setText(ds.write("settings_dbpass"));
        settings_net_l_fb.setText(Util.FACEBOOK_PATH);
        settings_net_l_tw.setText(Util.TWITTER_PATH);
        
        //set the combos
        settings_general_combo_language.getItems().setAll(Util.findFiles(Util.LANGUAGES_PATH, Util.LANGUAGE_EXTENSION));
        settings_db_combo_plugin.getItems().setAll(Util.findFiles(Util.PlUGINS_PATH, Util.PLUGIN_EXTENSION));
        settings_general_combo_currency.getItems().setAll(Util.getCurrency());
        
        //read the config
        settings_general_combo_language.getSelectionModel().select(ds.getConfig().getSetting(Config.SETTING.language));
        settings_db_combo_plugin.getSelectionModel().select(ds.getConfig().getSetting(Config.SETTING.dbplugin));
        settings_general_combo_currency.getSelectionModel().select(ds.getConfig().getSetting(Config.SETTING.currency));
        settings_db_tf_path.setText(ds.getConfig().getSetting(Config.SETTING.dbpath));
        settings_db_tf_name.setText(ds.getConfig().getSetting(Config.SETTING.dbname));
        settings_db_tf_user.setText(ds.getConfig().getSetting(Config.SETTING.dbuser));
        settings_db_tf_pass.setText(ds.getConfig().getSetting(Config.SETTING.dbpassword));
        settings_net_tf_fb.setText(ds.getConfig().getSetting(Config.SETTING.facebook));
        settings_net_tf_tw.setText(ds.getConfig().getSetting(Config.SETTING.twitter));
        
        //set the buttons ok and cancel on action
        settings_b_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                closeWindow();
            }
        });
        
        settings_b_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ds.getConfig().setSetting(Config.SETTING.language, settings_general_combo_language.getSelectionModel().getSelectedItem().toString());
                ds.getConfig().setSetting(Config.SETTING.dbplugin, settings_db_combo_plugin.getSelectionModel().getSelectedItem().toString());
                ds.getConfig().setSetting(Config.SETTING.currency, settings_general_combo_currency.getSelectionModel().getSelectedItem().toString());
                ds.getConfig().setSetting(Config.SETTING.dbpath,settings_db_tf_path.getText());
                ds.getConfig().setSetting(Config.SETTING.dbname,settings_db_tf_name.getText());
                ds.getConfig().setSetting(Config.SETTING.dbuser,settings_db_tf_user.getText());
                ds.getConfig().setSetting(Config.SETTING.dbpassword,settings_db_tf_pass.getText());
                ds.getConfig().setSetting(Config.SETTING.facebook,settings_net_tf_fb.getText());
                ds.getConfig().setSetting(Config.SETTING.twitter,settings_net_tf_tw.getText());
                MainController.refreshNetIcons();
                closeWindow();
            }
        });
    }  
    
    private void closeWindow(){
        ((Stage)settings_b_cancel.getScene().getWindow()).close();
    }
}
