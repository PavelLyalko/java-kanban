package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Loader {

    public static Map<String, String> loadPropertiesToMap() {
        String propertiesFilePath = "application.properties";
        Map<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);

            for (String key : properties.stringPropertyNames()) {
                propertiesMap.put(key, properties.getProperty(key));
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при загрузке application properties!");
        }

        return propertiesMap;
    }
}
