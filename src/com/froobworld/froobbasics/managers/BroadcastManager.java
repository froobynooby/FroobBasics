package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.frooblib.data.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BroadcastManager extends TaskManager {
    private int place = 0;

    public BroadcastManager() {
        super(FroobBasics.getPlugin());
    }


    public void ini() {
        addTask(Config.getBroadcastDelay() * 20, Config.getBroadcastDelay() * 20, new Runnable() {

            @Override
            public void run() {
                task();
            }
        });
    }

    public void task() {
        if (!Config.isBroadcastEnabled()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Config.getBroadcastMessages().get(place)));
        }

        place = Config.getBroadcastMessages().size() <= place + 1 ? 0 : place + 1;
    }

}
