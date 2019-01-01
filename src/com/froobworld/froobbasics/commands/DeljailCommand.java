package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Jail;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.CommandExecutor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class DeljailCommand extends CommandExecutor {
    private PunishmentManager punishmentManager;

    public DeljailCommand(PunishmentManager punishmentManager) {
        this.punishmentManager = punishmentManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("/" + cl + " <jail>");
            return false;
        }
        Jail jail = punishmentManager.getJail(args[0]);
        if (jail == null) {
            sender.sendMessage(ChatColor.RED + "That jail does not exist.");
            return false;
        }

        punishmentManager.deleteJail(args[0]);
        sender.sendMessage(ChatColor.YELLOW + "Jail deleted.");
        return true;
    }

    @Override
    public String command() {

        return "deljail";
    }

    @Override
    public String perm() {

        return "froobbasics.deljail";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
