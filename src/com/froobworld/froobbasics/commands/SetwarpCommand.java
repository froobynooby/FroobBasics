package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Warp;
import com.froobworld.froobbasics.managers.WarpManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class SetwarpCommand extends PlayerCommandExecutor {
	private WarpManager warpManager;
	
	public SetwarpCommand(WarpManager warpManager) {
		this.warpManager = warpManager;
	}
	
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length == 0) {
			player.sendMessage("/" + cl + " <name>");
			return false;
		}
		String name = StringUtils.join(args, " ");
		Warp warp = warpManager.getWarp(name);
		
		if(warp != null) {
			player.sendMessage(ChatColor.RED + "A warp by that name already exists.");
			return false;
		}
		
		warpManager.createWarp(name, player);
		player.sendMessage(ChatColor.YELLOW + "Warp created.");
		return true;
	}

	@Override
	public String command() {
		
		return "setwarp";
	}

	@Override
	public String perm() {
		
		return "froobbasics.setwarp";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}

}
