package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.frooblib.data.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AfkManager extends TaskManager {
    private HashMap<Player, Boolean> afkStatus;
    private HashMap<Player, Long> lastActive;
    private HashMap<Player, Location> locationAtAnnouncment;
    private HashMap<Player, Long> timeAtAnnouncment;

    public AfkManager() {
        super(FroobBasics.getPlugin());
    }


    @Override
    public void ini() {
        afkStatus = new HashMap<Player, Boolean>();
        lastActive = new HashMap<Player, Long>();
        locationAtAnnouncment = new HashMap<Player, Location>();
        timeAtAnnouncment = new HashMap<Player, Long>();

        addTask(0, 10, new Runnable() {

            @Override
            public void run() {
                task();

            }
        });
    }

    public boolean isAFK(Player player) {
        if (!afkStatus.containsKey(player)) {
            return false;
        }

        return afkStatus.get(player);
    }

    public long getLastActive(Player player) {
        if (!lastActive.containsKey(player)) {
            return System.currentTimeMillis();
        }

        return lastActive.get(player);
    }

    public Location getLocationAtAnnouncment(Player player) {
        if (!locationAtAnnouncment.containsKey(player)) {
            return null;
        }

        return locationAtAnnouncment.get(player);
    }

    public long getTimeAtAnnounment(Player player) {
        if (!timeAtAnnouncment.containsKey(player)) {
            return System.currentTimeMillis();
        }

        return timeAtAnnouncment.get(player);
    }

    public void setAfk(Player player, boolean bool, boolean announce) {
        afkStatus.put(player, bool);
        if (announce) {
            Bukkit.broadcastMessage(getAfkMessage(player, bool));
        }
        if (bool = true) {
            timeAtAnnouncment.put(player, System.currentTimeMillis());
            locationAtAnnouncment.put(player, player.getLocation());
            setLastActive(player, System.currentTimeMillis());
            return;
        }
        if (bool = false) {
            setLastActive(player, System.currentTimeMillis());
            return;
        }
    }

    public void setLastActive(Player player, long time) {
        lastActive.put(player, time);
    }

    public void task() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isAFK(player)) {
                if ((System.currentTimeMillis() - getLastActive(player)) / 1000 >=
                        Config.getTimeForAfkAnnouncement()) {
                    setAfk(player, true, true);
                }
                continue;
            } else {
                if ((System.currentTimeMillis() - getTimeAtAnnounment(player)) / 1000 >=
                        Config.getTimeForAfkKick()) {
                    player.kickPlayer("Kicked: AFK too long.");
                    setAfk(player, false, false);
                }
            }
        }
    }

    public String getAfkMessage(Player player, boolean afk) {
        if (afk) {
            return player.getDisplayName() + ChatColor.WHITE + " is now AFK.";
        } else {
            return player.getDisplayName() + ChatColor.WHITE + " is no longer AFK.";
        }
    }

}
