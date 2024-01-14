package org.lustrouslib.menu.interfaces;

import org.bukkit.event.player.PlayerInteractEvent;
import org.lustrouslib.wrapper.PlayerWrapper;

public interface ClickablePlayerState extends PlayerState {
    void rightClickAction(PlayerInteractEvent e, PlayerWrapper pw);
    void leftClickAction(PlayerInteractEvent e, PlayerWrapper pw);
}
