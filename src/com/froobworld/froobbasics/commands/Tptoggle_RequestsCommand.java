package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class Tptoggle_RequestsCommand extends PlayerCommandExecutor {
	private PlayerManager playerManager;
	
	public Tptoggle_RequestsCommand(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		Playerdata data = playerManager.getPlayerdata(player);
		data.toggleTeleportRequests();
		
		if(data.isTeleportRequestsDisabled()) {
			player.sendMessage(ChatColor.YELLOW + "Players will no longer be able to send you teleport requests.");
		}else {
			player.sendMessage(ChatColor.YELLOW + "Players will now be able to send you teleport requests.");
		}
		return true;
	}

	@Override
	public List<String> tabCompletions(Player arg0, Command arg1, String arg2, String[] arg3) {
		ArrayList<String> completions = new ArrayList<String>();
		
		return completions;
	}

	@Override
	public String command() {
		
		return "tptoggle requests";
	}

	@Override
	public String perm() {
		
		return "froobbasics.tptoggle";
	}

}
