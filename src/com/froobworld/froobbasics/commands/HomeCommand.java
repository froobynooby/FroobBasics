package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Home;
import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.HomeManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TeleportUtils;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeCommand extends PlayerCommandExecutor {
    private HomeManager homeManager;
    private PlayerManager playerManager;

    public HomeCommand(HomeManager homeManager, PlayerManager playerManager) {
        this.homeManager = homeManager;
        this.playerManager = playerManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        String name = "default";
        if (args.length > 0) {
            name = StringUtils.join(args, " ");
        }

        Home home = homeManager.getHome(player, name);
        if (home == null) {
            player.sendMessage(ChatColor.RED + "You don't have a home by that name.");
            return false;
        }
        Playerdata data = playerManager.getPlayerdata(player);
        data.setBackLocation(player.getLocation(), false);

        player.teleport(TeleportUtils.findSafeTeleportLocation(home.getLocation()));
        player.sendMessage(ChatColor.YELLOW + "There's no place like home...");
        return true;
    }

    @Override
    public String command() {

        return "home";
    }

    @Override
    public String perm() {

        return "froobbasics.home";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        for (Home home : homeManager.getHomes(player)) {
            String[] split = home.getName().split(" ");
            if (split.length >= args.length) {
                if (home.getName().toLowerCase().startsWith(StringUtils.join(args, " ").toLowerCase())) {
                    completions.add(StringUtils.join(Arrays.copyOfRange(split, args.length - 1, split.length), " "));
                }
            }
        }
        completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));

        return completions;
    }

}
