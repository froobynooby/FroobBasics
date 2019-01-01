package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.froobworld.froobbasics.data.Punishment.PunishmentRecord;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class StafflogCommand extends CommandExecutor{
	private PunishmentManager punishmentManager;
	private UUIDManager uuidManager;
	
	private static final int PAGE_LENGTH = 5;
	
	public StafflogCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
		this.punishmentManager = punishmentManager;
		this.uuidManager = uuidManager;
	}
	
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<PunishmentRecord> records = new ArrayList<PunishmentRecord>();
		for(PunishmentRecord r : punishmentManager.getPunishmentRecords()) {
			records.add(r);
		}
		Collections.reverse(records);
		int page = 1;
		int pages = PageUtils.pages(records, PAGE_LENGTH);
		int total = records.size();
		
		if(args.length > 0) {
			try {
				page = Integer.parseInt(args[0]);
			}catch(NumberFormatException ex) {
				sender.sendMessage(Messages.PAGE_NOT_A_NUMBER);
				return false;
			}
		}
		
		if(total == 0) {
			sender.sendMessage(ChatColor.YELLOW + "There are no records.");
			return true;
		}
		records = PageUtils.page(records, PAGE_LENGTH, page);
		if(records == null) {
			sender.sendMessage(Messages.PAGE_NOT_EXIST);
			return false;
		}
		
		sender.sendMessage(ChatColor.YELLOW + "There " + (total == 1 ?
				"is one record.":("are " + total + " records.")) + " Showing page " + page + " of " + pages);
		for(PunishmentRecord r : records) {
			sender.sendMessage(r.toString(uuidManager));
		}
		return true;
	}
	
	@Override
	public String command() {

		return "stafflog";
	}

	@Override
	public String perm() {

		return "froobbasics.stafflog";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			int pages = PageUtils.pages(punishmentManager.getPunishmentRecords(), PAGE_LENGTH);
			for(int i = 1; i <= pages; i++) {
				completions.add(i + "");
			}
			completions = StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
		}
		
		return completions;
	}

}
