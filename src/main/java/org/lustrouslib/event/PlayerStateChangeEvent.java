package org.lustrouslib.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.lustrouslib.menu.interfaces.PlayerState;
import org.lustrouslib.wrapper.PlayerWrapper;

public class PlayerStateChangeEvent extends Event implements Cancellable {
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

    private PlayerState currState;
    private PlayerState prevState;
    private PlayerWrapper wrap;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerStateChangeEvent(PlayerWrapper wrap, PlayerState currState, PlayerState prevState) {
        this.wrap = wrap;
        this.currState = currState;
        this.prevState = prevState;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
