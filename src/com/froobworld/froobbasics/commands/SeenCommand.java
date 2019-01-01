package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.utils.TimeUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class SeenCommand extends CommandExecutor {
	private PlayerManager playerManager;
	private UUIDManager uuidManager;
	
	public SeenCommand(PlayerManager playerManager, UUIDManager uuidManager) {
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
		
		if(Bukkit.getPlayer(data.getUUID()) != null) {
			sender.sendMessage(ChatColor.YELLOW + "That player is online now, silly.");
			return false;
		}
		sender.sendMessage(ChatColor.YELLOW + "That player was last seen " + 
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis()-data.getLastPlayed()) + " ago.");
		return true;
	}
	
	@Override
	public String command() {
		
		return "seen";
	}

	@Override
	public String perm() {
		
		return "froobbasics.seen";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			
			return CommandUtils.tabCompletePlayerList(args[0], true, false, uuidManager);
		}
			
		return completions;
	}
	
}
