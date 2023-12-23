package org.lustrouslib.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lustrouslib.menu.interfaces.WrapperStorage;

/**
 * Handles State and Plugin Details to Pass to Cmds
 */
public class StateHandler<E> {
    private JavaPlugin p;
    private WrapperStorage<Player, E> wrapStorage;

    public JavaPlugin getPlugin() {
        return p;
    }

    public E getPlayerWrapper(Player p) {
        return wrapStorage.getWrapper(p);
    }
}
