package org.lustrouslib.command;

import org.bukkit.entity.Player;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

import java.util.List;
import java.util.Map;

/**
 * Defines a subcommand and its functionality
 */
public interface SubCommand {
    void onCommand(PlayerWrapper p, StateHandler<? extends PlayerWrapper> state, String[] args);
    String getName();
    String getDesc();

    List<String> getPossibleArguments(int argIndex);
}
