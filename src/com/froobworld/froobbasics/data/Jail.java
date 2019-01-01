package com.froobworld.froobbasics.data;

import com.froobworld.frooblib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Jail {
    private File file;

    private String name;
    private Location location;
    private double radius;

    private UUID creator;
    private long created;

    public Jail(File file) {
        this.file = file;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.name = config.getString("name");
        this.location = LocationUtils.deserialiseLocation(config.getString("location"));
        this.radius = config.getDouble("radius");
    }

    public Jail(File file, String name, Player player, double radius) {
        this.file = file;
        this.name = name;
        this.location = player.getLocation();
        this.radius = radius;
        this.creator = player.getUniqueId();
        this.created = System.currentTimeMillis();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        config.set("location", LocationUtils.serialiseLocation(location));
        config.set("radius", radius);
        config.set("created", created);
        config.set("created-by", creator.toString());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getName() {

        return name;
    }

    public File getFile() {

        return file;
    }

    public Location getLocation() {

        return location;
    }

    public double getRadius() {

        return radius;
    }

    public boolean inJail(Location location) {
        if (location.getWorld() != getLocation().getWorld()) {

            return false;
        }

        return location.distance(getLocation()) <= radius;
    }

    @Override
    public String toString() {

        return name;
    }

}
