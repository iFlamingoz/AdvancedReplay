package me.jumper251.replay.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.jumper251.replay.inventories.inventories;
import me.jumper251.replay.replaysystem.utils.Utils;

public class InventoryClickListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equals(Utils.chat("&6Replay"))) {
			if (e.getCurrentItem() == null) {
				e.getWhoClicked().closeInventory();
				return;
			}
			inventories.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), "replay");
			e.setCancelled(true);
		}
	}
}