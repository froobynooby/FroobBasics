package com.froobworld.froobbasics.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.utils.LocationUtils;
import com.froobworld.frooblib.utils.TeleportUtils;
import com.froobworld.frooblib.utils.TimeUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class Playerdata {
	private PlayerManager playerManager;
	private File file;
	private YamlConfiguration config;
	
	private UUID uuid;
	private String lastIp;
	private long lastPlayed;
	private long firstJoined;
	
	private ArrayList<Mail> mail;
	private ArrayList<UUID> ignoring;
	
	private ArrayList<UUID> friends;
	private ArrayList<UUID> pendingFriends;
	private ArrayList<UUID> friendRequests;
	private boolean friendsOnlyChat;
	
	private boolean teleportFriendsDisabled;
	private boolean teleportRequestsDisabled;
	
	private boolean vanished;
	private Location backLocation;
	private Location vanishLocation;

	public Playerdata(PlayerManager playerManager, File file) {
		this.playerManager = playerManager;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		load();
	}
	
	public Playerdata(PlayerManager playerManager,File file, Player player) {
		this.playerManager = playerManager;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		
		uuid = player.getUniqueId();
		lastIp = player.getAddress().toString();
		lastPlayed = System.currentTimeMillis();
		firstJoined = System.currentTimeMillis();
		
		mail = new ArrayList<Mail>();
		ignoring = new ArrayList<UUID>();
		
		friends = new ArrayList<UUID>();
		pendingFriends = new ArrayList<UUID>();
		friendRequests = new ArrayList<UUID>();
		friendsOnlyChat = false;
		
		teleportFriendsDisabled = false;
		teleportRequestsDisabled = false;
		
		vanished = false;
		backLocation = null;
		vanishLocation = null;
		
		save();
	}
	
	
	public void load() {
		uuid = UUID.fromString(config.getString("uuid"));
		lastIp = config.getString("info.ip");
		lastPlayed = config.getLong("info.last-played");
		firstJoined = config.getLong("info.first-join");
		
		friends = new ArrayList<UUID>();
		pendingFriends = new ArrayList<UUID>();
		friendRequests = new ArrayList<UUID>();
		for(String string : config.getStringList("friends.friends")) {
			friends.add(UUID.fromString(string));
		}
		for(String string : config.getStringList("friends.pending")) {
			pendingFriends.add(UUID.fromString(string));
		}
		for(String string : config.getStringList("friends.requests")) {
			friendRequests.add(UUID.fromString(string));
		}
		friendsOnlyChat = config.getBoolean("friends.chat");
		
		ignoring = new ArrayList<UUID>();
		for(String string : config.getStringList("ignoring")) {
			ignoring.add(UUID.fromString(string));
		}
		
		teleportFriendsDisabled = config.getBoolean("teleport-disabled.friends");
		teleportRequestsDisabled = config.getBoolean("teleport-disabled.requests");
		
		vanished = config.getBoolean("vanish.vanished");
		vanishLocation = LocationUtils.deserialiseLocation(config.getString("vanish.location"));
		
		backLocation = LocationUtils.deserialiseLocation(config.getString("back-location"));
		
		mail = new ArrayList<Mail>();
		for(String string : config.getStringList("mail")) {
			mail.add(Mail.deserialiseMail(string));
		}
	}
	
	public void makeConfig() {
		config.set("uuid", uuid.toString());
		config.set("info.ip", lastIp);
		config.set("info.last-played", lastPlayed);
		config.set("info.first-join", firstJoined);

		ArrayList<String> friendsString = new ArrayList<String>();
		ArrayList<String> pendingFriendsString = new ArrayList<String>();
		ArrayList<String> friendRequestsString = new ArrayList<String>();
		for(UUID u : friends) {
			friendsString.add(u.toString());
		}
		for(UUID u : pendingFriends) {
			pendingFriendsString.add(u.toString());
		}
		for(UUID u : friendRequests) {
			friendRequestsString.add(u.toString());
		}
		
		config.set("friends.friends", friendsString);
		config.set("friends.pending", pendingFriendsString);
		config.set("friends.requests", friendRequestsString);
		config.set("friends.chat", friendsOnlyChat);
		
		ArrayList<String> ignoringString = new ArrayList<String>();
		for(UUID u : ignoring) {
			ignoringString.add(u.toString());
		}
		
		config.set("ignoring", ignoringString);
		
		config.set("teleport-disabled.friends", teleportFriendsDisabled);
		config.set("teleport-disabled.requests", teleportRequestsDisabled);
		
		config.set("vanish.vanished", vanished);
		config.set("vanish.location", LocationUtils.serialiseLocation(vanishLocation));
		
		config.set("back-location", LocationUtils.serialiseLocation(backLocation));
		
		ArrayList<String> serialisedMail = new ArrayList<String>();
		for(Mail m : mail) {
			serialisedMail.add(Mail.serialiseMail(m));
		}
		config.set("mail", serialisedMail);
	}
	
	public UUID getUUID() {
		
		return uuid;
	}
	
	public String getLastIp() {
		
		return lastIp;
	}
	
	public long getLastPlayed() {
		
		return lastPlayed;
	}
	
	public long getFirstJoined() {
		
		return firstJoined;
	}
	
	public ArrayList<Mail> getMail() {
		
		return mail;
	}
	
	public ArrayList<UUID> ignoreList() {
		
		return ignoring;
	}
	
	public ArrayList<UUID> getFriends() {
		
		return friends;
	}
	
	public ArrayList<UUID> getPendingFriends() {
		
		return pendingFriends;
	}
	
	public ArrayList<UUID> getFriendRequests() {
		
		return friendRequests;
	}
	
	public boolean isFriendsOnlyChat() {
		
		return friendsOnlyChat;
	}
	
	public boolean isVanished() {
		
		return vanished;
	}
	
	public boolean isTeleportFriendsDisabled() {
		
		return teleportFriendsDisabled;
	}
	
	public boolean isTeleportRequestsDisabled() {
		
		return teleportRequestsDisabled;
	}
	
	public Location getVanishLocation() {
		
		return vanishLocation;
	}
	
	public Location getBackLocation() {
		
		return backLocation;
	}
	
	public void update(Player player) {
		lastPlayed = System.currentTimeMillis();
		lastIp = player.getAddress().toString();
		playerManager.addToSaveQueue(this);
	}
	
	public void sendMail(Mail m) {
		mail.add(m);
		playerManager.addToSaveQueue(this);
	}
	
	public void clearMail() {
		mail.clear();
		playerManager.addToSaveQueue(this);
	}
	
	public void ignore(UUID u) {
		if(!ignoring.contains(u)) {
			ignoring.add(u);
			playerManager.addToSaveQueue(this);
		}
	}
	
	public void unignore(UUID u) {
		if(ignoring.contains(u)) {
			ignoring.remove(u);
			playerManager.addToSaveQueue(this);
		}
	}
	
	public void addFriendRequest(UUID uuid) {
		friendRequests.add(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void removeFriendRequest(UUID uuid) {
		friendRequests.remove(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void addPendingFriend(UUID uuid) {
		pendingFriends.add(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void removePendingFriend(UUID uuid) {
		pendingFriends.remove(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void addFriend(UUID uuid) {
		friends.add(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void removeFriend(UUID uuid) {
		friends.remove(uuid);
		playerManager.addToSaveQueue(this);
	}
	
	public void toggleFriendsOnlyChat() {
		friendsOnlyChat = !friendsOnlyChat;
		playerManager.addToSaveQueue(this);
	}
	
	public void toggleTeleportFriends() {
		teleportFriendsDisabled = !teleportFriendsDisabled;
		playerManager.addToSaveQueue(this);
	}
	
	public void toggleTeleportRequests() {
		teleportRequestsDisabled = !teleportRequestsDisabled;
		playerManager.addToSaveQueue(this);
	}
	
	public void toggleVanish(Player player) {
		vanished = !vanished;
		if(vanished) {
			vanishLocation = player.getLocation();
		}else {
			player.teleport(TeleportUtils.findSafeTeleportLocation(vanishLocation));
		}
		playerManager.addToSaveQueue(this);
	}
	
	public void setBackLocation(Location location, boolean overrideVanish){
		if(vanished && !overrideVanish){
			return;
		}
		backLocation = location;
		playerManager.addToSaveQueue(this);
	}
	
	public void save() {
		makeConfig();
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class Mail{
		private UUID sender;
		private String message;
		private long timeSent;
		
		public Mail(UUID sender, String message) {
			this.sender = sender;
			this.message = message;
			this.timeSent = System.currentTimeMillis();
		}
		
		public Mail(UUID sender, long timeSent, String message) {
			this.sender = sender;
			this.message = message;
			this.timeSent = timeSent;
		}
		
		
		public UUID getSender() {
			
			return sender;
		}
		
		public long getTimeSent() {
			
			return timeSent;
		}
		
		public String getMessage() {
			
			return message;
		}
		
		public static String serialiseMail(Mail mail) {
			
			return mail.getSender() + ";" + mail.getTimeSent() + ";" + mail.getMessage();
		}
		
		public static Mail deserialiseMail(String string) {
			String[] split = string.split(";");
			if(split.length < 3) {
				return null;
			}
			
			return new Mail(UUID.fromString(split[0]), Long.parseLong(split[1]),
					StringUtils.join(Arrays.copyOfRange(split, 2, split.length), ";"));
		}
		
		public String toString(UUIDManager uuidManager) {
			
			return ChatColor.RED + uuidManager.getUUIDData(sender).getLastName() + ChatColor.RESET 
					+ ": " + message + " (" + (TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - timeSent) + " ago)");
		}
	}
}
