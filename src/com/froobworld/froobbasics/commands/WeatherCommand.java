package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.froobworld.frooblib.command.CommandExecutor;

public class WeatherCommand extends CommandExecutor{

	private static final HashMap<String, Boolean> weather = new HashMap<String, Boolean>();
	static{
		weather.put("storm", true);
		weather.put("stormy", true);
		weather.put("rain", true);
		weather.put("rainy", true);
		weather.put("clear", false);
		weather.put("sunny", false);
		weather.put("sun", false);
	}
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		World world = Bukkit.getWorlds().get(0);
		if(sender instanceof Player) {
			world = ((Player) sender).getWorld();
		}
		String type = args[0].toLowerCase();
		if(!weather.containsKey(type)){
			sender.sendMessage(ChatColor.RED + "That weather type cannot be parsed.");
			return false;
		}
		
		world.setStorm(weather.get(type));
		if(weather.get(type)){
			sender.sendMessage(ChatColor.YELLOW + "Weather set to storm in world " + world.getName() + ".");
		}else{
			sender.sendMessage(ChatColor.YELLOW + "Weather set to sun in world " + world.getName() + ".");
		}
		return true;
	}
	
	
	@Override
	public String command() {
		
		return "weather";
	}

	@Override
	public String perm() {
		
		return "froobbasics.weather";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			completions.add("storm");
			completions.add("sun");
			completions = StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
		}
		
		return completions;
	}

}
