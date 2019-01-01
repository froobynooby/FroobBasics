package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.TeleportManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpacceptCommand extends PlayerCommandExecutor {
    private TeleportManager teleportManager;
    private PlayerManager playerManager;

    public TpacceptCommand(TeleportManager teleportManager, PlayerManager playerManager) {
        this.teleportManager = teleportManager;
        this.playerManager = playerManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        teleportManager.acceptRequest(player, playerManager);
        return true;
    }

    @Override
    public String command() {

        return "tpaccept";
    }

    @Override
    public String perm() {

        return "froobbasics.tpaccept";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}