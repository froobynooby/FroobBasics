package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.managers.AfkManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class MotdCommand extends PlayerCommandExecutor {
	private PlayerManager playerManager;
	private AfkManager afkManager;
	
	public MotdCommand(PlayerManager playerManager, AfkManager afkManager) {
		this.playerManager = playerManager;
		this.afkManager = afkManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		for(String string : Config.getMotd()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', string)
					.replaceAll("%PLAYER%", player.getDisplayName())
					.replaceAll("%PLAYERLIST%", playerManager.getPlayerList(player, afkManager)));
		}
		return true;
	}



	@Override
	public String command() {
		
		return "motd";
	}

	@Override
	public String perm() {
		
		return "froobbasics.motd";
	}
	
	@Override
	public List<String> tabCompletions(Player arg0, Command arg1, String arg2, String[] arg3) {
		ArrayList<String> completions = new ArrayList<String>();
		
		return completions;
	}
}
