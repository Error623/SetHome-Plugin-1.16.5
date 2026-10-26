package ru.error623.sethome;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HomesInventoryListener implements Listener {
    private final SetHome plugin;

    public HomesInventoryListener(SetHome plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals(ChatColor.DARK_GREEN + plugin.getMessages().get("gui-title"))) {
            return;
        }
        event.setCancelled(true);

        if(event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        if(event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) {
            return;
        }

        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        ItemMeta meta = event.getCurrentItem().getItemMeta();

        if(meta == null) {
            return;
        }

        NamespacedKey key = new NamespacedKey(plugin, "home_name");

        String homeName = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if(homeName == null) {
            return;
        }

        HomeData home = plugin.getDatabase().getHome(
                player.getUniqueId().toString(),
                homeName
        );

        if(home == null) {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("home-not-found"));
            return;
        }

        World world = Bukkit.getWorld(home.getWorldName());

        if(world == null) {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("world-not-found"));
            return;
        }

        Location location = new Location(
                world,
                home.getX(),
                home.getY(),
                home.getZ()
        );

        player.closeInventory();

        if(player.teleport(location)) {
            player.sendMessage(ChatColor.GREEN + plugin.getMessages().get("teleport-success"));
        } else {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("failed-to-teleported"));
        }

        player.sendMessage(ChatColor.GREEN + plugin.getMessages().get("select-homes")  + ": " + homeName);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(event.getView().getTitle().equals(ChatColor.DARK_GREEN + plugin.getMessages().get("gui-title"))) {
            event.setCancelled(true);
        }
    }
}
