package org.lustrouslib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LustrousLib extends JavaPlugin {
    private static LustrousLib instance;

    @Override
    public void onEnable() {
        instance = this;
        // Any initialization code when the plugin is enabled
        Bukkit.getLogger().info(ChatColor.AQUA + "----------------------");
        Bukkit.getLogger().info(ChatColor.DARK_AQUA + "LustrousLib: " + ChatColor.GREEN + "Enabled");
        Bukkit.getLogger().info(ChatColor.AQUA + "----------------------");

    }

    @Override
    public void onDisable() {
        // Any cleanup code when the plugin is disabled
        Bukkit.getLogger().info(ChatColor.AQUA + "----------------------");
        Bukkit.getLogger().info(ChatColor.DARK_AQUA + "LustrousLib: " + ChatColor.RED + "Disableds");
        Bukkit.getLogger().info(ChatColor.AQUA + "----------------------");
    }

    public static LustrousLib getInstance() {
        return instance;
    }
}
