package ru.error623.sethome;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class SethomeCommand implements CommandExecutor {
    private final HomeDatabase database;
    private final SetHome plugin;

    public SethomeCommand(SetHome plugin,HomeDatabase database) {
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

        if(args.length == 0) {
            player.sendMessage(ChatColor.GREEN + plugin.getMessages().get("usage-sethome"));
            return true;
        }
        String homeName = args[0];

        if(homeName == null || homeName.isBlank()) {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("invalid-home-name"));
            return true;
        }

        Location location = player.getLocation();

        String playerName = player.getName();
        String playerUuid = player.getUniqueId().toString();
        String worldName = location.getWorld().getName();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        // Вызывается метод и передаёт необходимые параметры
        boolean saved = database.saveHome(playerName, playerUuid, homeName, worldName, x, y, z);


        if(saved) {
            player.sendMessage(ChatColor.GREEN + plugin.getMessages().get("home-created") + ": " + homeName);
        } else {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("failed-to-crate-home"));
        }

        return true;
    }
}
