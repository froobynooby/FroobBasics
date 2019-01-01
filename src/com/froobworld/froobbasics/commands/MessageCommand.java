package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.managers.MessageManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;

import net.md_5.bungee.api.ChatColor;

public class MessageCommand extends PlayerCommandExecutor {
	private MessageManager messageManager;
	private PlayerManager playerManager;
	private PunishmentManager punishmentManager;
	
	public MessageCommand(MessageManager messageManager, PlayerManager playerManager, PunishmentManager punishmentManager) {
		this.messageManager = messageManager;
		this.playerManager = playerManager;
		this.punishmentManager = punishmentManager;
	}
	

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length < 2) {
			player.sendMessage("/" + cl + " <player> <message>");
			return false;
		}
		Player receiver = Bukkit.getPlayer(args[0]);
		if(receiver == null) {
			player.sendMessage(ChatColor.RED + "Player not found.");
			return false;
		}
		String message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
		
		messageManager.sendMessage(receiver, player, message, playerManager, punishmentManager);
		return true;
	}

	@Override
	public String command() {
		
		return "message";
	}

	@Override
	public String perm() {
		
		return "froobbasics.message";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			return CommandUtils.tabCompletePlayerList(args[0], false, false, null);
		}else {
			return completions;
		}
	}

}
;