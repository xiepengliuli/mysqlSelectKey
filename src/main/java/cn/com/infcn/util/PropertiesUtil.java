package cn.com.infcn.util;

import java.util.ResourceBundle;

public class PropertiesUtil {
    public static ResourceBundle rb = ResourceBundle.getBundle("config");

    public static String getString(String key) {
        return rb.getString(key);
    };

}
