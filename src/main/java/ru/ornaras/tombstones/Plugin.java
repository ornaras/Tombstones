package ru.ornaras.tombstones;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {
    public static final Material TombstoneMaterial = Material.BARRIER;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
