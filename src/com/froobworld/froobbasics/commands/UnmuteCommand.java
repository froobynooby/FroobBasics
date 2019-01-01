package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.Mediator;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class UnmuteCommand extends CommandExecutor {
    private PunishmentManager punishmentManager;
    private UUIDManager uuidManager;

    public UnmuteCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
        this.punishmentManager = punishmentManager;
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/" + cl + " <player>");
            return false;
        }
        Punishment punishment = punishmentManager.commandSearchForPlayer(args[0], sender, uuidManager);
        if (punishment == null) {
            return false;
        }
        if (!punishment.isMuted()) {
            sender.sendMessage(ChatColor.RED + "That player is not muted.");
            return false;
        }
        Mediator mediator = (sender instanceof Player) ? new Mediator(((Player) sender).getUniqueId()) : new Mediator(null);
        punishment.unmute(mediator);
        sender.sendMessage(ChatColor.YELLOW + "Player unmuted.");
        return true;
    }

    @Override
    public String command() {

        return "unmute";
    }

    @Override
    public String perm() {

        return "froobbasics.unmute";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Punishment punishment = punishmentManager.getPunishment(player);
                if (punishment.isMuted()) {
                    completions.add(player.getName());
                }
                completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
            }
        }
        return completions;
    }

}
