package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class InfoCommand extends CommandExecutor{
	private PlayerManager playerManager;
	private UUIDManager uuidManager;
	
	public InfoCommand(PlayerManager playerManager, UUIDManager uuidManager) {
		this.playerManager = playerManager;
		this.uuidManager = uuidManager;
	}

	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("/" + cl + " <player>");
			return false;
		}
		Playerdata data = playerManager.commandSearchForPlayer(args[0], sender, uuidManager);
		if(data == null) {
			return false;
		}
		sender.sendMessage(ChatColor.YELLOW + "Information for " + uuidManager.getUUIDData(data.getUUID()).getLastName() + ":");
		sender.sendMessage(ChatColor.YELLOW + "UUID: " + ChatColor.WHITE + data.getUUID().toString());
		sender.sendMessage(ChatColor.YELLOW + "Last IP: " + ChatColor.WHITE + data.getLastIp());
		return true;
	}
	
	@Override
	public String command() {
		
		return "info";
	}

	@Override
	public String perm() {
		
		return "froobbasics.info";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			return null;
		}
		
		return completions;
	}

}
