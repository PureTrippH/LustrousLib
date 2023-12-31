package org.lustrouslib.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.lustrouslib.command.specificmenus.HelpCommand;
import org.lustrouslib.wrapper.PlayerWrapper;
import org.lustrouslib.wrapper.StateHandler;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * A Base Command Handler for the Main command.
 * This Provides a base to all subcommands
 */
public class CommandManager implements CommandExecutor, TabExecutor {
    private String msgPrefix = ChatColor.of(new Color(140, 212, 191)) + "Vassals: ";
    private static HashMap<String, SubCommand> commands = new HashMap<>();
    private StateHandler<? extends PlayerWrapper> state;
    private String commandName;
    Set<String> possibleCommands;

    public CommandManager(String commandName, StateHandler state) {
        this.commandName = commandName;
        this.state = state;
        this.possibleCommands = new HashSet<String>();
        this.registerCommand("help", new HelpCommand());
        state.getPlugin().getCommand(commandName).setExecutor(this);
    }

    public void registerCommand(String commandName, SubCommand cmd) {
        this.commands.put(commandName, cmd);
        this.possibleCommands.add(commandName);
    }

    public static HashMap<String, SubCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) throw new IllegalArgumentException("Only Players Can Run This Command!");

        if (!command.getName().equalsIgnoreCase(this.commandName)) {
            return true;
        }
        Player p = (Player) sender;
        PlayerWrapper wrappedPlayer = new PlayerWrapper(p);
        try {
            if (args == null) {
                commands.get("help").onCommand(wrappedPlayer, state, args);
            } else if (args.length == 0) {
                commands.get("help").onCommand(wrappedPlayer, state, args);
            } else if (!commands.containsKey(args[0])) {
                p.sendMessage(msgPrefix + "Command Does Not Exist! Check the List Below");
                commands.get("help").onCommand(wrappedPlayer, state, args);
            } else {
                commands.get(args[0]).onCommand(wrappedPlayer, state, args);
            }
        } catch(Exception e) {
            p.sendMessage(msgPrefix + e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], possibleCommands, completions);
            Collections.sort(completions);
            return completions;
        }
        return Collections.EMPTY_LIST;
    }
}
