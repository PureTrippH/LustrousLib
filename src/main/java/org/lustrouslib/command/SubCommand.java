package org.lustrouslib.command;

import org.bukkit.entity.Player;
import org.lustrouslib.wrapper.PlayerWrapper;

public interface SubCommand {
    void onCommand(PlayerWrapper p, String[] args);
    String getName();
    String getDesc();
}
