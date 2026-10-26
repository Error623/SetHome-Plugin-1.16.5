package ru.error623.sethome;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelhomeCommand implements CommandExecutor {
    private final HomeDatabase database;
    private final SetHome plugin;

    public DelhomeCommand(SetHome plugin, HomeDatabase database) {
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
            player.sendMessage(ChatColor.AQUA + plugin.getMessages().get("usage-delhome"));
            return true;
        }

        String delHomeName = args[0];

        String uuid = player.getUniqueId().toString();

        boolean deleted = database.delHome(uuid, delHomeName);

        if(deleted) {
            player.sendMessage(ChatColor.GREEN + plugin.getMessages().get("home-deleted") + ": " + delHomeName);
        } else {
            player.sendMessage(ChatColor.RED + plugin.getMessages().get("failed-to-delhome"));
        }
        return true;
    }
}
