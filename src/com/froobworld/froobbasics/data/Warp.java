package com.froobworld.froobbasics.data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.froobworld.frooblib.utils.LocationUtils;

public class Warp {
	private File file;
	
	private String name;
	private Location location;
	private long created;
	private UUID creator;

	public Warp(File file){
		this.file = file;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		name = config.getString("name");
		location = LocationUtils.deserialiseLocation(config.getString("location"));
		created = config.getLong("created");
		creator = UUID.fromString(config.getString("created-by"));
	}
	
	public Warp(File file, String name, Location location, UUID creator) {
		this.file = file;
		this.name = name;
		this.location = location;
		this.created = System.currentTimeMillis();
		this.creator = creator;
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("name", name);
		config.set("location", LocationUtils.serialiseLocation(location));
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
	
	public Location getLocation() {
		
		return location;
	}
	
	public File getFile() {
		
		return file;
	}
	
	public UUID getCreator() {
		
		return creator;
	}
	
	@Override
	public String toString() {
		
		return name;
	}

}
