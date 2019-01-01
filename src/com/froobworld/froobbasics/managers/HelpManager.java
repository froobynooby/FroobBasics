package com.froobworld.froobbasics.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.froobworld.froobbasics.data.HelpObject;
import com.froobworld.frooblib.data.Manager;

public class HelpManager extends Manager {
	
	private ArrayList<HelpObject> helpObjects;

	@Override
	public void ini() {

	}
	
	public void load() {
		helpObjects = new ArrayList<HelpObject>();
		
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			for(Command command : PluginCommandYamlParser.parse(plugin)){
				PluginCommand cmd = Bukkit.getPluginCommand(command.getLabel());
				if(cmd == null) {
					continue;
				}
				helpObjects.add(new HelpObject(plugin, cmd));
			}
		}
		
		orderHelpObjects();
	}
	
	public void orderHelpObjects() {
		Collections.sort(helpObjects, new Comparator<HelpObject>() {
		    @Override
		    public int compare(HelpObject ho1, HelpObject ho2) {
		        return ho1.toString().compareToIgnoreCase(ho2.toString());
		    }
		});
	}
	
	public ArrayList<HelpObject> getHelpObjects(Player player){
		if(helpObjects == null) {
			load();
		}
		
		ArrayList<HelpObject> objects = new ArrayList<HelpObject>();
		for(HelpObject object : helpObjects) {
			if(object.getCommand().getPermission() != null && 
					player.hasPermission(object.getCommand().getPermission())) {
				objects.add(object);
			}
		}
		
		return objects;
	}
	
	public ArrayList<HelpObject> filterHelpObjects(ArrayList<HelpObject> objects, String arg){
		if(arg == null) {
			return objects;
		}
		ArrayList<HelpObject> filtered = new ArrayList<HelpObject>();
		for(HelpObject object : objects) {
			if(object.getCommand().getLabel().toLowerCase().contains(arg.toLowerCase()) ||
					object.getCommand().getDescription().toLowerCase().contains(arg.toLowerCase()) ||
					object.getPlugin().getName().toLowerCase().contains(arg.toLowerCase())) {
				filtered.add(object);
			}
		}
		
		return filtered;
	}

}
