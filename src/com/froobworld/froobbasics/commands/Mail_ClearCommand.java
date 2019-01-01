package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class Mail_ClearCommand extends PlayerCommandExecutor {
	private PlayerManager playerManager;

	public Mail_ClearCommand(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		Playerdata data = playerManager.getPlayerdata(player);
		if(data.getMail().size() == 0) {
			player.sendMessage(ChatColor.YELLOW + "You have no messages to clear.");
			return false;
		}
		
		data.clearMail();
		player.sendMessage(ChatColor.YELLOW + "Mail cleared.");
		return true;
	}


	@Override
	public String command() {
		
		return "mail clear";
	}


	@Override
	public String perm() {
		
		return "froobbasics.mail";
	}


	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length  == 1) {
			completions.add("clear");
		}
		
		return completions;
	}

}
