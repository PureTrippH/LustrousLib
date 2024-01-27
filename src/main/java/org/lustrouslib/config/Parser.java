package org.lustrouslib.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map;

public class Parser {
    private ConfigFile file;

    public Parser() {

    }
    public void serializeList(List<ConfigurationSerializable> list, String prevPath) {
        for (int i = 0; i < list.size(); i++) {
            String newPath = prevPath + "." + i;
            ConfigurationSection section = file.getConfigFile().createSection(newPath);
            Map<String, Object> serializedData = list.get(i).serialize();;
            for (Map.Entry<String, Object> entry : serializedData.entrySet()) {
                section.set(entry.getKey(), entry.getValue());
            }
        }
    }

    public void getConfigSetting(String path) {
        file.getConfigFile().get(path);
    }
}
