package ru.ornaras.tombstones;

import org.bukkit.Material;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {
    public static final Material TombstoneMaterial = Material.BARRIER;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(this),this);
        for(var world : getServer().getWorlds()){
            for(var tombstone : Tombstone.loadAll(world)){
                var block = tombstone.getLocation().getBlock();
                if (block.getType() == TombstoneMaterial){
                    var value = new FixedMetadataValue(this, tombstone.getOwnerId());
                    block.setMetadata("owner", value);
                }else{
                    tombstone.remove();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
