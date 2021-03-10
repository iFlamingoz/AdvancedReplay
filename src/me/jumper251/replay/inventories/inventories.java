package me.jumper251.replay.inventories;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.utils.Utils;
import me.jumper251.replay.utils.fetcher.Consumer;

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
			int e = 1;
			if (replays.size() == 0) {
				return null;
			}
			replays.sort(dateComparator());
			while (replays.size() >= e) {
				Utils.createItem(replay_inv, 339, 1, e, Utils.chat(replays.get(e-1)), Utils.chat("&4Ausirius Replays"));
				e++;
			}
			toReturn.setContents(replay_inv.getContents());
			return toReturn;
		} else {
			return null;
		}
	}
	public static Comparator<String> dateComparator() {
		return (s1, s2) -> {
			if (getCreationDate(s1) != null && getCreationDate(s2) != null) {
				return getCreationDate(s1).compareTo(getCreationDate(s2));
			} else {
				return 0;
			}
			
		};
	}
	private static Date getCreationDate(String replay) {
		if (ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
			return new Date(new File(DefaultReplaySaver.DIR, replay + ".replay").lastModified());
		}
		
		if (ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
			return new Date(DatabaseReplaySaver.replayCache.get(replay).getTime());
		}
		
		return null;
	}
	
	public static void clicked(Player p, int slot, ItemStack clicked, String inv) {
		if (inv.equals("replay")) {
			if (clicked == null || clicked.getType().equals(Material.AIR)) {
				return;
			}
			if (ReplaySaver.exists(clicked.getItemMeta().getDisplayName())) {
				String replayName0 = clicked.getItemMeta().getDisplayName();
				if (replayName0.startsWith("&6P")) {
					if (!Bukkit.getServerName().equals("Practice")) {
						p.sendMessage(Utils.chat("Sorry but you must be in practice to view this replay!"));
						return;
					}
				} else if (replayName0.startsWith("&6AR")) {
					if (!Bukkit.getServerName().equals("ArmsRace")) {
						p.sendMessage(Utils.chat("Sorry but you must be in ArmsRace to view this replay!"));
						return;
					}
				}
				try {
					ReplaySaver.load(replayName0, new Consumer<Replay>() {
						
						@Override
						public void accept(Replay replay) {
							p.sendMessage(ReplaySystem.PREFIX + Utils.chat("&cYou have loaded " + replayName0 + ". &6BETA REPLAY"));
							replay.play(p);
							p.sendMessage(ReplaySystem.PREFIX + Utils.chat("&6PLEASE REPORT BUGS TO THE DISCORD /DISCORD"));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();

					p.sendMessage(ReplaySystem.PREFIX + Utils.chat("An error occurred"));
				}
			} else {
				p.sendMessage(Utils.chat("&4Ausirius Replay - &cSorry something went wrong.."));
			}
		}
	}
	
}
