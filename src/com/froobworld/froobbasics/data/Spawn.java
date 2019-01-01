package com.froobworld.froobbasics.data;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.frooblib.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Spawn {
    private File file;

    private Location location;

    public Spawn() {
        file = new File(FroobBasics.getPlugin().getDataFolder(), "spawn.yml");
        load();
    }


    public void load() {
        if (!file.exists()) {
            createDefault();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        location = LocationUtils.deserialiseLocation(config.getString("location"));
    }

    public void createDefault() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration config = new YamlConfiguration();
        config.set("location", LocationUtils.serialiseLocation(Bukkit.getWorlds().get(0).getSpawnLocation()));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getSpawnLocation() {

        return location;
    }

    public void setSpawnLocation(Location location) {
        YamlConfiguration config = new YamlConfiguration();
        this.location = location;
        config.set("location", LocationUtils.serialiseLocation(location));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
