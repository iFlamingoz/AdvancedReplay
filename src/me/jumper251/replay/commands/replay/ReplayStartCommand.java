package me.jumper251.replay.commands.replay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.utils.Utils;
import me.jumper251.replay.utils.ReplayManager;

public class ReplayStartCommand extends SubCommand {

	public ReplayStartCommand(AbstractCommand parent) {
		super(parent, "start", "Records a new replay", "start <Name> [<Players ...>]", false);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length < 2) return false;
		
		String name = args[1];
		if (name.length() > 40) {
			cs.sendMessage(ReplaySystem.PREFIX + "§cReplay name is too long.");
			return true;
		}
		
		if (args.length == 2) {
			if (!ReplaySaver.exists(name) && !ReplayManager.activeReplays.containsKey(name)) {
				 ReplayAPI.getInstance().recordReplay(name, cs);
				 
				 cs.sendMessage(ReplaySystem.PREFIX + "§aSuccessfully started recording §e" + name + "§7.\n§7Use §6/Replay stop " + name + "§7 to save it.");
				 cs.sendMessage("§7INFO: You are recording all online players.");
				 
			} else {
				cs.sendMessage(ReplaySystem.PREFIX + "§cReplay already exists.");
			}
			
		} else {
			if (args[0].equalsIgnoreCase("console")) {
				if (!(cs instanceof Player)) {
					if (args.length == 3) {
						List<Player> playerList = new ArrayList<Player>();
						int e = 0;
						while (e <= 2) {
							if (Bukkit.getOnlinePlayers().contains(args[e+1])) {
								playerList.add(Bukkit.getPlayer(args[e+1]));
							}
						}
						Player[] players = new Player[playerList.size()];
						players = playerList.toArray(players);
						if (players.length > 0) {
							if (Bukkit.getServerName().equals("Practice")) {
								ReplayAPI.getInstance().recordReplay(Utils.chat("&4PRAC - &6" + players[0] + " Vs " + players[1] + " &0&l") + String.valueOf(ReplaySaver.getReplays().size()+1), cs, players);
							} else if (Bukkit.getServerName().equals("ArmsRace")) {
								ReplayAPI.getInstance().recordReplay(Utils.chat("&4ARMSR - &6" + players[0] + " Vs " + players[1] + " &0&l") + String.valueOf(ReplaySaver.getReplays().size()+1), cs, players);
							}
						 
							cs.sendMessage(ReplaySystem.PREFIX + Utils.chat("Saved replay!"));
						}
					} else {
						cs.sendMessage(Utils.chat("&4CONSOLE WRONG COMMAND - REPLAY!"));
						return true;
					}
				} else {
					cs.sendMessage(Utils.chat("&cSorry this command is unavailable."));
					return true;
				}// ------------------------------------
			} else if (!ReplaySaver.exists(name) && !ReplayManager.activeReplays.containsKey(name)) {
				
				List<Player> playerList = new ArrayList<Player>();
				for (int i = 2; i < args.length; i++) {
					if (Bukkit.getPlayer(args[i]) != null) {
						playerList.add(Bukkit.getPlayer(args[i]));
					}
				}
				
				Player[] players = new Player[playerList.size()];
				players = playerList.toArray(players);
				if (players.length > 0) {
					ReplayAPI.getInstance().recordReplay(name, cs, players);
				 
					cs.sendMessage(ReplaySystem.PREFIX + "§aSuccessfully started recording §e" + name + "§7.\n§7Use §6/Replay stop " + name + "§7 to save it.");
				}
				 
			} else {
				cs.sendMessage(ReplaySystem.PREFIX + "§cReplay already exists.");
			}
		} 
		
		
		return true;
	}

	
}
