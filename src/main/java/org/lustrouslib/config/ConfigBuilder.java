package org.lustrouslib.config;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigBuilder {
    private String configName;
    private Plugin pl;

    public ConfigBuilder(String configName, Plugin pl) {
        this.configName = configName;
        this.pl = pl;
    }

    public ConfigFile build() {
        try {
            return new ConfigFile(pl, configName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
