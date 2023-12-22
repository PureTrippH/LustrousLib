package org.lustrouslib.menu.interfaces;

import org.bukkit.entity.Player;

public interface MenuClient {
    public void pushMenu(GUIMenu menu);
    public void popMenu();
    public Player getPlayer();
}
