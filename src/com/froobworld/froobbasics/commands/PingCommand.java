package com.froobworld.froobbasics.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PingCommand extends CommandExecutor {

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (cl.equalsIgnoreCase("table")) {
            sender.sendMessage("Tennis!");
            return true;
        }
        sender.sendMessage("Pong!");
        return true;
    }

    @Override
    public String command() {

        return "ping";
    }

    @Override
    public String perm() {

        return "froobbasics.ping";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
