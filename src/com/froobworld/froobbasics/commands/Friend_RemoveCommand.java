package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.UUIDUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Friend_RemoveCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Friend_RemoveCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length <= 1) {
            player.sendMessage("/" + cl + " remove <player>");
            return false;
        }
        Playerdata data = playerManager.getPlayerdata(player);
        Playerdata friendData = playerManager.commandSearchForPlayer(args[1], player, uuidManager);
        if (friendData == null) {
            return false;
        }
        if (!friendData.getFriends().contains(player.getUniqueId()) &&
                !friendData.getFriendRequests().contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are not that person's friend.");
            return false;
        }

        friendData.removeFriend(player.getUniqueId());
        friendData.removeFriendRequest(player.getUniqueId());
        data.removeFriend(friendData.getUUID());
        data.removePendingFriend(friendData.getUUID());
        player.sendMessage(ChatColor.YELLOW + "Successfully removed that player as a friend.");

        return true;
    }

    @Override
    public String command() {

        return "friend remove";
    }

    @Override
    public String perm() {

        return "froobbasics.friend";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("accept");
        }
        if (args.length == 2) {
            Playerdata data = playerManager.getPlayerdata(player);
            completions.addAll(UUIDUtils.convertToNames(data.getFriends(), uuidManager, false));
            completions.addAll(UUIDUtils.convertToNames(data.getPendingFriends(), uuidManager, false));
            completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
