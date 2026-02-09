package ru.ornaras.tombstones;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Predicate;

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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        var b = e.getBlock();
        if (b.getType() != Plugin.TombstoneMaterial) return;
        Predicate<? super MetadataValue> filter = v -> v.getOwningPlugin() == main;
        var owner = b.getMetadata("owner").stream().filter(filter);
        if (owner.findAny().isEmpty()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onTombstoneInteract(PlayerInteractEvent e){
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        var b = e.getClickedBlock();
        if (b == null) return;
        if (b.getType() != Plugin.TombstoneMaterial) return;
        Predicate<? super MetadataValue> filter = v -> v.getOwningPlugin() == main;
        var owner = b.getMetadata("owner").stream().filter(filter).toList();
        if (owner.isEmpty()) return;
        var player = e.getPlayer();
        if (!player.getUniqueId().equals(owner.get(0).value())) return;
        var tombstone = Tombstone.load(b.getLocation());
        b.setType(Material.AIR);
        b.removeMetadata("owner", main);
        assert tombstone != null;
        for (var drop : tombstone.getDrops())
            player.getInventory().addItem(drop);
        tombstone.remove();
    }
}
