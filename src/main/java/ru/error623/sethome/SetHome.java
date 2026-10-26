package ru.error623.sethome;

import org.bukkit.plugin.java.JavaPlugin;

public final class SetHome extends JavaPlugin {

    private HomeDatabase database;
    private Messages messages;

    public HomeDatabase getDatabase() {
        return database;
    }

    public Messages getMessages() {
        return messages;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        messages = new Messages(this);

        database = new HomeDatabase(this);
        database.open();

        getLogger().info("SetHome is enabled");
        getServer().getPluginManager().registerEvents(new HomesInventoryListener(this), this);

        getCommand("sethome").setExecutor(new SethomeCommand(this,database));
        getCommand("delhome").setExecutor(new DelhomeCommand(this,database));
        getCommand("homes").setExecutor(new HomesCommand(this, database));


    }

    @Override
    public void onDisable() {

        if(database != null) {
            database.closeDb();
        }

        getLogger().info("SetHome is disabled");

    }
}
