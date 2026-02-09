package ru.ornaras.tombstones;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Events implements Listener {
    private final JavaPlugin main;

    public Events(JavaPlugin main){
        this.main = main;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        var player = e.getEntity();
        var drops = e.getDrops();
        if(drops.isEmpty()) return;
        var tombstone = new Tombstone(player, drops);
        var block = tombstone.getLocation().getBlock();
        block.setType(Plugin.TombstoneMaterial);
        block.setMetadata("owner", new FixedMetadataValue(main, tombstone.getOwnerId()));
        tombstone.save();
        drops.clear();
    }
}
