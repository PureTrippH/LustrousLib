package org.lustrouslib.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lustrouslib.config.ConfigFile;
import org.lustrouslib.menu.interfaces.WrapperStorage;

import java.util.ArrayList;

/**
 * Handles State and Plugin Details to Pass to Cmds
 */
public class StateHandler<E> {
    private JavaPlugin p;
    private ArrayList<ConfigFile> files;
    private WrapperStorage<Player, E> wrapStorage;

    public StateHandler(JavaPlugin p, WrapperStorage<Player, E> wrapStorage) {
        this.p = p;
        this.wrapStorage = wrapStorage;
    }

    public JavaPlugin getPlugin() {
        return p;
    }

    public E getPlayerWrapper(Player p) {
        return wrapStorage.getWrapper(p);
    }
}
