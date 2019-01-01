package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.frooblib.data.Manager;
import com.froobworld.frooblib.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeleportManager extends Manager {
    private HashMap<Player, String> requests;


    @Override
    public void ini() {
        requests = new HashMap<Player, String>();
    }

    public void requestTeleport(String type, Player sender, Player receiver, PlayerManager playerManager) {
        if (!type.equalsIgnoreCase("here") && !type.equalsIgnoreCase("to")) {
            sender.sendMessage(ChatColor.RED + "Something went wrong.");
            return;
        }
        if (receiver == null) {
            sender.sendMessage(ChatColor.RED + "Could not find that player.");
            return;
        }
        Playerdata data = playerManager.getPlayerdata(receiver);
        if (data.ignoreList().contains(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player is ignoring you.");
            return;
        }
        if (data.isTeleportRequestsDisabled()) {
            sender.sendMessage(ChatColor.RED + "That player is not accepting teleport requests.");
            return;
        }

        requests.put(receiver, type.toLowerCase() + ":" + sender.getUniqueId().toString());
        sender.sendMessage(ChatColor.YELLOW + "Request sent.");
        if (type.equalsIgnoreCase("here")) {
            receiver.sendMessage(sender.getDisplayName() + ChatColor.YELLOW + " has requested for you to teleport to them.");
            receiver.sendMessage(ChatColor.YELLOW + "/tpaccept to accept.");
            receiver.sendMessage(ChatColor.YELLOW + "/tpdeny to decline.");
        } else {
            receiver.sendMessage(sender.getDisplayName() + ChatColor.YELLOW + " has requested to teleport to you.");
            receiver.sendMessage(ChatColor.YELLOW + "/tpaccept to accept.");
            receiver.sendMessage(ChatColor.YELLOW + "/tpdeny to decline.");
        }
    }

    public void acceptRequest(Player sender, PlayerManager playerManager) {
        if (!requests.containsKey(sender)) {
            sender.sendMessage(ChatColor.RED + "You do not have any requests.");
            return;
        }
        String[] split = requests.get(sender).split(":");
        Player requester = Bukkit.getPlayer(UUID.fromString(split[1]));
        if (requester == null || !requester.isOnline()) {
            sender.sendMessage(ChatColor.RED + "The player that sent the request is no longer here.");
            requests.remove(sender);
            return;
        }
        if (split[0].equalsIgnoreCase("here")) {
            Playerdata data = playerManager.getPlayerdata(sender);
            data.setBackLocation(sender.getLocation(), false);
            sender.teleport(TeleportUtils.findSafeTeleportLocation(requester.getLocation()));
        } else {
            Playerdata data = playerManager.getPlayerdata(requester);
            data.setBackLocation(requester.getLocation(), false);
            requester.teleport(TeleportUtils.findSafeTeleportLocation(sender.getLocation()));

        }
        sender.sendMessage(ChatColor.YELLOW + "Request accepted.");
        requester.sendMessage(sender.getDisplayName() + ChatColor.YELLOW + " has accepted your teleport request.");
        requests.remove(sender);
    }

    public void denyRequest(Player sender) {
        if (!requests.containsKey(sender)) {
            sender.sendMessage(ChatColor.RED + "You do not have any requests.");
            return;
        }
        String[] split = requests.get(sender).split(":");
        Player requester = Bukkit.getPlayer(UUID.fromString(split[1]));
        if (requester == null || !requester.isOnline()) {
            sender.sendMessage(ChatColor.RED + "The player that sent the request is no longer here.");
            requests.remove(sender);
            return;
        }
        requests.remove(sender);
        sender.sendMessage(ChatColor.YELLOW + "Request declined.");
        requester.sendMessage(sender.getDisplayName() + ChatColor.YELLOW + " has declined your teleport request.");
    }

}
