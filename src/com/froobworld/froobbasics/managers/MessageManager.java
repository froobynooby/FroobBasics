package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.frooblib.data.Manager;
import com.froobworld.frooblib.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageManager extends Manager {
    private HashMap<Player, Player> reply;


    @Override
    public void ini() {
        reply = new HashMap<Player, Player>();
    }

    public void sendMessage(Player receiver, Player sender, String message, PlayerManager playerManager, PunishmentManager punishmentManager) {
        Punishment punishment = punishmentManager.getPunishment(sender);
        if (punishment.isMuted()) {
            sender.sendMessage(ChatColor.YELLOW + "You are muted for "
                    + TimeUtils.getDaysHoursMinutes(punishment.getMutedTime() + punishment.getMutePeriod() -
                    System.currentTimeMillis()) + ".");
            sender.sendMessage(ChatColor.YELLOW + "You were muted for '" + punishment.getMuteReason() + "'");
            return;
        }

        Playerdata data = playerManager.getPlayerdata(receiver);
        if (data.ignoreList().contains(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player is ignoring you.");
            return;
        }

        receiver.sendMessage("[" + sender.getDisplayName() + " -> Me] " + message);
        sender.sendMessage("[Me -> " + receiver.getDisplayName() + "] " + message);
        reply.put(receiver, sender);
    }

    public void sendReply(Player sender, String message, PlayerManager playerManager, PunishmentManager punishmentManager) {
        Punishment punishment = punishmentManager.getPunishment(sender);
        if (punishment.isMuted()) {
            sender.sendMessage(ChatColor.YELLOW + "You are muted for "
                    + TimeUtils.getDaysHoursMinutes(punishment.getMutedTime() + punishment.getMutePeriod() -
                    System.currentTimeMillis()) + ".");
            sender.sendMessage(ChatColor.YELLOW + "You were muted for '" + punishment.getMuteReason() + "'");
            return;
        }

        if (reply.get(sender) == null) {
            sender.sendMessage(ChatColor.RED + "You have no one to reply to.");
            return;
        }
        Player receiver = Bukkit.getPlayer(reply.get(sender).getUniqueId());
        if (receiver == null) {
            sender.sendMessage(ChatColor.RED + "You have no one to reply to.");
            return;
        }
        Playerdata data = playerManager.getPlayerdata(receiver);
        if (data.ignoreList().contains(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "That player is ignoring you.");
            return;
        }

        receiver.sendMessage("[" + sender.getDisplayName() + " -> Me] " + message);
        sender.sendMessage("[Me -> " + receiver.getDisplayName() + "] " + message);
        reply.put(receiver, sender);
    }

}
