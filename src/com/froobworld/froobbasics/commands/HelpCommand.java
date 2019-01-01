package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.HelpObject;
import com.froobworld.froobbasics.managers.HelpManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand extends PlayerCommandExecutor {

    private HelpManager helpManager;

    private int PAGE_LENGTH = 5;

    public HelpCommand(HelpManager helpManager) {
        this.helpManager = helpManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        ArrayList<HelpObject> helpObjects = null;
        int page = 1;
        String arg = null;

        if (args.length == 0) {
            helpObjects = helpManager.getHelpObjects(player);
        }
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[args.length - 1]);
                arg = args.length > 1 ? StringUtils.join(Arrays.copyOf(args, args.length - 1), " ") : null;
            } catch (NumberFormatException ex) {
                arg = StringUtils.join(args, " ");
            }

            helpObjects = helpManager.filterHelpObjects(helpManager.getHelpObjects(player), arg);
        }
        if (helpObjects.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There is no help for the given argument.");
            return false;
        }
        int pages = PageUtils.pages(helpObjects, PAGE_LENGTH);
        helpObjects = PageUtils.page(helpObjects, PAGE_LENGTH, page);
        if (helpObjects == null) {
            player.sendMessage(ChatColor.RED + "That page does not exist.");
            return false;
        }
        if (arg == null) {
            player.sendMessage(ChatColor.RED + "---- " + ChatColor.WHITE + "Help page "
                    + page + "/" + pages + ChatColor.RED + " ----");
        } else {
            player.sendMessage(ChatColor.RED + "---- " + ChatColor.WHITE + "Help for \'" + arg + "\' page "
                    + page + "/" + pages + ChatColor.RED + " ----");
        }

        for (HelpObject object : helpObjects) {
            player.sendMessage(object.toString());
        }
        return false;
    }

    @Override
    public String command() {

        return "help";
    }

    @Override
    public String perm() {

        return "froobbasics.help";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
