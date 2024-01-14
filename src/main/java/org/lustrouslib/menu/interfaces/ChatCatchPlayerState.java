package org.lustrouslib.menu.interfaces;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.lustrouslib.wrapper.PlayerWrapper;

public interface ChatCatchPlayerState extends PlayerState {
    void chatAction(AsyncPlayerChatEvent e, PlayerWrapper pw);
}
