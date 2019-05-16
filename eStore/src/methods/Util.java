package methods;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class contains some universal values and helpful methods used in the
 * application.
 *
 * @author Daniel Plaza
 */
public abstract class Util {

    public static final String APP_NAME = "eStore";
    public static final String CONFIG_EXTENSION = ".ini";
    public static final String CONFIG_PATH = "";
    public static final String LANGUAGES_PATH = "languages" + File.separator;
    public static final String LANGUAGE_EXTENSION = ".xml";
    public static final String PlUGINS_PATH = "modules" + File.separator;
    public static final String PLUGIN_EXTENSION = ".jar";
    public static final String RESOURCES_PATH = "ui/resources/";//avoid File.separator through packages
    public static final String RESOURCES_PATH_SHORT = "resources/";
    public static final String DB_PLUGIN_CLASS_NAME = "data.database.DatabaseHelper";
    
    
    public static final SimpleDateFormat SDF_DATEPICKER=new SimpleDateFormat("dd/MM/yyyy");
    
    private static String[] CURRENCY={"€","$","\u00a3"};

    public static final String FACEBOOK_PATH = "https://www.facebook.com/";
    public static final String TWITTER_PATH = "https://twitter.com/";
    
    private static final double SCREEN_REDUCTION=0.7;
    
    
    
    public static ArrayList<String> getCurrency(){
        ArrayList<String> currencyList=new ArrayList<>();
        for(String c:CURRENCY){
            currencyList.add(c);
        }
        return currencyList;
    }
    
    
    /**
     * Find out into a directory in order to get all the files with the extension passed as parameter.
     * 
     * @param path path to search in
     * @param ext extension of the desired files. Ex: ".jpg"
     * @return an array list of filenames
     */
    public static ArrayList<String> findFiles(String path,String ext) {
        ArrayList<String> fileList=new ArrayList<>();
        File f = new File(path);
        String[] allFiles = f.list();
        for (String file : allFiles) {
            if (file.contains(ext)) {
                fileList.add(file.substring(0, file.lastIndexOf(ext)));
            }
        }
        return fileList;
    }
    
    /**
     * This method change a double to a string only showing 2 decimals.
     *
     * @param d value (double) to be formated
     * @return 2 decimals formatted string
     */
    public static String format2d(double d) {
        return String.format("%.2f", round2d(d));
    }

    /**
     * Rounds a decimal number to two decimals format.
     *
     * @param d value (double) to be rounded
     * @return d rounded
     */
    public static double round2d(double d) {
        d = Math.round(d * 100);
        d /= 100;
        return d;
    }

    /**
     * Copy a file to a destination using streams.
     *
     * @param source path of the file to be copied
     * @param dest path where the file is going to be copied
     * @throws Exception
     */
    private static void copyFile(File source, File dest) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new Exception("Unable to copy file: '" + source + "' to destination: '" + dest + "'");
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * It opens a URL in the user's default browser.
     *
     * @param url link to the webpage
     */
    public static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
        }
    }
    
    /**
     * Replaces the commas of a String by dots
     * 
     * @param number String to be changed
     * @return the same String replacing commas by dots
     */
    public static String replaceCommasWithDots(String number){
        return number.replace(",", ".");
    }
    
    public static double getDefaultWidth(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.getWidth()*SCREEN_REDUCTION;
    }
    
    public static double getDefaultHeight(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize.getHeight()*SCREEN_REDUCTION;
    }
    

}
