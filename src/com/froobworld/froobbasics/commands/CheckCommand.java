package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.PunishmentRecord;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.utils.TimeUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CheckCommand extends CommandExecutor {
    private PunishmentManager punishmentManager;
    private UUIDManager uuidManager;

    public CheckCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
        this.punishmentManager = punishmentManager;
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/" + cl + " <player>");
            return false;
        }
        Punishment punishment = punishmentManager.commandSearchForPlayer(args[0], sender, uuidManager);
        if (punishment == null) {
            return false;
        }

        if (!punishment.isBanned()) {
            sender.sendMessage(ChatColor.YELLOW + "That player is not banned.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "That player was banned " + TimeUtils.getDaysHoursMinutes(
                    System.currentTimeMillis() - punishment.getBannedTime()) + " ago:");
            sender.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.RESET + punishment.getBanReason());
            sender.sendMessage(ChatColor.YELLOW + "Mediator: " + ChatColor.RESET + punishment.getBannedBy().getName(uuidManager));
        }
        if (!punishment.isMuted()) {
            sender.sendMessage(ChatColor.YELLOW + "That player is not muted.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "That player was muted " + TimeUtils.getDaysHoursMinutes(
                    System.currentTimeMillis() - punishment.getMutedTime()) + " ago:");
            sender.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.RESET + punishment.getMuteReason());
            sender.sendMessage(ChatColor.YELLOW + "Remaining duration: " + ChatColor.RESET + TimeUtils.getDaysHoursMinutes(
                    punishment.getMutedTime() + punishment.getMutePeriod() - System.currentTimeMillis()));
            sender.sendMessage(ChatColor.YELLOW + "Mediator: " + ChatColor.RESET + punishment.getMutedBy().getName(uuidManager));
        }
        if (!punishment.isJailed()) {
            sender.sendMessage(ChatColor.YELLOW + "That player is not jailed.");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "That player was jailed " + TimeUtils.getDaysHoursMinutes(
                    System.currentTimeMillis() - punishment.getJailedTime()) + " ago:");
            sender.sendMessage(ChatColor.YELLOW + "Reason: " + ChatColor.RESET + punishment.getJailReason());
            sender.sendMessage(ChatColor.YELLOW + "Remaining duration: " + ChatColor.RESET + TimeUtils.getDaysHoursMinutes(
                    punishment.getMutedTime() + punishment.getMutePeriod() - System.currentTimeMillis()));
            sender.sendMessage(ChatColor.YELLOW + "Mediator: " + ChatColor.RESET + punishment.getJailedBy().getName(uuidManager));
        }
        if (punishment.getHistory().size() > 0) {
            sender.sendMessage(ChatColor.YELLOW + "Punishment history:");
            for (PunishmentRecord record : punishment.getHistory()) {
                sender.sendMessage(record.toString(uuidManager));
            }
        }
        return true;
    }

    @Override
    public String command() {

        return "check";
    }

    @Override
    public String perm() {

        return "froobbasics.check";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            return CommandUtils.tabCompletePlayerList(args[0], true, true, uuidManager);
        }

        return completions;
    }


}
