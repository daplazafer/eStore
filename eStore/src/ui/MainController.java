package ui;

import data.config.Config;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import methods.DataSource;
import methods.Util;

/**
 * FXML Controller class
 *
 * @author Daniel Plaza
 */
public class MainController implements Initializable {

    //FXML components
    @FXML
    private ImageView iv_icon_e;
    @FXML
    private Button b_icon_e;
    @FXML
    private ImageView iv_icon_invoice;
    @FXML
    private ImageView iv_icon_stock;
    @FXML
    private Button b_icon_invoice;
    @FXML
    private Button b_icon_stock;
    @FXML
    private Button b_icon_settings;
    @FXML
    private Button b_icon_fb;
    @FXML
    private Button b_icon_tw;
    @FXML
    private ImageView iv_icon_settings;
    @FXML
    private ImageView iv_icon_fb;
    @FXML
    private ImageView iv_icon_tw;

    //DataSource and AppStore instances
    private DataSource ds = DataSource.getDataSource();
    private AppStore app = AppStore.APP;

    public static ArrayList<MainController> WINDOW_LIST = new ArrayList<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //sets the icons image
        iv_icon_e.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_e.png")));
        iv_icon_invoice.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_invoice.png")));
        iv_icon_stock.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_stock.png")));
        iv_icon_settings.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_settings.png")));
        iv_icon_fb.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_fb.png")));
        iv_icon_tw.setImage(new Image(getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT + "icon_tw.png")));

        //set the icons text
        b_icon_e.setText(ds.write("icon_e"));
        b_icon_invoice.setText(ds.write("icon_invoice"));
        b_icon_stock.setText(ds.write("icon_stock"));

        //set the icons tooltip
        b_icon_e.setTooltip(new Tooltip(ds.write("icon_e_tt")));
        b_icon_invoice.setTooltip(new Tooltip(ds.write("icon_invoice_tt")));
        b_icon_stock.setTooltip(new Tooltip(ds.write("icon_stock_tt")));
        b_icon_settings.setTooltip(new Tooltip(ds.write("icon_settings_tt")));
        b_icon_fb.setTooltip(new Tooltip(ds.write("icon_fb_tt")));
        b_icon_tw.setTooltip(new Tooltip(ds.write("icon_tw_tt")));

        //facebook and twitter icons
        b_icon_fb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.goToFacebook();
            }
        });
        b_icon_tw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.goToTwitter();
            }
        });
        if ("".equals(DataSource.getDataSource().getConfig().getSetting(Config.SETTING.facebook))) {
            b_icon_fb.setVisible(false);
        }
        if ("".equals(DataSource.getDataSource().getConfig().getSetting(Config.SETTING.twitter))) {
            b_icon_tw.setVisible(false);
        }

        //sets icons onAction
        b_icon_e.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.shop);
            }
        });
        b_icon_invoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.invoice);
            }
        });
        b_icon_stock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.stock);
            }
        });
        b_icon_settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                app.openWindow(AppStore.WINDOW.settings);
            }
        });

    }

    public static void refreshNetIcons() {
        //show/hide facebook icon
        for (MainController mc : WINDOW_LIST) {
            if (!"".equals(DataSource.getDataSource().getConfig().getSetting(Config.SETTING.facebook))) {
                mc.b_icon_fb.setVisible(true);
            } else {
                mc.b_icon_fb.setVisible(false);
            }
            //show/hide twitter icon
            if (!"".equals(DataSource.getDataSource().getConfig().getSetting(Config.SETTING.twitter))) {
                mc.b_icon_tw.setVisible(true);
            } else {
                mc.b_icon_tw.setVisible(false);
            }
        }
    }

}
