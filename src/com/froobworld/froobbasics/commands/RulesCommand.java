package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.Config;
import com.froobworld.frooblib.command.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand extends CommandExecutor {


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        for (String string : Config.getRules()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        }
        return true;
    }

    @Override
    public String command() {

        return "rules";
    }

    @Override
    public String perm() {

        return "froobbasics.rules";
    }

    @Override
    public List<String> tabCompletions(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
