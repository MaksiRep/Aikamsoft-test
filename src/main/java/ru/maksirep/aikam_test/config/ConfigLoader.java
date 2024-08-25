package ru.maksirep.aikam_test.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties = new Properties();

    public ConfigLoader(String propertiesFileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
