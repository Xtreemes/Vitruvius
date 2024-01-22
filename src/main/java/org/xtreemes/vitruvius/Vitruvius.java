package org.xtreemes.vitruvius;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Vitruvius extends JavaPlugin {

    public static ComponentLogger LOGGER;
    public static Plugin PLUGIN;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getComponentLogger().debug("Vitruvius enabled!");
        PLUGIN = this;
        LOGGER = this.getComponentLogger();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
