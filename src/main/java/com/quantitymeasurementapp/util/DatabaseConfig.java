package com.quantitymeasurementapp.util;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
	
	private static final Properties properties = new Properties();

    static {
        try {
            InputStream input = DatabaseConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");

            if (input != null) {
                properties.load(input);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
