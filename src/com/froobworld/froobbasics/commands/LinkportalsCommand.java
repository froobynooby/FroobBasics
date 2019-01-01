package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Portal;
import com.froobworld.froobbasics.managers.PortalManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LinkportalsCommand extends CommandExecutor {
    public static final String PERM = "froobbasics.linkportals";

    private PortalManager portalManager;

    public LinkportalsCommand(PortalManager portalManager) {
        this.portalManager = portalManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("/" + cl + " <portal 1> <portal 2>");
            return false;
        }
        Portal portal1 = portalManager.getPortal(args[0]);
        Portal portal2 = portalManager.getPortal(args[1]);
        if (portal1 == null || portal2 == null) {
            sender.sendMessage(ChatColor.RED + "That portal does not exist.");
            return false;
        }
        if (portal1 == portal2) {
            sender.sendMessage(ChatColor.RED + "You cannot link a portal to itself.");
            return false;
        }

        if (portal1.getLink() != null) {
            portal1.getLink().setLink(null);
        }
        if (portal2.getLink() != null) {
            portal2.getLink().setLink(null);
        }

        portal1.setLink(portal2);
        portal2.setLink(portal1);
        sender.sendMessage(ChatColor.YELLOW + portal1.getName() + " has been linked to " + portal2.getName() + ".");
        return true;
    }


    @Override
    public String command() {

        return "linkportals";
    }


    @Override
    public String perm() {

        return "froobbasics.linkportals";
    }


    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1 || args.length == 2) {
            completions = PageUtils.toStringList(portalManager.getPortals());
            completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }
}
