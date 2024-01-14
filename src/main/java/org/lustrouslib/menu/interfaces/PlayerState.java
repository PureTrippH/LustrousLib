package org.lustrouslib.menu.interfaces;

import org.bukkit.event.player.PlayerInteractEvent;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

/**
 * Interface for a left & right click action state.
 */
public interface PlayerState<E> {
    public Menu getPrevMenu();
    public Menu getCurrMenu();
    public void destruct(StateHandler<E> pw);
}
