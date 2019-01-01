package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Portal;
import com.froobworld.froobbasics.managers.PortalManager;
import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PortalsCommand extends CommandExecutor {
    private static final int PAGE_LENGTH = 20;
    private PortalManager portalManager;

    public PortalsCommand(PortalManager portalManager) {
        this.portalManager = portalManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(Messages.PAGE_NOT_A_NUMBER);
                return false;
            }
        }
        ArrayList<Portal> portals = portalManager.getPortals();
        int pages = PageUtils.pages(portals, PAGE_LENGTH);
        int total = portals.size();
        if (pages == 0) {
            sender.sendMessage(ChatColor.YELLOW + "There are no portals.");
            return true;
        }
        portals = PageUtils.page(portals, PAGE_LENGTH, page);

        if (portals == null) {
            sender.sendMessage(Messages.PAGE_NOT_EXIST);
            return false;
        }

        sender.sendMessage(ChatColor.YELLOW + "There " + (total == 1 ? "is one portal." : ("are " + total + " portals."))
                + " Showing page " + page + " of " + pages);
        sender.sendMessage(PageUtils.toString(portals));
        return true;
    }


    @Override
    public String command() {

        return "portals";
    }


    @Override
    public String perm() {

        return "froobbasics.portals";
    }


    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
