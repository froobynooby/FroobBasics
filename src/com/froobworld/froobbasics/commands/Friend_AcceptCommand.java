package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.UUIDUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Friend_AcceptCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Friend_AcceptCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 2) {
            player.sendMessage("/" + cl + " accept <player>");
            return false;
        }
        Player friend = Bukkit.getPlayerExact(args[1]);
        Playerdata data = playerManager.getPlayerdata(player);
        Playerdata friendData = playerManager.commandSearchForPlayer(args[1], player, uuidManager);
        if (friendData == null) {
            return false;
        }
        if (!data.getFriendRequests().contains(friendData.getUUID())) {
            player.sendMessage(ChatColor.RED + "That player has not requested for you to be their friend.");
            return false;
        }

        data.removeFriendRequest(friendData.getUUID());
        data.addFriend(friendData.getUUID());
        friendData.removePendingFriend(player.getUniqueId());
        friendData.addFriend(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "Friend request accepted.");
        if (friend != null) {
            friend.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.YELLOW + " has"
                    + " accepted your friend request.");
        }
        return true;
    }

    @Override
    public String command() {

        return "friend accept";
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
            completions = UUIDUtils.convertToNames(data.getFriendRequests(), uuidManager, false);
            completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
