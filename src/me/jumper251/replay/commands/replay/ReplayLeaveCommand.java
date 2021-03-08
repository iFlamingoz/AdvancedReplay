package me.jumper251.replay.commands.replay;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.replaysystem.utils.Utils;

public class ReplayLeaveCommand extends SubCommand {

	public ReplayLeaveCommand(AbstractCommand parent) {
		super(parent, "leave", "Leave your Replay", "leave", true);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {		
		Player p = (Player) cs;
		
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
			
			replayer.stop();
			plugin().getServer().getScheduler().runTaskAsynchronously(plugin(), new Runnable() {
				
				@Override
				public void run() {
					if (Bukkit.getServerName().equals("Practice")) {
						Utils.Teleport(p, Bukkit.getWorld("world"), 4.5, 14, -5);	
					} else if (Bukkit.getServerName().equals("Lobby")) {
						Utils.Teleport(p, Bukkit.getWorld("world"), 0.5, 16, 0.5);
					}	
				}
			});
		} else {
			p.sendMessage(ReplaySystem.PREFIX + "§cYou need to play a Replay first");
		}
		
		return true;
	}

	
}
