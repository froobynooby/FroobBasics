package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Home;
import com.froobworld.froobbasics.managers.HomeManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand extends PlayerCommandExecutor {
    private HomeManager homeManager;

    public HomesCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        ArrayList<Home> homes = homeManager.getHomes(player);

        if (homes.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You have no homes.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "You have " + (homes.size() == 1
                ? "one home." : (homes.size() + " homes.")));
        player.sendMessage(PageUtils.toString(homes));
        return true;
    }

    @Override
    public String command() {

        return "homes";
    }

    @Override
    public String perm() {

        return "froobbasics.homes";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
