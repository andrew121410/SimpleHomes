package config;

import Main.SimpleHomes;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class HomeFileManager {

    private final static String fileName = "Homes.yml";
    private final SimpleHomes instance;
    private YamlConfiguration homes;
    private File homesFile;

    public HomeFileManager(SimpleHomes instance) {
        this.instance = instance;
    }

    public YamlConfiguration getHomes() {
        if (homes == null) {
            this.reloadHomes();
        }
        return homes;
    }

    private void reloadHomes() {
        if (homesFile == null) {
            homesFile = new File(instance.getDataFolder(), fileName);
        }
        homes = YamlConfiguration.loadConfiguration(homesFile);

        InputStream defHomes = instance.getResource(fileName);
        if (defHomes != null) {
            // What don't we do when the Bukkit staff randomly deprecates methods?
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(defHomes)));
            homes.setDefaults(defConfig);
        }
    }

    public void saveHomes() {
        if (homes == null || homesFile == null) {
            return;
        }
        try {
            getHomes().save(homesFile);
        } catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "Could not save homes file to " + homesFile, e);
        }
    }
}