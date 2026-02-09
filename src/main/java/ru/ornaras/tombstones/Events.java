package ru.ornaras.tombstones;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Events implements Listener {
    private final JavaPlugin main;

    public Events(JavaPlugin main){
        this.main = main;
    }
}
