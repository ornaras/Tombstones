package ru.ornaras.tombstones;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    public boolean save(){
        try {
            var filename = String.format("%d_%d_%d.dat", location.getBlockX(), location.getBlockY(), location.getBlockZ());
            var filepath = new File(getTombstonesFolder(location.getWorld()), filename);
            var fileStream = new FileOutputStream(filepath);
            var gzipStream =  new GZIPOutputStream(fileStream);
            var out = new BukkitObjectOutputStream(gzipStream);
            out.writeObject(this);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(){
        try {
            var filename = String.format("%d_%d_%d.dat", location.getBlockX(), location.getBlockY(), location.getBlockZ());
            var filepath = new File(getTombstonesFolder(location.getWorld()), filename);
            return filepath.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Tombstone load(Location loc){
        try {
            var filename = String.format("%d_%d_%d.dat", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            var filepath = new File(getTombstonesFolder(loc.getWorld()), filename);
            var fileStream = new FileInputStream(filepath);
            var gzipStream =  new GZIPInputStream(fileStream);
            var in = new BukkitObjectInputStream(gzipStream);
            var result = (Tombstone)in.readObject();
            in.close();
            return result;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Tombstone> loadAll(World w){
        try {
            var filenames = getTombstonesFolder(w).list();
            var result = new ArrayList<Tombstone>();
            for (var filename : filenames) {
                var filepath = new File(getTombstonesFolder(w), filename);
                var fileStream = new FileInputStream(filepath);
                var gzipStream = new GZIPInputStream(fileStream);
                var in = new BukkitObjectInputStream(gzipStream);
                result.add((Tombstone) in.readObject());
                in.close();
            }
            return result;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    private static File getTombstonesFolder(World w) throws IOException {
        var dataPath = new File(w.getWorldFolder(), "data");
        if(!dataPath.exists())
            Files.createDirectory(Paths.get(dataPath.toURI()));
        var tombstonesPath = new File(dataPath, "tombstones");
        if(!tombstonesPath.exists())
            Files.createDirectory(Paths.get(tombstonesPath.toURI()));
        return tombstonesPath;
    }
}
