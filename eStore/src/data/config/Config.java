package data.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import methods.Util;
/**
 * This class works as a wrapper for the configuration file
 *
 * @author Daniel Plaza
 */
public class Config {
    
    /**
     * Enun with the available setting commands.
     * 
     * Each enum constant has 2 fields: its name and its default value.
     */
    public static enum SETTING {
        
        language("language","spanish"),
        dbplugin("dbplugin","hsqldbplugin"),
        dbpath("dbpath","hsqldbdatabase/"),
        dbname("dbname",Util.APP_NAME+"db"),
        dbuser("dbuser","sa"),
        dbpassword("dbpassword",""),
        currency("currency","€"),
        facebook("facebook",""),
        twitter("twitter","");

        private final String varname;
        private final String defaultValue;
        
        SETTING(String varname,String defaultValue) {
            this.varname = varname;
            this.defaultValue=defaultValue;
        }
        
        public String getDefaultValue(){
            return defaultValue;
        }
        
        @Override
        public String toString() {
            return varname;
        }
    }
    
    private final String configPath;
    private final Properties properties;
    private OutputStream output = null;
    private static Config INSTANCE=new Config();
    

    private Config() {
        properties = new Properties();
        configPath = Util.CONFIG_PATH+Util.APP_NAME+Util.CONFIG_EXTENSION;
        readProperties();
    }
    
    public static Config getConfig(){
        return INSTANCE;
    }
    
    /**
     * This method reads the configuration file and returns the desired
     * setting value.
     * 
     * @param setting the setting you need
     * @return the value of the setting
     */
    public String getSetting(SETTING setting) {
        return properties.getProperty(setting.toString());
    }
    
    /**
     * Changes the value of a property
     * 
     * @param setting the setting to be created/modificated
     * @param newValue the new value of the setting
     */
    public void setSetting(SETTING setting,String newValue){
        try {
            output = new FileOutputStream(configPath);
            // set the property value
            properties.setProperty(setting.toString(), newValue);
            // save properties to project root folder
            properties.store(output, null);
            System.out.println(setting.toString()+"="+ newValue);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    
    /**
     * Read the properties file or creates one if it doesn't exists
     */
    private void readProperties() {
        InputStream input = null;
        try {
            input = new FileInputStream(configPath);
            properties.load(input);
        } catch (IOException ex) {
            setDefaultProperties();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Creates a new configuration file with the default values
     */
    private void setDefaultProperties() { 
        try {
            output = new FileOutputStream(configPath);
            
            // set the properties value
            for(SETTING s:SETTING.values()){
                properties.setProperty(s.toString(),s.getDefaultValue());
            }

            // save properties to project root folder
            properties.store(output, null);
            System.out.println("Creating "+configPath);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
