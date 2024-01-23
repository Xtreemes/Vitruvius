package org.xtreemes.vitruvius;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.xtreemes.vitruvius.commands.CreateCommand;
import org.xtreemes.vitruvius.events.LeaveEvent;
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;


public final class Vitruvius extends JavaPlugin {

    public static Logger LOGGER;
    public static Plugin PLUGIN;
    @Override
    public void onEnable() {
        // Plugin startup logic

        PLUGIN = this;
        LOGGER = this.getLogger();
        LOGGER.log(Level.INFO, "Vitruvius started");

        getCommand("create").setExecutor(new CreateCommand());

        registerListeners(
                new LeaveEvent()
        );

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SelectionHandler.resetSelections();
    }

    private static void registerListeners(Listener... listeners){
        for(Listener e : listeners){
            PLUGIN.getServer().getPluginManager().registerEvents(e, PLUGIN);
        }
    }
}
