package kernx.utils;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = new Properties();

    static {
        load();
    }

    public static void load() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            // Default settings
            properties.setProperty("pageSize", "4"); // 4 KB
            properties.setProperty("totalMemory", "64"); // 64 KB
            save();
        }
    }

    public static void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    public static void setInt(String key, int value) {
        properties.setProperty(key, String.valueOf(value));
        save();
    }

    public static int getPageSize() {
        return getInt("pageSize", 4);
    }

    public static int getTotalMemory() {
        return getInt("totalMemory", 64);
    }
}
