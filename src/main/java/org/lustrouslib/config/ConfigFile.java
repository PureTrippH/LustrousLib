package org.lustrouslib.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile {
    private volatile File configFile;
    private volatile FileConfiguration configuration;

    ConfigFile(Plugin plugin, String name) throws IOException {
        this.configFile = new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), name);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw e;
            }
        }
        configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfigFile() {
        return configuration;
    }

    public void save() throws IOException {
        try {
            configuration.save(configFile);
        } catch (IOException e) {
            throw e;
        }
    }


    public void reload() {
        YamlConfiguration.loadConfiguration(configFile);
    }
}
