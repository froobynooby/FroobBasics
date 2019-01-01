package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.ContainerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;

public class IgnoreCommand extends ContainerCommandExecutor {
	
	public IgnoreCommand(PlayerManager playerManager, UUIDManager uuidManager) {
		this.addSubCommand(new Ignore_AddCommand(playerManager, uuidManager), 0, "add", "/ignore add <player>");
		this.addSubCommand(new Ignore_RemoveCommand(playerManager, uuidManager), 0, "remove", "/ignore remove <player>");
		this.addSubCommand(new Ignore_ListCommand(playerManager, uuidManager), 0, "list", "/ignore list");
	}

	@Override
	public String command() {
		
		return "ignore";
	}

	@Override
	public String perm() {
		
		return "froobbasics.ignore";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			completions.add("list");
			completions.add("add");
			completions.add("remove");
			completions = StringUtil.copyPartialMatches(args[0], completions, new ArrayList<String>(completions.size()));
			
			return completions;
		}else {
			for(SubCommand subCommand : getSubCommands()) {
				if(args.length > subCommand.getArgIndex() + 1) {
					if(subCommand.getArg().equalsIgnoreCase(args[subCommand.getArgIndex()])) {
						return subCommand.getExecutor().tabCompletions(sender, command, cl, args);
					}
				}
			}
		}
			
		return completions;
	}

}
