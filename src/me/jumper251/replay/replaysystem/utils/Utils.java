package me.jumper251.replay.replaysystem.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Utils {
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
    @SuppressWarnings("deprecation")
	public static ItemStack createItem(Inventory inv, int materialId, int amount, int invSlot, String displayName,
			String... loreString) {

		ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = new ItemStack(Material.getMaterial(materialId), amount);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);

		inv.setItem(invSlot - 1, item);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItemByte(Inventory inv, int materialId, int byteId, int amount, int invSlot,
			String displayName, String... loreString) {

		ItemStack item;
		List<String> lore = new ArrayList<String>();

		item = new ItemStack(Material.getMaterial(materialId), amount, (short) byteId);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.chat(displayName));
		for (String s : loreString) {
			lore.add(Utils.chat(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		inv.setItem(invSlot - 1, item);
		return item;
	}
	
	public static void Teleport(Player player, String world, double x, double y, double z) {
		if (player.isInsideVehicle()) player.eject();
		player.closeInventory();
		player.teleport(new Location(Bukkit.getWorld(world), x, y, z));
	}
	
	public static String OGWorldsCheck(String worldname) {
		String name = worldname.toLowerCase();
		if (name.startsWith("node")) return "node";
		else if (name.startsWith("soup")) return "soup";
		else if (name.startsWith("sumo")) return "sumo";
		else if (name.startsWith("combo")) return "combo";
		else if (name.startsWith("bridge")) return "bridge";
		else if (name.startsWith("parkour")) return "parkour";
		else return "none";
	}
}
