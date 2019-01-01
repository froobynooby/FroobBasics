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

public class Friend_RequestsCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Friend_RequestsCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Playerdata data = playerManager.getPlayerdata(player);


        if (data.getFriendRequests().size() == 0) {
            player.sendMessage(ChatColor.RED + "You have no friend requests.");
            return true;
        }
        player.sendMessage(ChatColor.YELLOW + "You have friend requests from the following " +
                (data.getFriendRequests().size() == 1 ? "person" : "people"));
        player.sendMessage(PageUtils.toString(UUIDUtils.convertToNames(data.getFriendRequests(), uuidManager, true)));
        return true;
    }

    @Override
    public String command() {

        return "friend requests";
    }

    @Override
    public String perm() {

        return "froobbasics.friend";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("requests");
        }

        return completions;
    }


}
