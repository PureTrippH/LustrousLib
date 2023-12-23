package org.lustrouslib.menu.interfaces;

import org.bukkit.entity.Player;

/**
 * Interface to Determine if an Object
 * Can have a Menu History
 */
public interface MenuClient {
    public void pushMenu(GUIMenu menu);
    public void popMenu();
    public Player getPlayer();
}
