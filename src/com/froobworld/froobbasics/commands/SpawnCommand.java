package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Spawn;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TeleportUtils;

public class SpawnCommand extends PlayerCommandExecutor {
	private Spawn spawn;
	private PlayerManager playerManager;
	
	public SpawnCommand(Spawn spawn, PlayerManager playerManager) {
		this.spawn = spawn;
		this.playerManager = playerManager;
	}
	

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		playerManager.getPlayerdata(player).setBackLocation(player.getLocation(), false);
		player.teleport(TeleportUtils.findSafeTeleportLocation(spawn.getSpawnLocation()));
		return true;
	}


	@Override
	public String command() {
		
		return "spawn";
	}

	@Override
	public String perm() {
		
		return "froobbasics.spawn";
	}
	
	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}

}
