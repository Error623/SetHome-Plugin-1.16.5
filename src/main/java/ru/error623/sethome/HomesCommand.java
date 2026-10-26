package ru.error623.sethome;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomesCommand implements CommandExecutor {
    private final HomeDatabase database;
    private final SetHome plugin;

    public HomesCommand(SetHome plugin, HomeDatabase database) {
        this.plugin = plugin;
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + plugin.getMessages().get("player-only"));
            return true;
        }

        Player player = (Player) sender;

        List<HomeData> homes = database.getHomes(player.getUniqueId().toString());

        Inventory inventory = Bukkit.createInventory(
                null,
                9,
                ChatColor.DARK_GREEN + plugin.getMessages().get("gui-title")
        );

        for(HomeData home : homes) {
            ItemStack bed = new ItemStack(Material.RED_BED);

            ItemMeta meta = bed.getItemMeta();

            NamespacedKey key = new NamespacedKey(plugin, "home_name");
            meta.getPersistentDataContainer().set(
                    key,
                    PersistentDataType.STRING,
                    home.getHomeName()
            );

            meta.setDisplayName(ChatColor.GREEN + home.getHomeName());
            meta.setLore(Arrays.asList(
                    ChatColor.LIGHT_PURPLE + plugin.getMessages().get("home-name") + ": " + home.getHomeName(),
                    ChatColor.AQUA + plugin.getMessages().get("home-world") + ": " + home.getWorldName(),
                    ChatColor.GOLD + plugin.getMessages().get("home-x") + ": " + String.format(Locale.US, "%.2f", home.getX()),
                    ChatColor.GOLD + plugin.getMessages().get("home-y") + ": " + String.format(Locale.US, "%.2f", home.getY()),
                    ChatColor.GOLD + plugin.getMessages().get("home-z")  + ": "+ String.format(Locale.US, "%.2f", home.getZ())
            ));

            bed.setItemMeta(meta);

            inventory.addItem(bed);
        }

        player.openInventory(inventory);

        return true;
    }
}
