package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Warp;
import com.froobworld.froobbasics.managers.WarpManager;
import com.froobworld.frooblib.command.CommandExecutor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelwarpCommand extends CommandExecutor {
    private WarpManager warpManager;

    public DelwarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/" + cl + " <name>");
            return false;
        }
        String name = StringUtils.join(args, " ");
        Warp warp = warpManager.getWarp(name);

        if (warp == null) {
            sender.sendMessage(ChatColor.RED + "A warp by that name does not exist.");
            return false;
        }

        warpManager.deleteWarp(name);
        sender.sendMessage(ChatColor.YELLOW + "Warp deleted.");
        return true;
    }

    @Override
    public String command() {

        return "delwarp";
    }

    @Override
    public String perm() {

        return "froobbasics.delwarp";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        for (Warp warp : warpManager.getWarps()) {
            String[] split = warp.getName().split(" ");
            if (split.length >= args.length) {
                if (warp.getName().toLowerCase().startsWith(StringUtils.join(args, " ").toLowerCase())) {
                    completions.add(StringUtils.join(Arrays.copyOfRange(split, args.length - 1, split.length), " "));
                }
            }
        }
        completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));

        return completions;
    }

}
