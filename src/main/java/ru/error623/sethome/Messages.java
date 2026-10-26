package ru.error623.sethome;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Messages {
    private final YamlConfiguration messages;

    public Messages(JavaPlugin plugin) {
        for(String lang : new String[]{"ru", "en"}) {
            String fileName = "messages_" + lang + ".yml";
            File file = new File(plugin.getDataFolder(), fileName);

            if(!file.exists()) {
                plugin.saveResource(fileName, false);
            }
        }

        String language = plugin.getConfig().getString("language", "ru");
        File file = new File(plugin.getDataFolder(), "messages_" + language + ".yml");

        messages = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String key) {
        return messages.getString(key, key);
    }

}
