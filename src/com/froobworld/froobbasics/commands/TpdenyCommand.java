package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.TeleportManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpdenyCommand extends PlayerCommandExecutor {
    private TeleportManager teleportManager;

    public TpdenyCommand(TeleportManager teleportManager) {
        this.teleportManager = teleportManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        teleportManager.denyRequest(player);
        return true;
    }

    @Override
    public String command() {

        return "tpdeny";
    }

    @Override
    public String perm() {

        return "froobbasics.tpdeny";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}