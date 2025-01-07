package feature;
import jakarta.servlet.ServletContext;
import util.HelperFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SCWSLocale {

    private static Properties properties = new Properties();
    private static ServletContext sct;

    public synchronized static void load(ServletContext sc, String language) throws IOException, IOException, FileNotFoundException {
        sct = sc;
        properties.load(new FileInputStream(sc.getRealPath(String.format("%s%s.properties", SCWSFeature.get("PATH_LOCALE"), language))));
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public synchronized static String[] list() {
        File[] files = HelperFile.files(sct.getRealPath(SCWSFeature.get("PATH_LOCALE")));
        String[] filesName = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filesName[i] = HelperFile.getOnlyName(files[i]);
        }
        return filesName;
    }

}
