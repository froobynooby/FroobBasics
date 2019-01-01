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

public class Friend_ListCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Friend_ListCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Playerdata data = playerManager.getPlayerdata(player);

        if (data.getFriends().size() + data.getPendingFriends().size() == 0) {
            player.sendMessage(ChatColor.RED + "You have no friends. So sorry to hear that...");
            return true;
        }
        if (data.getFriends().size() != 0) {
            player.sendMessage(ChatColor.YELLOW + "You have " +
                    (data.getFriends().size() == 1 ? "one friend" : data.getFriends().size() + " friends"));
            player.sendMessage(PageUtils.toString(UUIDUtils.convertToNames(data.getFriends(), uuidManager, true)));
        }
        if (data.getPendingFriends().size() != 0) {
            player.sendMessage(ChatColor.YELLOW + "You have " +
                    (data.getPendingFriends().size() == 1 ? "one pending friend request" : data.getPendingFriends().size() + " pending friend requests"));
            player.sendMessage(PageUtils.toString(UUIDUtils.convertToNames(data.getPendingFriends(), uuidManager, true)));
        }
        return true;
    }

    @Override
    public String command() {

        return "friend list";
    }

    @Override
    public String perm() {

        return "froobbasics.friend";
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
