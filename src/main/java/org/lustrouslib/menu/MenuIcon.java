package org.lustrouslib.menu;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * A Clickable icon in a menu and the icon state
 */
public class MenuIcon {
    private Runnable leftClickAction;
    private Runnable rightClickAction;
    private ItemStack icon;

    public MenuIcon(ItemStack icon, Runnable leftClickAction, Runnable rightClickAction) {
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
        this.icon = icon;
    }
    public MenuIcon(ItemStack icon, Runnable leftClickAction) {
        this(icon, leftClickAction, () -> {return;});
    }
    public MenuIcon(ItemStack icon) {
        this(icon, () -> {return;}, () -> {return;});
    }

    public void onClick() {
        leftClickAction.run();
    }
    public void onRightClick() {
        rightClickAction.run();
    }
    public ItemStack getIcon() {
        return icon;
    }

    public static ItemStack generateItem(Material m, String name, String value, List<String> lore) {
        ItemStack newIcon = new ItemStack(m);
        ItemMeta meta = newIcon.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (lore != null) {
            meta.setLore(lore);
        }
        newIcon.setItemMeta(meta);
        return newIcon;
    }

    public static ItemStack generateSkull(String name, String value, List<String> lore,
                                             UUID playerName) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(playerName);
        ItemStack newIcon = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) newIcon.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setOwner(p.getName());
        if (lore != null) {
            meta.setLore(lore);
        }
        newIcon.setItemMeta(meta);
        return newIcon;
    }
    public static ItemStack generateCustomSkull(String name, String value, List<String> lore,
                                                   String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        String url = ("http://textures.minecraft.net/texture/" + texture);
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            Bukkit.getLogger().info(ChatColor.RED + e.getMessage());
        }
        item.setItemMeta(itemMeta);
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        System.out.println(item);
        return item;
    }

    public static ItemStack generateItem(Material m, String name, String value) {
        return generateItem(m, name, value, new ArrayList<String>());
    }
}
