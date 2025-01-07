package feature;
import jakarta.servlet.ServletContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
public class SCWSFeature {

    private static Properties properties = new Properties();
    private static ServletContext sct;

    public synchronized static void load(ServletContext sc) throws FileNotFoundException, IOException {
        sct=sc;
        properties.load(new FileInputStream(sc.getRealPath("resource/features.properties")));
        properties.put("OS", System.getProperty("os.name"));
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static Enumeration<Object> getKeys() {
        return properties.keys();
    }

    public static void set(String key, String value) {
        properties.put(key, value);
    }

    public synchronized static void save() throws FileNotFoundException, IOException {
        properties.store(new FileOutputStream(sct.getRealPath("resource/features.properties")), "SCALE CONNECTOR");
    }


}
