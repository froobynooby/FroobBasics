package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

public class Friend_TogglechatCommand extends PlayerCommandExecutor{
	private PlayerManager playerManager;
	
	public Friend_TogglechatCommand(PlayerManager playerManager){
		this.playerManager = playerManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		Playerdata data = playerManager.getPlayerdata(player);
		
		data.toggleFriendsOnlyChat();
		if(data.isFriendsOnlyChat()){
			player.sendMessage(ChatColor.YELLOW + "Friends only chat toggled. Your chat messages will now only go to your friends.");
		}else{
			player.sendMessage(ChatColor.YELLOW + "Friends only chat detoggled. Your chat messages will now be public.");
		}
		return true;
	}

	@Override
	public String command() {
		
		return "friend togglechat";
	}

	@Override
	public String perm() {
		
		return "froobbasics.friend";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length  == 1) {
			completions.add("togglechat");
		}
			
		return completions;
	}
	
	
}
