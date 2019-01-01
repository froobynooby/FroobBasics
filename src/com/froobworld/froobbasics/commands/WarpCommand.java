package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Warp;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.WarpManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TeleportUtils;

import net.md_5.bungee.api.ChatColor;

public class WarpCommand extends PlayerCommandExecutor {
	private WarpManager warpManager;
	private PlayerManager playerManager;
	
	public WarpCommand(WarpManager warpManager, PlayerManager playerManager) {
		this.warpManager = warpManager;
		this.playerManager = playerManager;
	}
	
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length == 0) {
			player.sendMessage("/" + cl + " <name>");
			return false;
		}
		String name = StringUtils.join(args, " ");
		Warp warp = warpManager.getWarp(name);
		
		if(warp == null) {
			player.sendMessage(ChatColor.RED + "A warp by that name does not exist.");
			return false;
		}
		Playerdata data = playerManager.getPlayerdata(player);
		data.setBackLocation(player.getLocation(), false);
		
		player.teleport(TeleportUtils.findSafeTeleportLocation(warp.getLocation()));
		player.sendMessage(ChatColor.YELLOW + "Whoosh!");
		return true;
	}

	@Override
	public String command() {
		
		return "warp";
	}

	@Override
	public String perm() {
		
		return "froobbasics.warp";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		for(Warp warp : warpManager.getWarps()) {
			String[] split = warp.getName().split(" ");
			if(split.length >= args.length) {
				if(warp.getName().toLowerCase().startsWith(StringUtils.join(args, " ").toLowerCase())){
					completions.add(StringUtils.join(Arrays.copyOfRange(split, args.length-1, split.length), " "));
				}
			}
		}
		completions = StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
			
		return completions;
	}

}
