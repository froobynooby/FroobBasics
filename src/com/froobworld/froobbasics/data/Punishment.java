package com.froobworld.froobbasics.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.utils.TimeUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

import net.md_5.bungee.api.ChatColor;

public class Punishment {
	private PunishmentManager punishmentManager;
	
	private File file;
	private YamlConfiguration config;
	private UUID uuid;
	
	private boolean banned,muted,jailed;
	private Mediator bannedBy,mutedBy,jailedBy;
	private String bannedReason,mutedReason,jailedReason;
	private long bannedTime,mutedTime,jailedTime;
	private long mutePeriod,jailPeriod;
	private Jail jail;
	
	private ArrayList<PunishmentRecord> history;
	
	public Punishment(PunishmentManager punishmentManager, File file) {
		this.punishmentManager = punishmentManager;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		
		history = new ArrayList<PunishmentRecord>();
		
		load();
	}
	
	public Punishment(PunishmentManager punishmentManager, File file, UUID uuid) {
		this.punishmentManager = punishmentManager;
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		
		this.uuid = uuid;
		banned = false;
		muted = false;
		jailed = false;
		
		history = new ArrayList<PunishmentRecord>();
		
		save();
	}
	
	
	public void save() {
		config.set("uuid", uuid.toString());
		
		config.set("ban.banned", banned);
		config.set("ban.reason", bannedReason);
		if(bannedBy != null) {
			config.set("ban.mediator", bannedBy.toString());
		}
		config.set("ban.time", bannedTime);
		
		config.set("mute.muted", muted);
		config.set("mute.reason", mutedReason);
		if(mutedBy != null) {
			config.set("mute.mediator", mutedBy.toString());
		}
		config.set("mute.time", mutedTime);
		config.set("mute.period", mutePeriod);
		
		config.set("jail.jailed", jailed);
		config.set("jail.reason", jailedReason);
		if(jailedBy != null) {
			config.set("jail.mediator", jailedBy.toString());
		}
		config.set("jail.time", jailedTime);
		config.set("jail.period", jailPeriod);
		if(jail != null) {
			config.set("jail.jail-name", jail.getName());
		}
		
		ArrayList<String> serialised = new ArrayList<String>();
		for(PunishmentRecord record : history) {
			serialised.add(record.serialise());
		}
		config.set("history", serialised);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
			
	public UUID getUUID() {
		
		return uuid;
	}
	
	public boolean isBanned() {
		
		return banned;
	}
	
	public boolean isMuted() {
		
		return muted;
	}
	
	public boolean isJailed() {
		
		return jailed;
	}
	
	public Mediator getBannedBy() {
		
		return bannedBy;
	}
	
	public Mediator getMutedBy() {
		
		return mutedBy;
	}
	
	public Mediator getJailedBy() {
		
		return jailedBy;
	}
	
	public String getBanReason() {
		
		return bannedReason;
	}
	
	public String getMuteReason() {
		
		return mutedReason;
	}
	
	public long getBannedTime() {
		
		return bannedTime;
	}
	
	public long getMutedTime() {
		
		return mutedTime;
	}
	
	public long getJailedTime() {
		
		return jailedTime;
	}
	
	public long getMutePeriod() {
		
		return mutePeriod;
	}
	
	public long getJailPeriod() {
		
		return jailPeriod;
	}
	
	public Jail getJail() {
		
		return jail;
	}
	
	public String getJailReason() {
		
		return jailedReason;
	}
	
	public void ban(Mediator mediator, String reason) {
		banned = true;
		bannedBy = mediator;
		bannedReason = reason;
		bannedTime = System.currentTimeMillis();
		PunishmentRecord record = new PunishmentRecord(RecordType.BAN, uuid, mediator, bannedTime, 0, reason);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public void mute(Mediator mediator, String reason, long duration) {
		muted = true;
		mutedBy = mediator;
		mutedReason = reason;
		mutePeriod = duration;
		mutedTime = System.currentTimeMillis();
		PunishmentRecord record = new PunishmentRecord(RecordType.MUTE, uuid, mediator, mutedTime, duration, reason);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public void jail(Mediator mediator, String reason, long duration, Jail jail) {
		jailed = true;
		jailedBy = mediator;
		jailedReason = reason;
		jailPeriod = duration;
		this.jail = jail;
		jailedTime = System.currentTimeMillis();
		PunishmentRecord record = new PunishmentRecord(RecordType.JAIL, uuid, mediator, jailedTime, duration, reason);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public void unban(Mediator mediator) {
		banned = false;
		PunishmentRecord record = new PunishmentRecord(RecordType.UNBAN, uuid, mediator, System.currentTimeMillis(), 0, null);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public void unmute(Mediator mediator) {
		muted = false;
		PunishmentRecord record = new PunishmentRecord(RecordType.UNMUTE, uuid, mediator, System.currentTimeMillis(), 0, null);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public void unjail(Mediator mediator) {
		jailed = false;
		PunishmentRecord record = new PunishmentRecord(RecordType.UNJAIL, uuid, mediator, System.currentTimeMillis(), 0, null);
		punishmentManager.addPunishmentRecords(record);
		history.add(record);
		save();
	}
	
	public ArrayList<PunishmentRecord> getHistory(){
		
		return history;
	}
	
	public void load() {
		uuid = UUID.fromString(config.getString("uuid"));
		
		banned = config.getBoolean("ban.banned");
			if(banned) {
			bannedReason = config.getString("ban.reason");
			String banMediator = config.getString("ban.mediator");
			if(banMediator != null) {
				String m = config.getString("ban.mediator");
				bannedBy = m.equalsIgnoreCase("console") ? new Mediator(null):
					new Mediator(UUID.fromString(m));
			}
			bannedTime = config.getLong("ban.time");
		}
		
		muted = config.getBoolean("mute.muted");
		if(muted) {
			mutedReason = config.getString("mute.reason");
			String muteMediator = config.getString("mute.mediator");
			if(muteMediator != null) {
				String m = config.getString("mute.mediator");
				mutedBy = m.equalsIgnoreCase("console") ? new Mediator(null):
					new Mediator(UUID.fromString(m));
			}
			mutedTime = config.getLong("mute.time");
			mutePeriod = config.getLong("mute.period");
		}
		
		jailed = config.getBoolean("jail.jailed");
		if(jailed) {
			jailedReason = config.getString("jail.reason");
			String jailMediator = config.getString("jail.mediator");
			if(jailMediator != null) {
				String m = config.getString("jail.mediator");
				jailedBy = m.equalsIgnoreCase("console") ? new Mediator(null):
					new Mediator(UUID.fromString(m));
			}
			jailedTime = config.getLong("jail.time");
			jailPeriod = config.getLong("jail.period");
			jail = punishmentManager.getJail(config.getString("jail.jail-name"));
		}
		
		history = new ArrayList<PunishmentRecord>();
		for(String string : config.getStringList("history")) {
			history.add(PunishmentRecord.deserialise(string));
		}
	}
	
	
	
	public static class PunishmentRecord{
		private UUID affected;
		
		private RecordType type;
		private long time;
		private String reason;
		private Mediator mediator;
		private long period;
		
		public PunishmentRecord(RecordType type, UUID affected, Mediator mediator, long time, long period, String reason) {
			this.affected = affected;
			this.time = time;
			this.type = type;
			this.reason = reason;
			this.mediator = mediator;
			this.period = period;
		}
		
		
		public String serialise() {
			switch (type) {
			case BAN:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time + ";" + reason;
			case JAIL:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time + ";" + period + ";" + reason;
			case MUTE:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time + ";" + period + ";" + reason;
			case UNBAN:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time;
			case UNJAIL:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time;
			case UNMUTE:
				return type.toString() + ";" + affected.toString() + ";" + mediator.toString() + ";" + time;
			default:
				return "";
			}
		}
		
		public static PunishmentRecord deserialise(String string) {
			String[] split = string.split(";");
			Mediator m = split[2].equalsIgnoreCase("console") ?
					new Mediator(null):new Mediator(UUID.fromString(split[2]));
			switch (split[0]) {
			case "BAN":
				return new PunishmentRecord(RecordType.BAN, UUID.fromString(split[1]),
						m, Long.parseLong(split[3]), 0,
						StringUtils.join(Arrays.copyOfRange(split, 4, split.length)));
			case "JAIL":
				return new PunishmentRecord(RecordType.JAIL, UUID.fromString(split[1]),
						m,Long.parseLong(split[3]), Long.parseLong(split[4]),
						StringUtils.join(Arrays.copyOfRange(split, 5, split.length)));
			case "MUTE":
				return new PunishmentRecord(RecordType.MUTE, UUID.fromString(split[1]),
						m,Long.parseLong(split[3]), Long.parseLong(split[4]),
						StringUtils.join(Arrays.copyOfRange(split, 5, split.length)));
			case "UNBAN":
				return new PunishmentRecord(RecordType.UNBAN, UUID.fromString(split[1]),
						m,Long.parseLong(split[3]), 0, null);
			case "UNJAIL":
				return new PunishmentRecord(RecordType.UNJAIL, UUID.fromString(split[1]),
						m,Long.parseLong(split[3]),0, null);
			case "UNMUTE":
				return new PunishmentRecord(RecordType.UNMUTE, UUID.fromString(split[1]),
						m,Long.parseLong(split[3]), 0, null);
			}
			
			return null;
		}
		
		public String toString(UUIDManager uuidManager) {
			switch (type) {
			case BAN:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was banned by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago for: "
				+ reason;
			case JAIL:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was jailed by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago for: "
				+ reason + " (" + TimeUtils.getDaysHoursMinutes(period) + ")";
			case MUTE:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was muted by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago for: "
				+ reason + " (" + TimeUtils.getDaysHoursMinutes(period) + ")";
			case UNBAN:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was unbanned by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago";
			case UNJAIL:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was unjailed by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago";
			case UNMUTE:
				return ChatColor.RED + uuidManager.getUUIDData(affected).getLastName() + ChatColor.RESET + 
				" was unmuted by " + ChatColor.RED + mediator.getName(uuidManager) + ChatColor.RESET + " " +
				TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - time) + " ago";
			default:
				return "";
			}
		}
		
	}
	
	public static class Mediator {
		private UUID uuid;
		
		public Mediator(UUID uuid) {
			this.uuid = uuid;
		}
		
		
		public String getName(UUIDManager uuidManager) {
			if(uuid == null) {
				return "Console";
			}

			return uuidManager.getUUIDData(uuid).getLastName();
		}
		
		public String toString() {
			if(uuid == null) {
				return "console";
			}
			
			return uuid.toString();
		}
	}
	
	public enum RecordType{
		BAN,
		MUTE,
		JAIL,
		UNBAN,
		UNMUTE,
		UNJAIL
	}
}
