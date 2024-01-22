package org.lustrouslib.menu.interfaces;

import org.bukkit.event.player.PlayerInteractEvent;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

/**
 * Interface for a left & right click action state.
 */
public interface PlayerState<E> {
    public GUIMenu getPrevMenu();
    public GUIMenu getCurrMenu();
    public void destruct(PlayerWrapper pw);
    public void setCurrMenu(GUIMenu menu);
    public void setPrevMenu(GUIMenu menu);
}
