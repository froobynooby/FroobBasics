package com.froobworld.froobbasics.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Portal;
import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.data.TaskManager;

import net.md_5.bungee.api.ChatColor;

public class PortalManager extends TaskManager{
	private Storage storage; 
	
	private ArrayList<Portal> portals;
	
	private HashMap<Player, Teleport> pending = new HashMap<Player, Teleport>();
	private HashMap<Player, Boolean> cooldown = new HashMap<Player, Boolean>();
	
	public PortalManager(){
		super(FroobBasics.getPlugin());
	}
	

	@Override
	public void ini() {
		storage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/portals");
		load();
		addLinks();
		
		addTask(0, 10, new Runnable() {

			@Override
			public void run() {
				task();
			}
		});
	}
	
	private void load(){
		portals = new ArrayList<Portal>();
		for(File file : storage.listFiles()){
			Portal portal = new Portal(file);
			portals.add(portal);
		}
	}
	
	private void addLinks(){
		for(Portal portal : portals){
			portal.addLink(portals);
		}
	}
	
	public void createPortal(Player player, String portalName, double radius){
		Portal portal = new Portal(storage.getFile(portalName + ".yml"), player, portalName, radius);
		portals.add(portal);
	}
	
	public void deletePortal(String portalName){
		Portal del = null;
		for(Portal portal : portals){
			if(portal.getName().equalsIgnoreCase(portalName)){
				del = portal;
			}
		}
		if(del == null){
			return;
		}
		
		if(del.getLink() != null){
			del.getLink().setLink(null);
		}
		del.getFile().delete();
		portals.remove(del);
	}
	
	public ArrayList<Portal> getPortals(){
		
		return portals;
	}
	
	public Portal getPortal(String name){
		for(Portal portal : portals){
			if(portal.getName().equalsIgnoreCase(name)){
				return portal;
			}
		}
		
		return null;
	}

	public ArrayList<String> listPortals(){
		ArrayList<String> list = new ArrayList<String>();
		for(Portal portal : portals){
			list.add(portal.getName());
		}
		
		return list;
	}
	
	private void task(){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(cooldown.get(player) == null){
				cooldown.put(player, false);
			}
			boolean ticked = false;
			boolean cool = cooldown.get(player);
			boolean stillOn = false;
			for(Portal portal : portals){
				if(portal.getLink() == null){
					continue;
				}
				if(portal.getLocation().getWorld().equals(player.getWorld())){
					if(portal.getLocation().distance(player.getLocation()) <= portal.getRadius()){
						if(cool){
							stillOn = true;
							break;
						}
						Teleport cur = pending.get(player);
						if(cur != null && cur.getPortal() == portal){
							if(cur.getTimeTill() == 0){
								player.teleport(cur.getPortal().getLink().getLocation());
								pending.remove(player);
								player.sendMessage(ChatColor.YELLOW + "Beam me up, Scotty.");
								cooldown.put(player, true);
								stillOn = true;
							}else{
								cur.tick();
								ticked = true;
							}
							break;
						}else{
							ticked = true;
							pending.put(player, new Teleport(portal, 10));
							player.sendMessage(ChatColor.YELLOW + "You are standing on a portal. "
									+ "Teleporting in 5 seconds.");
							break;
						}
					}
				}
			}
			if(!ticked){
				pending.remove(player);
			}
			if(stillOn){
				continue;
			}
			cooldown.put(player, false);
		}
	}
	
	private class Teleport{
		private Portal portal;
		private int timeTill;
		
		public Teleport(Portal portal, int timeTill){
			this.portal = portal;
			this.timeTill = timeTill;
		}
		
		public Portal getPortal(){
			
			return portal;
		}
		
		public int getTimeTill(){
			
			return timeTill;
		}
		
		public void tick(){
			timeTill = timeTill-1;
		}
		
	}

}
