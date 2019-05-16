package methods;

import data.config.Config;
import data.config.Language;
import data.database.DatabaseManager;

/**
 *
 * @author Daniel Plaza
 */
public class DataSource {
    
    private static final DataSource INSTANCE=new DataSource();
    
    private final DatabaseManager db;
    private final Config config;
    private final Language lang;
    
    private DataSource(){
        config=Config.getConfig();
        lang=Language.getLanguage(config.getSetting(Config.SETTING.language),Util.LANGUAGES_PATH);
        db=new DatabaseManager(config);
    }
            
    public static DataSource getDataSource(){
        return INSTANCE; 
    }
    
    public DatabaseManager database(){
        return db;
    }
    
    public Config getConfig(){
        return config;
    }
    
    public String write(String markup){
        return lang.write(markup);
    }
    
}
