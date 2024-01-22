package org.lustrouslib.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
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
            plugin.saveResource(name, false);
        }
        configuration = new YamlConfiguration();
        configuration.options().parseComments(true);
        try {
            configuration.load(configFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
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
