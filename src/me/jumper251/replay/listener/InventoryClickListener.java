package me.jumper251.replay.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.jumper251.replay.inventories.inventories;
import me.jumper251.replay.replaysystem.utils.Utils;

public class InventoryClickListener implements Listener {
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().startsWith(Utils.chat("&6Replay"))) {
			if (e.getCurrentItem() == null) {
				e.getWhoClicked().closeInventory();
				return;
			} else if (e.getCurrentItem().getType().equals(Material.AIR)) {
				return;
			}
			if (e.getInventory().getTitle().equals(Utils.chat("&6Replay"))) {
				inventories.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), "replay", 0);
			} else {
				String[] pagenumsplit = e.getClickedInventory().getTitle().split(" ");
				int pagenum = Integer.parseInt(pagenumsplit[1]);
				inventories.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), "replay", pagenum);	
			}
			e.setCancelled(true);
		}
	}
}