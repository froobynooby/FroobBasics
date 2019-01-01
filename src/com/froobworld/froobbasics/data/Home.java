package com.froobworld.froobbasics.data;

import com.froobworld.frooblib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Home {
    private File file;

    private String name;
    private Location location;
    private long created;

    public Home(File file) {
        this.file = file;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        name = config.getString("name");
        location = LocationUtils.deserialiseLocation(config.getString("location"));
        created = config.getLong("created");
    }

    public Home(File file, String name, Location location) {
        this.file = file;
        this.name = name;
        this.location = location;
        this.created = System.currentTimeMillis();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        config.set("location", LocationUtils.serialiseLocation(location));
        config.set("created", created);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {

        return name;
    }

    public Location getLocation() {

        return location;
    }

    public File getFile() {

        return file;
    }

    @Override
    public String toString() {

        return name;
    }

}
