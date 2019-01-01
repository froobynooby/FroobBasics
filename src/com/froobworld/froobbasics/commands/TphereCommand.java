package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TphereCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;

    public TphereCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length == 0) {
            player.sendMessage("/" + cl + " <player>");
            return false;
        }
        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }
        Playerdata data = playerManager.getPlayerdata(receiver);

        data.setBackLocation(player.getLocation(), false);
        receiver.teleport(player);
        player.sendMessage(ChatColor.YELLOW + "Teleported " + receiver.getDisplayName() + ChatColor.YELLOW + " to you.");
        receiver.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.YELLOW + " has teleported you to them.");
        return true;
    }

    @Override
    public String command() {

        return "tphere";
    }

    @Override
    public String perm() {

        return "froobbasics.tphere";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            return CommandUtils.tabCompletePlayerList(args[0], false, false, null);
        }

        return completions;
    }

}