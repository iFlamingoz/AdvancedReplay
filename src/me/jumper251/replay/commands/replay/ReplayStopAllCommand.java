package me.jumper251.replay.commands.replay;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.utils.ReplayManager;

public class ReplayStopAllCommand extends SubCommand{
	public ReplayStopAllCommand(AbstractCommand parent) {
		super(parent, "stopall", "Stops all replays", "stopall", false);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (ReplayManager.activeReplays.size() == 0) {
			sender.sendMessage("Sorry there does not seem like there are replay active!");
			return true;
		}
		for (Replay replay : new HashMap<>(ReplayManager.activeReplays).values()) {
		    if (replay.isRecording()) {
				replay.getRecorder().stop(ConfigManager.SAVE_STOP);
			}
		}
		return true;
	}
}
