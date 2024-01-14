package org.lustrouslib.event;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.lustrouslib.LustrousLib;
import org.lustrouslib.menu.interfaces.ChatCatchPlayerState;
import org.lustrouslib.menu.interfaces.ClickablePlayerState;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

/**
 * A Listener for The Player State Interactions and Transitions
 */
public class PlayerStateHandler implements Listener {
    private StateHandler<? extends PlayerWrapper> state;

    public PlayerStateHandler(StateHandler<? extends PlayerWrapper> state) {
        this.state = state;
        LustrousLib.getInstance().getServer().getPluginManager()
                .registerEvents(this, LustrousLib.getPlugin(LustrousLib.class));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        PlayerWrapper wp = state.getPlayerWrapper(e.getPlayer());
        if (wp.getState() == null) return;
        if (!(wp.getState() instanceof ClickablePlayerState)) return;
        ClickablePlayerState currState = (ClickablePlayerState) wp;
        e.setCancelled(true);
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
            currState.leftClickAction(e, wp);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            currState.rightClickAction(e, wp);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer() == null) return;
        PlayerWrapper wp = state.getPlayerWrapper(e.getPlayer());
        if (wp.getState() == null) return;
        if (!(wp.getState() instanceof ChatCatchPlayerState)) return;
        ChatCatchPlayerState currState = (ChatCatchPlayerState) wp;
        e.setCancelled(true);
        currState.chatAction(e, wp);
    }
}
