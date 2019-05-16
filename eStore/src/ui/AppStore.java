package ui;

import data.config.Config;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import methods.DataSource;
import methods.Util;
import ui.undecorator.Undecorator;

/**
 *
 * @author Daniel Plaza
 */
public class AppStore extends Application {

    /**
     * Enum variable which contains the necessary attributes for each window.
     *
     * fxml: route of its javafx fmxl file as a string. Ex: "file.fxml" 
     * tittle: tittle for the window. You can pass an empty string and only the
     *          application name will be shown.
     * resizable and modal: window properties.
     * stage: you can pass an Stage or null to show the content in a new one.
     * windowList: this arraylist contains a list of available windows like
     *          this. Then is possible to refresh all of these windows together.
     *
     */
    public enum WINDOW {

        main("main.fxml", "", true, false,  MainController.WINDOW_LIST),
        stock("stock.fxml", ds.write("icon_stock"), true, false,  StockController.WINDOW_LIST),
        settings("settings.fxml", ds.write("settings"), false, true, null),
        invoice("invoice.fxml", ds.write("icon_invoice"), true, false,  InvoiceController.WINDOW_LIST),
        shop("shop.fxml", ds.write("shop_tittle"), true, false,  ShopController.WINDOW_LIST),
        dialog_add_category("dialogAddCategory.fxml", ds.write("add_dialog_new_category"), false, true,  null),
        dialog_add_tax("dialogAddTax.fxml", ds.write("add_dialog_new_tax"), false, true, null);

        private WINDOW(String fxml, String tittle, boolean resizable, boolean modal, ArrayList windowList) {
            this.fxml = fxml;
            this.tittle = tittle;
            this.resizable = resizable;
            this.modal = modal;
            this.windowList = windowList;
        }

        private final String fxml, tittle;
        private final boolean resizable, modal;
        private ArrayList windowList;

        public ArrayList getWindowList() {
            return windowList;
        }

        public String getFxml() {
            return fxml;
        }

        public String getTittle() {
            return tittle;
        }

        public boolean isResizable() {
            return resizable;
        }

        public boolean isModal() {
            return modal;
        }
    }

    public static AppStore APP;

    public final static String STYLE_RED_BACKGROUND = "-fx-background-color: RED";
    public final static String STYLE_TEXT_ALIGNMENT_RIGHT = "-fx-alignment: CENTER-RIGHT";

    private static DataSource ds = DataSource.getDataSource();

    public AppStore() {
        APP = AppStore.this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        openWindow(WINDOW.main);
    }

    public void goToFacebook() {
        Util.openWebpage(Util.FACEBOOK_PATH + ds.getConfig().getSetting(Config.SETTING.facebook));
    }

    public void goToTwitter() {
        Util.openWebpage(Util.TWITTER_PATH + ds.getConfig().getSetting(Config.SETTING.twitter));
    }

    /**
     * Shows a window passing the Window enum for the interface
     *
     * @param w window
     * @return Controller instance in case of being needed. You can also access to it through the
     *          arraylist of windows if it exists
     */
    public Initializable openWindow(WINDOW w) {
        Stage stage=new Stage();
        if ("".equals(w.getTittle())) {
            stage.setTitle(Util.APP_NAME);
        } else {
            stage.setTitle(Util.APP_NAME + " | " + w.getTittle());
        }
        stage.setResizable(w.isResizable());
        if (w.isModal()) {
            try {
                stage.initModality(Modality.APPLICATION_MODAL);
            } catch (IllegalStateException e) {
            }
        }
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream(Util.RESOURCES_PATH_SHORT+"icon.png")));
        
        Region root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(w.getFxml()));
            root = (Region) fxmlLoader.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final Undecorator undecorator = new Undecorator(ds.write("language"), stage, root);
        // Customize it by CSS if needed:
        undecorator.getStylesheets().add(Util.RESOURCES_PATH+"undecorator.css");
        Scene scene = new Scene(undecorator);
        // Install default Accelerators in the Scene
        undecorator.installAccelerators(scene);
        
        // Enable fade transition
        //undecorator.setFadeInTransition();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                we.consume();   // Do not hide
                undecorator.setFadeOutTransition();
            }
        });

        // Transparent scene and stage
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        // Set minimum size
        stage.setMinWidth(undecorator.getMinWidth());
        stage.setMinHeight(undecorator.getMinHeight());
        if(!w.isModal()){
            stage.setWidth(Util.getDefaultWidth());
            stage.setHeight(Util.getDefaultHeight());
        }
        
        stage.centerOnScreen();

        Initializable ini = (Initializable) fxmlLoader.getController();
        if (w.getWindowList() != null) {
            w.getWindowList().add(ini);
        }
        return ini;
    }

    /**
     * Main method of the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Use in tableColumn.setCellFactory(STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT)
     * to align the text of that column to the right.
     */
    public static Callback<TableColumn, TableCell> STYLE_TABLECOLUMN_RIGHT_ALIGNMENT_TEXT = new Callback<TableColumn, TableCell>() {
        public TableCell call(TableColumn param) {
            TableCell cell = new TableCell() {
                @Override
                public void updateItem(Object item, boolean empty) {
                    if (item != null) {
                        setText(item.toString());
                    }
                }
            };
            //adding style class for the cell
            cell.setAlignment(Pos.CENTER_RIGHT);
            return cell;
        }

    };

}
