package org.lustrouslib.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.lustrouslib.menu.interfaces.GUIMenu;
import org.lustrouslib.menu.interfaces.MenuClient;

import java.util.ArrayList;

/**
 * A generic GUI that a player can interact with.
 * @author Tripp H.
 * @version 1.0
 */
public abstract class Menu implements Listener, GUIMenu {
    protected final Inventory inv;
    private Plugin pl;
    protected ArrayList<MenuIcon> contents;
    private MenuClient user;
    private int page = 1;

    /**
     * Creates a menu with multiple pages.
     * @param name String name of inventory.
     * @param page int page number.
     */
    public Menu(String name, int page, MenuClient user, JavaPlugin pl) {
        this.inv = Bukkit.createInventory(null, 9*3, name);
        this.contents = new ArrayList<MenuIcon>();
        initializeItems(page);
        this.pl = pl;
        this.user = user;
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    /**
     * Creates a One Page Menu
     * @param name String name of menu
     */
    public Menu(String name, MenuClient user, JavaPlugin pl) {
        this(name, 1, user, pl);
    }


//Test
    /**
     * Priminventory with placeholders and next Page
     */
    public void initializeItems(int page) {
        initializeItems(page, false);
    }
    public void initializeItems(int page, boolean disablePages) {
        //Populates Void Slots
        for (int i = 0; i < 27; i++) {
            if (i / 9 != 1) {
                inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        refreshContents();
        if(!disablePages) {
            ItemStack leftArrow = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta leftMeta = (SkullMeta) leftArrow.getItemMeta();
            leftMeta.setDisplayName(ChatColor.RED + "<- BACK");
            leftMeta.setOwner("MHF_ArrowLeft");
            leftArrow.setItemMeta(leftMeta);
            inv.setItem(9, leftArrow);
            ItemStack rightArrow = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta rightMeta = (SkullMeta) leftArrow.getItemMeta();
            rightMeta.setDisplayName(ChatColor.GREEN + "NEXT ->");
            rightMeta.setOwner("MHF_ArrowRight");
            rightArrow.setItemMeta(rightMeta);
            inv.setItem(17, rightArrow);
            inv.setItem(22, new ItemStack(Material.BARRIER));
        }
    }

    protected void refreshContents() {
        for (int i = 0 + (7*(page-1)); i < 7 + (7*(page-1)); i++) {
            if (i < contents.size()) {
                inv.setItem(10 + (i - (7*(page-1))), contents.get(i).getIcon());
            }
        }
    }

    @EventHandler
    public void onInvClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() == 9 && page > 1) {
            --page;
            inv.clear();
            initializeItems(page);
        }
        if(e.getRawSlot() == 17 && page <= Math.ceil(contents.size()/7)) {
            ++page;
            inv.clear();
            initializeItems(page);
        }
        if(e.getRawSlot() == 22) {
            user.popMenu();
        }
        if (e.getRawSlot() > 9 && e.getRawSlot() < 17) {
            if (e.isRightClick()) {
                contents.get(e.getRawSlot() - 10).onRightClick();
            } else {
                contents.get(e.getRawSlot() - 10).onClick();
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onInvDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    public Inventory getInv() { return inv; }

    @Override
    public void open(Player p) {
        p.openInventory(this.inv);
    }

}
