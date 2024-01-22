package org.xtreemes.vitruvius;

import org.bukkit.plugin.java.JavaPlugin;

public final class Vitruvius extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getComponentLogger().debug("Vitruvius enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
