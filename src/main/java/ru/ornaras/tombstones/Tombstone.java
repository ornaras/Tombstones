package ru.ornaras.tombstones;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class Tombstone implements Serializable {
    private final UUID ownerId;
    private final Location location;
    private final List<ItemStack> drops;

    public Tombstone(Player player, List<ItemStack> drops){
        ownerId = player.getUniqueId();
        location = player.getLocation().clone();
        if(location.getBlockY() < -64) location.setY(-64);
        this.drops = drops;
    }

    public UUID getOwnerId() { return ownerId; }
    public Location getLocation() { return location; }
    public List<ItemStack> getDrops() { return drops; }
}
