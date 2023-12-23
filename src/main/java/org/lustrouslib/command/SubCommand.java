package org.lustrouslib.command;

import org.bukkit.entity.Player;
import org.lustrouslib.wrapper.PlayerWrapper;

/**
 * Defines a subcommand and its functionality
 */
public interface SubCommand {
    void onCommand(PlayerWrapper p, String[] args);
    String getName();
    String getDesc();
}
