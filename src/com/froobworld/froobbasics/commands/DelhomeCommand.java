package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.data.Home;
import com.froobworld.froobbasics.managers.HomeManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class DelhomeCommand extends PlayerCommandExecutor {
	private HomeManager homeManager;
	
	public DelhomeCommand(HomeManager homeManager) {
		this.homeManager = homeManager;
	}
	

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		String name = "default";
		if(args.length > 0) {
			name = StringUtils.join(args, " ");
		}
		
		Home home = homeManager.getHome(player, name);
		if(home == null) {
			player.sendMessage(ChatColor.RED + "You don't have a home by that name.");
			return false;
		}
		
		homeManager.delHome(player, name);
		player.sendMessage(ChatColor.YELLOW + "Home deleted.");
		return true;
	}

	@Override
	public String command() {
		
		return "delhome";
	}

	@Override
	public String perm() {
		
		return "froobbasics.delhome";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		for(Home home : homeManager.getHomes(player)) {
			String[] split = home.getName().split(" ");
			if(split.length >= args.length) {
				if(home.getName().toLowerCase().startsWith(StringUtils.join(args, " ").toLowerCase())){
					completions.add(StringUtils.join(Arrays.copyOfRange(split, args.length-1, split.length), " "));
				}
			}
		}
		completions = StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
			
		return completions;
	}

}
