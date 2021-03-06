package me.jumper251.replay.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.jumper251.replay.inventories.inventories;
import me.jumper251.replay.replaysystem.utils.Utils;


public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

	@SuppressWarnings("unused")
	private String command, description;
	private String permission;
	private List<SubCommand> subCommands;
	private MessageFormat format;
	
	
	public AbstractCommand(String command, String description, String permission) {
		this.command = command;
		this.permission = permission;
		this.description = description;
		this.subCommands = new ArrayList<>();
		
		this.subCommands = Arrays.asList(setupCommands());
	}
	
	
	public String getCommand() {
		return command;
	}
	
	public String getPermission() {
		return permission;
	}
	
	protected abstract SubCommand[] setupCommands();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		String arg = args.length >= 1 ? args[0] : "overview";

		if (checkPermission(cs, arg)) {
			
			if (args.length == 0) {
				Player player = (Player) cs;
				if (inventories.GUI(player, "replay", 0) != null) {
					player.openInventory(inventories.GUI(player, "replay", 0));
				} else {
					player.sendMessage(Utils.chat("&4Ausirius Replays - &cNo replays were found/is available."));
				}
			} else {
				for (SubCommand sub : this.subCommands) {
					if (sub.getLabel().equalsIgnoreCase(arg) || sub.getAliases().contains(arg)) {
						
						if (sub.isPlayerOnly() && !(cs instanceof Player)) {
							cs.sendMessage(this.format.getConsoleMessage());
							return true;
						}
						
						if (!sub.execute(cs, cmd, label, args)) {
							cs.sendMessage(Utils.chat("&cInvalid Usage!"));
						}
						
						return true;
					}
				}
				cs.sendMessage(Utils.chat("&cInvalid Arguments!"));
			}
			
		} else {
			if (args.length == 0) {
				Player player = (Player) cs;
				if (inventories.GUI(player, "replay", 0) != null) {
					player.openInventory(inventories.GUI(player, "replay", 0));	
				} else {
					player.sendMessage(Utils.chat("&4Ausirius Replays - &cNo replays were found/is available."));
				}
			}
		}
		
		return true;
	}


	@Override
	public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
		String perm = args.length >= 1 ? args[0] : "overview";
		
		if (!checkPermission(cs, perm)) return new ArrayList<String>();
		
		
		if (args.length == 1) {
			return this.subCommands.stream()
					.map(SubCommand::getLabel)
					.filter(name -> StringUtil.startsWithIgnoreCase(name, args[0]))
					.collect(Collectors.toList());
		} else {
			SubCommand sub = this.subCommands.stream()
					.filter(sc -> sc.getLabel().equalsIgnoreCase(args[0]) || sc.getAliases().contains(args[0]))
					.findAny()
					.orElse(null);
			
			return sub != null ? sub.onTab(cs, cmd, label, args) : null;
		}
		
	}
	
	private boolean checkPermission(CommandSender cs, String arg) {
		return this.permission == null || cs.hasPermission(this.permission + "." + arg);
	}
	

}
