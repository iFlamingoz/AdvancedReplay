package me.jumper251.replay;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.inventories.inventories;
import me.jumper251.replay.listener.InventoryClickListener;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.utils.ReplayCleanup;
import me.jumper251.replay.replaysystem.utils.Utils;
import me.jumper251.replay.utils.LogUtils;
import me.jumper251.replay.utils.Metrics;
import me.jumper251.replay.utils.ReplayManager;
import me.jumper251.replay.utils.Updater;


public class ReplaySystem extends JavaPlugin {

	
	public static ReplaySystem instance;
	
	public static Updater updater;
	public static Metrics metrics;
	
	public final static String PREFIX = Utils.chat("&4Ausirius Replay - &c");

	
	@Override
	public void onDisable() {
		for (Replay replay : new HashMap<>(ReplayManager.activeReplays).values()) {
		    if (replay.isRecording()) {
				replay.getRecorder().stop(ConfigManager.SAVE_STOP);
			}
		}
	}
	@Override
	public void onEnable() {
		instance = this;
		
		Long start = System.currentTimeMillis();

		LogUtils.log("Loading Replay");
		
		ReplayManager.register();
		ConfigManager.loadConfigs();
		inventories.initialize();
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		
		ReplaySaver.register(ConfigManager.USE_DATABASE ? new DatabaseReplaySaver() : new DefaultReplaySaver());
		
		updater = new Updater();
		metrics = new Metrics(this);
		
		if (ConfigManager.CLEANUP_REPLAYS > 0) {
			ReplayCleanup.cleanupReplays();
		}
		
		LogUtils.log("Finished (" + (System.currentTimeMillis() - start) + "ms)");

	}
	
	
	public static ReplaySystem getInstance() {
		return (ReplaySystem) Bukkit.getPluginManager().getPlugin("AusiriusReplay");
	}
}
