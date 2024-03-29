package org.lustrouslib.command.specificmenus;


import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.lustrouslib.command.CommandManager;
import org.lustrouslib.command.SubCommand;
import org.lustrouslib.menu.specificmenus.HelpMenu;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HelpCommand implements SubCommand {
    public final String name = "Help";
    public final String desc = ChatColor.WHITE + "Sends a list of all of the commands in Vassals\n" + ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/vassal help";
    private HashMap<String, SubCommand> commands;

    public HelpCommand(HashMap<String, SubCommand> commands) {
        this.commands = commands;
    }

    public void onCommand(PlayerWrapper p, StateHandler state, String[] args) {
        HelpMenu cmds = new HelpMenu(commands, p, state);
        p.getPlayer().playSound(p.getPlayer(), Sound.BLOCK_CHEST_OPEN, 1f, 0.3f);
        p.getPlayer().openInventory(cmds.getInv());
    }
    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }

    @Override
    public List<String> getPossibleArguments(int argIndex) {
        return Collections.EMPTY_LIST;
    }
}