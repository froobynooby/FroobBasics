package com.froobworld.froobbasics.listeners;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityListener implements Listener {
    private PlayerManager playerManager;

    public EntityListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = ((Player) e.getEntity());
            Playerdata data = playerManager.getPlayerdata(player);
            if (data.isVanished()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != player && p.getWorld() == player.getWorld()) {
                        if (p.getLocation().distance(player.getLocation()) <= 75) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e) {
        if (e.getTarget() instanceof Player) {
            Player player = ((Player) e.getTarget());
            Playerdata data = playerManager.getPlayerdata(player);
            if (data.isVanished()) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p != player && p.getWorld() == player.getWorld()) {
                        if (p.getLocation().distance(player.getLocation()) <= 75) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
