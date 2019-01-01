package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Spawn;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class SetspawnCommand extends PlayerCommandExecutor {
	
	private Spawn spawn;
	
	public SetspawnCommand(Spawn spawn) {
		this.spawn = spawn;
	}

	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		spawn.setSpawnLocation(player.getLocation());
		player.getWorld().setSpawnLocation(player.getLocation());
		player.sendMessage(ChatColor.YELLOW + "Spawn set.");
		return true;
	}

	@Override
	public String command() {
		
		return "setspawn";
	}

	@Override
	public String perm() {
		
		return "froobbasics.setspawn";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}
	
}
