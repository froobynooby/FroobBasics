package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.AfkManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AfkCommand extends PlayerCommandExecutor {
    private AfkManager afkManager;

    public AfkCommand(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        afkManager.setAfk(player, !afkManager.isAFK(player), true);
        return true;
    }

    @Override
    public String command() {

        return "afk";
    }

    @Override
    public String perm() {

        return "froobbasics.afk";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
