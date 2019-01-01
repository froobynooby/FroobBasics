package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.ContainerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;

public class MailCommand extends ContainerCommandExecutor {
	
	public MailCommand(PlayerManager playerManager, UUIDManager uuidManager) {
		super();
		addSubCommand(new Mail_ReadCommand(playerManager, uuidManager), 0, "read", "/mail read [page]");
		addSubCommand(new Mail_SendCommand(playerManager, uuidManager), 0, "send", "/mail send <player> <message>");
		addSubCommand(new Mail_ClearCommand(playerManager), 0, "clear", "/mail clear");
	}


	@Override
	public String command() {
		
		return "mail";
	}

	@Override
	public String perm() {

		return "froobbasics.mail";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			completions.add("read");
			completions.add("send");
			completions.add("clear");
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
