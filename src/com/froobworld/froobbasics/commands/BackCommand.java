package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TeleportUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;

    public BackCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Playerdata data = playerManager.getPlayerdata(player);
        if (data.getBackLocation() == null) {
            player.sendMessage(ChatColor.RED + "There is no where to go back to.");
            return false;
        }

        Location location = player.getLocation().clone();
        player.teleport(TeleportUtils.findSafeTeleportLocation(data.getBackLocation()));
        data.setBackLocation(location, false);
        return true;
    }

    @Override
    public String command() {

        return "back";
    }

    @Override
    public String perm() {

        return "froobbasics.back";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }
}
