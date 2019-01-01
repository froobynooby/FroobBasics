package com.froobworld.froobbasics.data;

import com.froobworld.frooblib.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Portal {
    private File file;
    private String name;
    private Location location;
    private double radius;
    private Portal link;
    private String linkName;
    private UUID creator;
    private long created;

    public Portal(File file) {
        this.file = file;
        load();
    }

    public Portal(File file, Player player, String name, double radius) {
        this.file = file;
        this.name = name;
        this.location = player.getLocation();
        this.radius = radius;
        this.creator = player.getUniqueId();
        this.created = System.currentTimeMillis();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("name", name);
        config.set("location.location", LocationUtils.serialiseLocation(location));
        config.set("location.radius", radius);
        config.set("creator", creator.toString());
        config.set("created", created);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        name = config.getString("name");
        location = LocationUtils.deserialiseLocation(config.getString("location.location"));
        radius = config.getDouble("location.radius");
        linkName = config.getString("link");
        creator = UUID.fromString(config.getString("creator"));
        created = config.getLong("created");
    }

    public File getFile() {

        return file;
    }

    public String getName() {

        return name;
    }

    public Location getLocation() {

        return location;
    }

    public double getRadius() {

        return radius;
    }

    public Portal getLink() {
        return link;
    }

    public void setLink(Portal portal) {
        link = portal;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (portal != null) {
            config.set("link", portal.getName());
        } else {
            config.set("link", null);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getCreator() {

        return creator;
    }

    public long getTimeCreated() {

        return created;
    }

    @Override
    public String toString() {

        return name;
    }

    public void addLink(ArrayList<Portal> portals) {
        for (Portal portal : portals) {
            if (portal.getName().equalsIgnoreCase(linkName)) {
                link = portal;
                return;
            }
        }
    }

}
