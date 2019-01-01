package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.Mediator;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanCommand extends CommandExecutor {
    private PunishmentManager punishmentManager;
    private UUIDManager uuidManager;

    public BanCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
        this.punishmentManager = punishmentManager;
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/" + cl + " <player> [reason]");
            return false;
        }
        Punishment punishment = punishmentManager.commandSearchForPlayer(args[0], sender, uuidManager);
        if (punishment == null) {
            return false;
        }
        String reason = "breaking a rule";
        if (args.length > 1) {
            reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
        }

        Mediator mediator = (sender instanceof Player) ? new Mediator(((Player) sender).getUniqueId()) : new Mediator(null);
        punishment.ban(mediator, reason);
        Player player = Bukkit.getPlayer(punishment.getUUID());
        if (player != null) {
            player.kickPlayer("Banned: " + reason + "\n\n Appeal at " + Config.getBanAppealUrl());
        }
        Bukkit.getBanList(Type.NAME).addBan(uuidManager.getUUIDDataFull(punishment.getUUID()).getLastName(),
                reason, null, sender.getName());
        Bukkit.broadcastMessage(uuidManager.getUUIDData(punishment.getUUID()).getLastName() +
                " has been banned (" + reason + ").");
        return true;
    }

    @Override
    public String command() {

        return "ban";
    }

    @Override
    public String perm() {

        return "froobbasics.ban";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions = CommandUtils.tabCompletePlayerList(args[0], true, true, uuidManager);
        }
        if (args.length == 2) {
            completions.add("grief");
            completions.add("x-ray");
            completions.add("theft");
            completions.add("bigotry");
            completions.add("hacking");
            completions.add("advertising");
            completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
