package me.jumper251.replay.commands.replay;




import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.utils.Utils;
import me.jumper251.replay.utils.fetcher.Consumer;

public class ReplayPlayCommand extends SubCommand {

	public ReplayPlayCommand(AbstractCommand parent) {
		super(parent, "play", "Starts a recorded replay", "play <Name>", true);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length != 2) return false;
		
		String name = args[1];
		
		final Player p = (Player)cs;	
		
		if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(p.getName())) {
			if (name.startsWith("&6P")) {
				if (!Bukkit.getServerName().equals("Practice")) {
					cs.sendMessage(Utils.chat("Sorry but you must be in practice to view this replay!"));
					return true;
				}
			} else if (name.startsWith("&6AR")) {
				if (!Bukkit.getServerName().equals("ArmsRace")) {
					cs.sendMessage(Utils.chat("Sorry but you must be in ArmsRace to view this replay!"));
					return true;
				}
			}
			p.sendMessage(ReplaySystem.PREFIX + "Loading replay §e" + name + "§7...");
			try {
				ReplaySaver.load(args[1], new Consumer<Replay>() {
					
					@Override
					public void accept(Replay replay) {
						p.sendMessage(ReplaySystem.PREFIX + "Replay loaded. Duration §e" + (replay.getData().getDuration() / 20) + "§7 seconds.");
						replay.play(p);
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				
				p.sendMessage(ReplaySystem.PREFIX + "§cError while loading §o" + name + ".replay. §r§cCheck console for more details.");
			}
		} else {
			p.sendMessage(ReplaySystem.PREFIX + "§cReplay not found.");
		}
		
		return true;
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return ReplaySaver.getReplays().stream()
				.filter(name -> StringUtil.startsWithIgnoreCase(name, args.length > 1 ? args[1] : null))
				.collect(Collectors.toList());
	}
	

	
}
