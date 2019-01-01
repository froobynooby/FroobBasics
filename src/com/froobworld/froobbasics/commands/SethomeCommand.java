package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.data.Home;
import com.froobworld.froobbasics.managers.HomeManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

import net.md_5.bungee.api.ChatColor;

public class SethomeCommand extends PlayerCommandExecutor {
	private HomeManager homeManager;
	
	public SethomeCommand(HomeManager homeManager) {
		this.homeManager = homeManager;
	}
	

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		String name = "default";
		int maxHomes = Config.getMaxHomes(player);
		boolean bypass = false;
		if(args.length > 0) {
			name = StringUtils.join(args, " ");
		}
		
		Home home = homeManager.getHome((Player) player, name);
		if(home != null) {
			bypass = true;
		}
		
		if(!bypass && homeManager.getHomes(player).size() >= maxHomes) {
			player.sendMessage(ChatColor.RED + "You can only set " + (maxHomes == 1 
					? "one home":(maxHomes + " homes.")) );
			return false;
		}
		
		homeManager.setHome(player, name);
		player.sendMessage(ChatColor.YELLOW + "Home set.");
		return false;
	}

	@Override
	public String command() {
		
		return "sethome";
	}

	@Override
	public String perm() {
		
		return "froobbasics.sethome";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}

}
