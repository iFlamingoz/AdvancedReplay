package me.jumper251.replay.inventories;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.utils.Utils;

public class inventories {
	public static Inventory replay_inv;
	public static String replay;
	public static int replay_Invrows = 5 * 9;
	
	public static void initialize() {
		replay = Utils.chat("&6Replay");
		
		replay_inv = Bukkit.createInventory(null, replay_Invrows);
	}
	
	public static Inventory GUI (Player p, String mode) {
		if (mode == "replay") {
			Inventory toReturn = Bukkit.createInventory(null, replay_Invrows, replay);
			List<String> replays = ReplaySaver.getReplays();
			int e = 0;
			if (replays.size() == 0) {
				p.sendMessage(Utils.chat("&4Ausirius &cSorry no replays seem to be available"));
				return null;
			}
			while (replays.size() != e) {
				Utils.createItem(replay_inv, 1, 1, e, replays.get(e), "&4Ausirius Replays");
				e++;
			}
			toReturn.setContents(replay_inv.getContents());
			return toReturn;
		} else {
			return null;
		}
	}
	
	public static void clicked(Player p, int slot, ItemStack clicked, String inv) {
		if (clicked.equals(new ItemStack(Material.PAPER))) {
			
		}
	}
	
}
