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

public class DelportalCommand extends CommandExecutor {
    private PortalManager portalManager;

    public DelportalCommand(PortalManager portalManager) {
        this.portalManager = portalManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/" + cl + " <portal>");
            return false;
        }
        String name = args[0];
        Portal portal = portalManager.getPortal(name);
        if (portal == null) {
            sender.sendMessage(ChatColor.RED + "A portal by that name does not exist.");
            return false;
        }

        portalManager.deletePortal(name);
        sender.sendMessage(ChatColor.YELLOW + "Portal deleted.");
        return true;
    }


    @Override
    public String command() {

        return "delportal";
    }


    @Override
    public String perm() {

        return "froobbasics.delportal";
    }


    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions = PageUtils.toStringList(portalManager.getPortals());
            completions = StringUtil.copyPartialMatches(args[0], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }
}
