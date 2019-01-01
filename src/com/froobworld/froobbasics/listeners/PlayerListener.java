package com.froobworld.froobbasics.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapCommonAPI;

import com.froobworld.froobbasics.Config;
import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Jail;
import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Spawn;
import com.froobworld.froobbasics.managers.AfkManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.utils.TeleportUtils;
import com.froobworld.frooblib.utils.TimeUtils;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	private PlayerManager playerManager;
	private AfkManager afkManager;
	private PunishmentManager punishmentManager;
	private Spawn spawn;
	
	public PlayerListener(PlayerManager playerManager, AfkManager afkManager, PunishmentManager punishmentManager, Spawn spawn) {
		this.playerManager = playerManager;
		this.afkManager = afkManager;
		this.punishmentManager = punishmentManager;
		this.spawn = spawn;
	}
	
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		playerManager.update(e.getPlayer());
		playerManager.updateVanish();
		
		if(!e.getPlayer().hasPlayedBefore()){
			e.getPlayer().teleport(TeleportUtils.findSafeTeleportLocation(spawn.getSpawnLocation()));
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Config.getFirstJoinMessage())
					.replaceAll("%PLAYER%", e.getPlayer().getDisplayName())
					.replaceAll("%PLAYERLIST%", playerManager.getPlayerList(e.getPlayer(), afkManager)));
			Playerdata data = playerManager.getPlayerdata(e.getPlayer());
			data.setBackLocation(null, true);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(FroobBasics.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for(String string : Config.getMotd()) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', string)
							.replaceAll("%PLAYER%", e.getPlayer().getDisplayName())
							.replaceAll("%PLAYERLIST%", playerManager.getPlayerList(e.getPlayer(), afkManager)));
				}
		    	Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		    	if(data.getMail().size() > 0){
		    		e.getPlayer().sendMessage(ChatColor.YELLOW + "You've got mail. " +
		    				(data.getMail().size() == 1 ? "One message.":(data.getMail().size() + " messages.")));
		    	}
		    	if(data.getFriendRequests().size() > 0){
		    		e.getPlayer().sendMessage(ChatColor.YELLOW + "You've got friend requests. /friend requests");
		    	}
			}
		}, 10);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
		Punishment punishment = punishmentManager.getPunishment(e.getUniqueId());
		if(punishment.isBanned()) {
			e.disallow(Result.KICK_BANNED, "Banned: " + punishment.getBanReason() + "\n\n" + 
					"Appeal at " + Config.getBanAppealUrl());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		playerManager.update(e.getPlayer());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(afkManager.isAFK(e.getPlayer())) {
			afkManager.setAfk(e.getPlayer(), false, true);
		}
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isMuted()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.YELLOW + "You are muted for "
					+ TimeUtils.getDaysHoursMinutes(punishment.getMutedTime() + punishment.getMutePeriod() -
					System.currentTimeMillis()) + ".");
			e.getPlayer().sendMessage(ChatColor.YELLOW + "You were muted for '" + punishment.getMuteReason() + "'");
		}
		
		for(Player player : Bukkit.getOnlinePlayers()){
			Playerdata data = playerManager.getPlayerdata(player);
			if(data.ignoreList().contains(e.getPlayer().getUniqueId())){
				e.getRecipients().remove(player);
			}
		}
		
		Playerdata pdata = playerManager.getPlayerdata(e.getPlayer());
		if(e.getMessage().startsWith("\\" + "\\")){
			pdata.toggleFriendsOnlyChat();
			if(pdata.isFriendsOnlyChat()){
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Friends only chat toggled. Your chat messages will now only go to your friends.");
			}else{
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Friends only chat detoggled. Your chat messages will now be public.");
			}
			e.setCancelled(true);
			return;
		}
		
		final Plugin dynmap = Bukkit.getServer().getPluginManager().getPlugin("dynmap");
		if((pdata.isFriendsOnlyChat() && !(e.getMessage().startsWith("\\") && e.getMessage().length() > 1)) ||
				(!pdata.isFriendsOnlyChat() && (e.getMessage().startsWith("\\") && e.getMessage().length() > 1))){
			if(e.getMessage().startsWith("\\")){
				e.setMessage(e.getMessage().substring(1, e.getMessage().length()));
			}
			e.setMessage(e.getMessage() + "§S");
			if(dynmap != null){
				DynmapCommonAPI api = ((DynmapCommonAPI)dynmap);
				api.setDisableChatToWebProcessing(true);
			}
			for(Player player : Bukkit.getOnlinePlayers()){
				Playerdata data = playerManager.getPlayerdata(player);
				if(!data.getFriends().contains(e.getPlayer().getUniqueId()) && e.getRecipients().contains(player) && player !=e.getPlayer()){
					e.getRecipients().remove(player);
				}
			}
			if(e.getRecipients().size() == 1 && e.getRecipients().contains(e.getPlayer())) {
				e.getPlayer().sendMessage(ChatColor.RED + "You are using friend chat, and no one can see your message.");
			}
			e.setFormat(ChatColor.DARK_GRAY + "~" + ChatColor.WHITE + e.getFormat());
		}else{
			if((e.getMessage().startsWith("\\") && e.getMessage().length() > 1)){
				e.setMessage(e.getMessage().substring(1, e.getMessage().length()));
			}
			if(dynmap != null){
				DynmapCommonAPI api = ((DynmapCommonAPI)dynmap);
				api.setDisableChatToWebProcessing(false);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e){
		if(afkManager.isAFK(e.getPlayer())){
			Location location1 = afkManager.getLocationAtAnnouncment(e.getPlayer());
			Location location2 = e.getPlayer().getLocation();
			if(location1 != null){
				if(location1.getWorld() != location2.getWorld() || location1.distance(location2) > 3){
					afkManager.setAfk(e.getPlayer(), false, true);
				}
				return;
			}
		}else{
			Location to = e.getTo();
			Location from = e.getFrom();
			if(to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()){
				afkManager.setLastActive(e.getPlayer(), System.currentTimeMillis());
			}
			
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent e){
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isJailed()){
			Jail jail = punishment.getJail();
			if(jail != null){
				e.setRespawnLocation(jail.getLocation());
				return;
			}
		}
		
		if(e.getPlayer().getBedSpawnLocation() != null){
			e.setRespawnLocation(e.getPlayer().getBedSpawnLocation());
		}else{
			e.setRespawnLocation(spawn.getSpawnLocation());
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onGameModeChange(PlayerGameModeChangeEvent e){
		if(e.getNewGameMode().equals(GameMode.CREATIVE)){
			if(Config.isCreativeDisabled()){
				e.getPlayer().sendMessage(ChatColor.RED + "Sorry, creative is disabled.");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player player = ((Player)e.getDamager());
			
			Punishment punishment = punishmentManager.getPunishment(player);
			if(punishment.isJailed()){
				e.setCancelled(true);
				player.sendMessage(ChatColor.YELLOW + "You will be unjailed in " +
				TimeUtils.getDaysHoursMinutes(punishment.getJailPeriod() + punishment.getJailedTime() - System.currentTimeMillis()) + ".");
				player.sendMessage(ChatColor.YELLOW +"You were jailed for '" + ChatColor.WHITE + punishment.getJailReason() + "'");
				return;
			}
			
			Playerdata data = playerManager.getPlayerdata(player);
			if(data.isVanished()){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(p != player && p.getWorld() == player.getWorld()){
						if(p.getLocation().distance(player.getLocation()) <= 75){
							e.setCancelled(true);
							player.sendMessage(ChatColor.RED + "You cannot damage entities while vanished.");
							return;
						}
					}
				}
			}
		}else{
			if(e.getDamager() instanceof Projectile){
				if(((Projectile)e.getDamager()).getShooter() instanceof Player){
					Player player = ((Player)((Projectile)e.getDamager()).getShooter());
					
					Punishment punishment = punishmentManager.getPunishment(player);
					if(punishment.isJailed()){
						e.setCancelled(true);
						player.sendMessage(ChatColor.YELLOW + "You will be unjailed in " +
								TimeUtils.getDaysHoursMinutes(punishment.getJailPeriod() + punishment.getJailedTime() - System.currentTimeMillis()) + ".");
								player.sendMessage(ChatColor.YELLOW +"You were jailed for '" + ChatColor.WHITE + punishment.getJailReason() + "'");
						return;
					}
					
					Playerdata data = playerManager.getPlayerdata(player);
					if(data.isVanished()){
						for(Player p : Bukkit.getOnlinePlayers()){
							if(p != player && p.getWorld() == player.getWorld()){
								if(p.getLocation().distance(player.getLocation()) <= 75){
									e.setCancelled(true);
									player.sendMessage(ChatColor.RED + "You cannot damage entities while vanished.");
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onCommandPreproccess(PlayerCommandPreprocessEvent e){
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isJailed()){
			if(!Config.getCommandsUsableInJail().contains(e.getMessage().split(" ")[0].toLowerCase())){
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "You cannot use that command in jail.");
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e){
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isJailed()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.YELLOW + "You will be unjailed in " +
			TimeUtils.getDaysHoursMinutes(punishment.getJailPeriod() + punishment.getJailedTime() - System.currentTimeMillis()) + ".");
			e.getPlayer().sendMessage(ChatColor.YELLOW +"You were jailed for '" + ChatColor.WHITE + punishment.getJailReason() + "'");
		}
		
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent e){
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isJailed()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.YELLOW + "You will be unjailed in " +
			TimeUtils.getDaysHoursMinutes(punishment.getJailPeriod() + punishment.getJailedTime() - System.currentTimeMillis()) + ".");
			e.getPlayer().sendMessage(ChatColor.YELLOW +"You were jailed for '" + ChatColor.WHITE + punishment.getJailReason() + "'");
		}
		
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(afkManager.isAFK(e.getPlayer())){
			afkManager.setAfk(e.getPlayer(), false, true);
		}
		afkManager.setLastActive(e.getPlayer(), System.currentTimeMillis());
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR){
			return;
		}
		Punishment punishment = punishmentManager.getPunishment(e.getPlayer());
		if(punishment.isJailed()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.YELLOW + "You will be unjailed in " +
			TimeUtils.getDaysHoursMinutes(punishment.getJailPeriod() + punishment.getJailedTime() - System.currentTimeMillis()) + ".");
			e.getPlayer().sendMessage(ChatColor.YELLOW +"You were jailed for '" + ChatColor.WHITE + punishment.getJailReason() + "'");
		}
		if(data.isVanished()){
			e.setCancelled(true);
			if(e.getAction() != Action.PHYSICAL) {
				e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getEntity());
		data.setBackLocation(e.getEntity().getLocation(), true);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPickupItem(EntityPickupItemEvent e){
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getEntity();
		Playerdata data = playerManager.getPlayerdata(player);
		if(data.isVanished()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			Playerdata data = playerManager.getPlayerdata((Player) e.getEntity());
			if(data.isVanished()){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(p != player && p.getWorld() == player.getWorld()){
						if(p.getLocation().distance(player.getLocation()) <= 75){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			Playerdata data = playerManager.getPlayerdata((Player) e.getEntity());
			if(data.isVanished()){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(p != player && p.getWorld() == player.getWorld()){
						if(p.getLocation().distance(player.getLocation()) <= 75){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEnityLeash(PlayerLeashEntityEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEnityUnleash(PlayerUnleashEntityEvent e){
		Playerdata data = playerManager.getPlayerdata(e.getPlayer());
		if(data.isVanished()){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + "You can't do that while vanished.");
		}
	}

}
