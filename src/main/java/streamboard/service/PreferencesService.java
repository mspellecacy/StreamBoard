package streamboard.service;

/**
 * Created by mspellecacy on 4/19/2017.
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamboard.core.UserPreferences;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by mspellecacy on 6/11/2016.
 */
public enum PreferencesService {
    INSTANCE;

    private static final String APPNAME = "StreamBoard";

    private Logger log = LoggerFactory.getLogger(PreferencesService.class);

    private String dirSeparator = System.getProperty("file.separator");
    private String userHome = System.getProperty("user.home");
    private String prefsDir = "." + APPNAME;
    private String prefsFileName = "preferences.json";
    private String programDataDir = System.getenv("PROGRAMDATA");
    private String gameSenseEndpoint = "";

    private ObjectMapper mapper = new ObjectMapper();
    private UserPreferences userPrefs;
    private File prefsFile;


    PreferencesService() {
        log.info("PrefsService Starting...");
        mapper.findAndRegisterModules();
        loadPreferences();
    }

    public Boolean loadPreferences() {
        boolean loadedSuccessfully;
        String dir = userHome + dirSeparator + prefsDir + dirSeparator;
        File configDir = new File(dir);
        prefsFile = new File(dir + prefsFileName);

        //See if we have a config dir...
        if (!configDir.isDirectory()) {
            log.warn("Config Dir not found, trying to create file: {}", configDir.getAbsolutePath());
            if (configDir.mkdir()) {
                log.info("Config dir created.");
            } else {
                log.warn("Unable to create config dir: {}", configDir.getAbsolutePath());
                log.warn("Won't be able to save preferences.");
            }
        }

        log.info("User Preferences Path: " + prefsFile.getPath());
        log.info("Opening user preferences...");
        try {
            //If we created a new file, write an empty preferences file.
            if (prefsFile.createNewFile()) {
                userPrefs = new UserPreferences();
                mapper.writeValueAsString(userPrefs);
                // If we did not create a new file load our existing preferences.
            } else userPrefs = mapper.readValue(prefsFile, UserPreferences.class);
            loadedSuccessfully = true;
        } catch (IOException e) {
            log.error("File Save Error : " + e.getMessage());
            userPrefs = new UserPreferences();
            loadedSuccessfully = false;
        }

        return loadedSuccessfully;
    }

    public UserPreferences getUserPrefs() {
        return userPrefs;
    }

    public void setUserPrefs(UserPreferences userPrefs) {
        this.userPrefs = userPrefs;
    }

    public void savePreferences() {
        try {
            mapper.writeValue(prefsFile, userPrefs);
        } catch (IOException e) {
            log.error("Error Saving Preferences: {}", e.getMessage());
        }
        Notifications.create().title(APPNAME).text("Your Preferences have been saved.").show();
    }

}