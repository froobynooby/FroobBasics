package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.utils.UUIDUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Ignore_ListCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Ignore_ListCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Playerdata data = playerManager.getPlayerdata(player);

        if (data.ignoreList().size() == 0) {
            player.sendMessage(ChatColor.YELLOW + "You are not ignoring anyone.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "You are ignoring " +
                (data.ignoreList().size() == 1 ? "one player" : data.ignoreList().size() + " players"));
        player.sendMessage(PageUtils.toString(UUIDUtils.convertToNames(data.ignoreList(), uuidManager, true)));
        return true;
    }

    @Override
    public String command() {

        return "ignore list";
    }

    @Override
    public String perm() {

        return "froobbasics.ignore";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("list");
        }

        return completions;
    }

}
