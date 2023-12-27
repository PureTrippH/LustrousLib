package org.lustrouslib.menu.specificmenus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.command.SubCommand;
import org.lustrouslib.menu.interfaces.MenuClient;
import org.lustrouslib.wrapper.StateHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelpMenu extends Menu {

    public HelpMenu(HashMap<String, SubCommand> commands, MenuClient mc, StateHandler state) {
        super( "Help Menu", mc, state.getPlugin());
        populateItems(commands);
    }

    public void populateItems(HashMap<String, SubCommand> commands) {
        for (SubCommand value : commands.values()) {
            if (value.getDesc() != null) {
                List<String> desc = Arrays.asList(value.getDesc().split("\n"));
                super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.LIME_CANDLE,
                    ChatColor.GREEN + value.getName(), "HelpIcon", desc)));
            } else {
                super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.LIME_CANDLE,
                    ChatColor.GREEN + value.getName(), "HelpIcon")));
            }
        }
        refreshContents();
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
            }
        }
    }
}
