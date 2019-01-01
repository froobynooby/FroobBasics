package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.Mediator;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.utils.TimeUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class MuteCommand extends CommandExecutor {
	private PunishmentManager punishmentManager;
	private UUIDManager uuidManager;
	
	public MuteCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
		this.punishmentManager = punishmentManager;
		this.uuidManager = uuidManager;
	}

	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		if(args.length < 2) {
			sender.sendMessage("/" + cl + " <player> <duration> [reason]");
			return false;
		}
		Punishment punishment = punishmentManager.commandSearchForPlayer(args[0], sender, uuidManager);
		if(punishment == null) {
			return false;
		}
		if(punishment.isMuted()) {
			sender.sendMessage(ChatColor.RED + "That player is already muted.");
			return false;
		}
		Long duration = TimeUtils.parseTime(args[1]);
		if(duration == null) {
			sender.sendMessage(Messages.TIME_PARSE_EXCEPTION);
			return false;
		}
		String reason = "breaking a rule";
		if(args.length > 2) {
			reason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
		}
		Mediator mediator = (sender instanceof Player) ? new Mediator(((Player) sender).getUniqueId()):new Mediator(null);
		
		punishment.mute(mediator, reason, duration);
		sender.sendMessage(ChatColor.YELLOW + "Player muted.");
		Player player = Bukkit.getPlayer(punishment.getUUID());
		if(player != null) {
			player.sendMessage(ChatColor.YELLOW + "You have been muted for " + TimeUtils.getDaysHoursMinutes(duration) + ".");
			player.sendMessage(ChatColor.YELLOW + "You were muted for '" + reason + "'");
		}
		return true;
	}
	
	@Override
	public String command() {
		
		return "mute";
	}

	@Override
	public String perm() {
		
		return "froobbasics.mute";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			completions = CommandUtils.tabCompletePlayerList(args[0], true, true, uuidManager);
		}
		if(args.length == 2) {
			completions.add("15m");
			completions.add("30m");
			completions.add("1h");
			completions.add("2h");
			completions.add("3h");
			completions.add("6h");
			completions.add("12h");
			completions.add("1d");
			completions.add("7d");
			completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
		}
		if(args.length == 3) {
			completions.add("profanity");
			completions.add("bigotry");
			completions.add("rudeness");
			completions.add("advertising");
			completions = StringUtil.copyPartialMatches(args[2], completions, new ArrayList<String>(completions.size()));
		}
			
		return completions;
	}

}
