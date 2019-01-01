package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetjailCommand extends PlayerCommandExecutor {
    private PunishmentManager punishmentManager;

    public SetjailCommand(PunishmentManager punishmentManager) {
        this.punishmentManager = punishmentManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 2) {
            player.sendMessage("/" + cl + " <jail> <radius>");
            return false;
        }
        String name = args[0];
        Double radius = null;
        try {
            radius = Double.valueOf(args[1]);
        } catch (NumberFormatException ex) {

        }
        if (radius == null || radius < 0) {
            player.sendMessage(ChatColor.RED + "The radius must be a positive number.");
            return false;
        }
        if (punishmentManager.getJail(name) != null) {
            player.sendMessage(ChatColor.RED + "A jail by that name already exists.");
            return false;
        }

        punishmentManager.createJail(player, name, radius);
        player.sendMessage(ChatColor.YELLOW + "Jail created.");
        return false;
    }

    @Override
    public String command() {

        return "setjail";
    }

    @Override
    public String perm() {

        return "froobbasics.setjail";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
