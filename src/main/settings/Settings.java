package settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Settings implements ISettings {
    @Override
    public Map<String, String> getSettings() {
        Properties properties = new Properties();
        Map<String, String> result = new HashMap<>();
        try {
            properties.load(new FileInputStream((System.getProperty("user.dir") + "/src/main/resources/SQLSettings.properties")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (String key : properties.stringPropertyNames()) {
            result.put(key, properties.getProperty(key));
        }
        return result;
    }

}