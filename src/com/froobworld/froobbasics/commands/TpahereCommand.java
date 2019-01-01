package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.TeleportManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpahereCommand extends PlayerCommandExecutor {
    private TeleportManager teleportManager;
    private PlayerManager playerManager;

    public TpahereCommand(TeleportManager teleportManager, PlayerManager playerManager) {
        this.teleportManager = teleportManager;
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

        teleportManager.requestTeleport("here", player, receiver, playerManager);
        return true;
    }

    @Override
    public String command() {

        return "tpahere";
    }

    @Override
    public String perm() {

        return "froobbasics.tpahere";
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
