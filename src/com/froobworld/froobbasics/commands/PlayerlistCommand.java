package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.froobbasics.managers.AfkManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.CommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class PlayerlistCommand extends CommandExecutor {
	private PlayerManager playerManager;
	private AfkManager afkManager;
	
	public PlayerlistCommand(PlayerManager playerManager, AfkManager afkManager) {
		this.playerManager = playerManager;
		this.afkManager = afkManager;
	}
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		int players = Bukkit.getOnlinePlayers().size();
		if(players == 0) {
			sender.sendMessage(ChatColor.YELLOW + "There are no players online.");
			return true;
		}
		
		sender.sendMessage(ChatColor.YELLOW + "There " + (players == 1 ? "is one player ":
			("are " + players + " players ")) + "online.");
		sender.sendMessage(playerManager.getPlayerList(sender, afkManager));
		
		return true;
	}
	
	
	@Override
	public String command() {
		
		return "playerlist";
	}

	@Override
	public String perm() {
		
		return "froobbasics.playerlist";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}

}
