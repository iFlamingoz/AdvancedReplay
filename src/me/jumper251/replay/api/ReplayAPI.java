package me.jumper251.replay.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.saving.IReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.replaysystem.utils.Utils;
import me.jumper251.replay.utils.ReplayManager;
import me.jumper251.replay.utils.fetcher.Consumer;

public class ReplayAPI {

	private static ReplayAPI instance;

	private HookManager hookManager;

	private ReplayAPI() {
		this.hookManager = new HookManager();
	}

	public void registerHook(IReplayHook hook) {
		this.hookManager.registerHook(hook);
	}

	public void unregisterHook(IReplayHook hook) {
		this.hookManager.unregisterHook(hook);
	}

	public int GetReplaysNum() {
		return ReplaySaver.getReplays().size();
	}
	
	public int getActiveReplays() {
		return ReplayManager.activeReplays.size();
	}
	
	public int getTotalReplays() {
		return getActiveReplays()+GetReplaysNum();
	}

	public Replay recordReplay(String name, CommandSender sender, Player... players) {
		List<Player> toRecord = new ArrayList<Player>();

		if (players != null && players.length > 0) {
			for (Player p : players) {
				toRecord.add(p);
			}
		} else {
			for (Player all : Bukkit.getOnlinePlayers()) {
				toRecord.add(all);
			}
		}

		Replay replay = new Replay();
		if (name != null)
			replay.setId(name);
		replay.recordAll(toRecord, sender);

		return replay;
	}

	public void stopReplay(String name, boolean save) {
		if (ReplayManager.activeReplays.containsKey(name)) {
			Replay replay = ReplayManager.activeReplays.get(name);
			if (replay.isRecording())
				replay.getRecorder().stop(save);
		}
	}

	public void playReplay(String name, Player watcher) {
		if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(watcher.getName())) {
			ReplaySaver.load(name, new Consumer<Replay>() {

				@Override
				public void accept(Replay replay) {
					replay.play(watcher);

				}
			});
		}
	}

	public void jumpToReplayTime(Player watcher, Integer second) {
		if (ReplayHelper.replaySessions.containsKey(watcher.getName())) {
			Replayer replayer = ReplayHelper.replaySessions.get(watcher.getName());
			if (replayer != null) {
				int duration = replayer.getReplay().getData().getDuration() / 20;
				if (second > 0 && second <= duration) {
					replayer.getUtils().jumpTo(second);
				}
			}
		}
	}

	public void registerReplaySaver(IReplaySaver replaySaver) {
		ReplaySaver.register(replaySaver);
	}

	public HookManager getHookManager() {
		return hookManager;
	}

	public static ReplayAPI getInstance() {
		if (instance == null)
			instance = new ReplayAPI();

		return instance;
	}
	
	public static HashMap<Integer, List<String>> gameIniReplays = new HashMap<>();
	public static HashMap<Integer, String> gameIniReplaysNames = new HashMap<>();

	public static void gameIni(Player p1, Player p2) {
		List<Player> playerList = new ArrayList<Player>();
		playerList.add(p1);
		playerList.add(p2);
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		Player[] players1 = new Player[playerList.size()];
		players1 = playerList.toArray(players1);
		int num = getInstance().getTotalReplays()+1;
		Bukkit.getServer().getScheduler().runTaskLater(ReplaySystem.getInstance(), new Runnable() {
			@Override
			public void run() {
				Player[] players = new Player[playerList.size()];
				players = playerList.toArray(players);
				if (Bukkit.getServerName().equals("Practice")) {
					ReplayAPI.getInstance().recordReplay("�4P_" + players[0].getName() + "-" + players[1].getName() + "-"
							+ String.valueOf(num), cs, players);
				} else if (Bukkit.getServerName().equals("ArmsRace")) {
					ReplayAPI.getInstance().recordReplay(Utils.chat("&6AR-" + players[0].getName() + "-" + players[1].getName() + "-")
							+ String.valueOf(num), cs, players);
				} else {
					return;
				}
				List<String> playerUUIDS = new ArrayList<>();
				playerUUIDS.add(players[0].getUniqueId().toString());
				playerUUIDS.add(players[1].getUniqueId().toString());
				gameIniReplays.put(num, playerUUIDS);
			}
		}, 20L);
		gameIniReplaysNames.put(num, "�6P-" + players1[0].getName() + "-" + players1[1].getName() + "-" + String.valueOf(num));
		}
	
	public static void stopGame(int INT) {
		if (gameIniReplays.containsKey(INT)) {
			String name = gameIniReplaysNames.get(INT);
			if (ReplayManager.activeReplays.containsKey(name) && ReplayManager.activeReplays.get(name).isRecording()) {
				Replay replay = ReplayManager.activeReplays.get(name);
				if (replay.getRecorder().getData().getActions().size() == 0) {
					replay.getRecorder().stop(false);
				} else {
					replay.getRecorder().stop(true);
				}
			} else {
				Bukkit.getLogger().warning("WARN: STOP KEY WAS NOT RECORDING/DID NOT EXIST! " + INT);
				return;
			}
		} else {
			Bukkit.getLogger().warning("WARN: STOP KEY WAS INCORRECT! " + INT);
			return;
		}
	}
}