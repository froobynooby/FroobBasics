package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Warp;
import com.froobworld.froobbasics.managers.WarpManager;
import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class WarpsCommand extends CommandExecutor {
    private static final int PAGE_LENGTH = 20;
    private WarpManager warpManager;

    public WarpsCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
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
        ArrayList<Warp> warps = warpManager.getWarps();
        int pages = PageUtils.pages(warps, PAGE_LENGTH);
        int total = warps.size();
        if (pages == 0) {
            sender.sendMessage(ChatColor.YELLOW + "There are no warps.");
            return true;
        }
        warps = PageUtils.page(warps, PAGE_LENGTH, page);

        if (warps == null) {
            sender.sendMessage(Messages.PAGE_NOT_EXIST);
            return false;
        }

        sender.sendMessage(ChatColor.YELLOW + "There " + (total == 1 ? "is one warp." : ("are " + total + " warps."))
                + " Showing page " + page + " of " + pages);
        sender.sendMessage(PageUtils.toString(warps));
        return true;
    }

    @Override
    public String command() {

        return "warps";
    }

    @Override
    public String perm() {

        return "froobbasics.warps";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            int pages = PageUtils.pages(warpManager.getWarps(), PAGE_LENGTH);
            for (int i = 1; i <= pages; i++) {
                completions.add(i + "");
            }
            completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
        }
        return completions;
    }

}
