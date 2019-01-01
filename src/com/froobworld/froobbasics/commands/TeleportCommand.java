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

public class TeleportCommand extends PlayerCommandExecutor {
    private String PERM_TP_ALL = perm() + ".all";

    private PlayerManager playerManager;

    public TeleportCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length == 0) {
            player.sendMessage("/" + cl + " <player>");
            return false;
        }
        Player to = Bukkit.getPlayer(args[0]);
        if (to == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }
        Playerdata data = playerManager.getPlayerdata(player);
        if (!player.hasPermission(PERM_TP_ALL)) {
            if (!data.getFriends().contains(to.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You can only instantly teleport to people on your friends list. Use /tpa instead.");
                return false;
            }
            Playerdata toData = playerManager.getPlayerdata(to);
            if (toData.isTeleportFriendsDisabled()) {
                player.sendMessage(ChatColor.RED + "That player does not want people teleporting to them.");
                return false;
            }
        }
        data.setBackLocation(player.getLocation(), false);
        player.teleport(to);
        player.sendMessage(ChatColor.YELLOW + "Teleported to " + to.getDisplayName() + ChatColor.YELLOW + ".");
        return true;
    }

    @Override
    public String command() {

        return "teleport";
    }

    @Override
    public String perm() {

        return "froobbasics.teleport";
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