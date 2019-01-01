package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.ContainerCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TptoggleCommand extends ContainerCommandExecutor {

    public TptoggleCommand(PlayerManager playerManager) {
        addSubCommand(new Tptoggle_FriendsCommand(playerManager), 0, "friends", "/tptoggle friends");
        addSubCommand(new Tptoggle_RequestsCommand(playerManager), 0, "requests", "/tptoggle requests");
    }

    @Override
    public String command() {

        return "tptoggle";
    }

    @Override
    public String perm() {

        return "froobbasics.tptoggle";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("friends");
            completions.add("requests");
            completions = StringUtil.copyPartialMatches(args[0], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
