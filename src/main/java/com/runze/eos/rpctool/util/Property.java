package com.runze.eos.rpctool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    private static final Properties Properties = new Properties();
    static {
        try {
            InputStream in = Property.class.getResourceAsStream("/property.properties");
            Properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getProperty(String key) {
        return Properties.getProperty(key);
    }
}
