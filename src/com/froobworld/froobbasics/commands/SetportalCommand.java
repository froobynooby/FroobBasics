package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PortalManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetportalCommand extends PlayerCommandExecutor {
    private PortalManager portalManager;

    public SetportalCommand(PortalManager portalManager) {
        this.portalManager = portalManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 2) {
            player.sendMessage("/" + cl + " <name> <radius>");
            return false;
        }
        String name = args[0];
        Double radius = Double.valueOf(args[1]);
        if (portalManager.getPortal(name) != null) {
            player.sendMessage(ChatColor.RED + "A portal by that name already exists.");
            return false;
        }
        if (radius == null || radius <= 0) {
            player.sendMessage(ChatColor.RED + "The radius must be a positive number.");
            return false;
        }

        portalManager.createPortal(player, name, radius.doubleValue());
        player.sendMessage(ChatColor.YELLOW + "Portal created.");
        return true;
    }


    @Override
    public String command() {

        return "setportal";
    }


    @Override
    public String perm() {

        return "froobbasics.setportal";
    }


    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }
}
