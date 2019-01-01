package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Jail;
import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.Mediator;
import com.froobworld.froobbasics.data.Punishment.PunishmentRecord;
import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.data.TaskManager;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PunishmentManager extends TaskManager {
    private Storage storage;
    private Storage jailStorage;

    private ArrayList<Punishment> punishments;
    private ArrayList<Jail> jails;

    private File punishmentRecordsFile;
    private ArrayList<PunishmentRecord> punishmentRecords;

    public PunishmentManager() {
        super(FroobBasics.getPlugin());
    }


    @Override
    public void ini() {
        storage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/punishments");
        punishments = new ArrayList<Punishment>();

        jailStorage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/jails");
        jails = new ArrayList<Jail>();
        for (File file : jailStorage.listFiles()) {
            jails.add(new Jail(file));
        }

        punishmentRecords = new ArrayList<PunishmentRecord>();
        punishmentRecordsFile = new File(FroobBasics.getPlugin().getDataFolder().getPath() + "/punishment-records.yml");
        if (!punishmentRecordsFile.exists()) {
            try {
                punishmentRecordsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(punishmentRecordsFile);
        for (String string : config.getStringList("records")) {
            punishmentRecords.add(PunishmentRecord.deserialise(string));
        }

        addTask(0, 40, new Runnable() {

            @Override
            public void run() {
                task();
            }
        });
    }

    public Punishment commandSearchForPlayer(String name, CommandSender sender, UUIDManager uuidManager) {
        Punishment punishment = null;

        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            punishment = getPunishment(player);
        } else {
            ArrayList<UUIDData> uuids = uuidManager.getUUIDData(name);
            if (uuids.size() > 1) {
                sender.sendMessage(ChatColor.RED + "There are multiple people who last played with that name:");
                sender.sendMessage(PageUtils.toString(uuids));
                sender.sendMessage(ChatColor.RED + "Please use their UUID in place of their name.");
                return null;
            }
            if (uuids.size() == 1) {
                punishment = getPunishment(uuids.get(0).getUUID());
            }
        }
        if (punishment == null) {
            UUID uuid = null;
            try {
                uuid = UUID.fromString(name);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
                return null;
            }
            if (uuid != null) {
                punishment = getPunishment(uuid);
            } else {
                sender.sendMessage(ChatColor.RED + "A player with that UUID could not be found.");
                return null;
            }
        }
        if (punishment == null) {
            sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
            return null;
        }

        return punishment;
    }

    public Punishment getPunishment(UUID uuid) {
        Punishment punishment = null;
        for (Punishment p : punishments) {
            if (p.getUUID().equals(uuid)) {
                punishment = p;
            }
        }
        if (punishment == null) {
            File file = storage.getFile(uuid.toString() + ".yml");
            if (file.exists()) {
                punishment = new Punishment(this, file);
                punishments.add(punishment);
            }
        }
        if (punishment == null) {
            punishment = new Punishment(this, storage.getFile(uuid.toString() + ".yml"), uuid);
            punishments.add(punishment);
        }

        return punishment;
    }

    public Punishment getPunishment(Player player) {

        return getPunishment(player.getUniqueId());
    }

    public Jail getJail(String name) {
        for (Jail jail : jails) {
            if (jail.getName().equalsIgnoreCase(name)) {
                return jail;
            }
        }

        return null;
    }

    public ArrayList<Jail> getJails() {

        return jails;
    }

    public void deleteJail(String name) {
        Jail jail = getJail(name);
        if (jail != null) {
            jail.getFile().delete();
            jails.remove(jail);
        }
    }

    public void createJail(Player player, String name, double radius) {
        jails.add(new Jail(jailStorage.getFile(name + ".yml"), name, player, radius));
    }

    public ArrayList<PunishmentRecord> getPunishmentRecords() {

        return punishmentRecords;
    }

    public void addPunishmentRecords(PunishmentRecord record) {
        punishmentRecords.add(record);
        ArrayList<String> strings = new ArrayList<String>();
        for (PunishmentRecord r : punishmentRecords) {
            strings.add(r.serialise());
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(punishmentRecordsFile);
        config.set("records", strings);
        try {
            config.save(punishmentRecordsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void task() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Punishment punishment = getPunishment(player);
            if (punishment.isMuted()) {
                if (punishment.getMutedTime() + punishment.getMutePeriod() < System.currentTimeMillis()) {
                    punishment.unmute(new Mediator(null));
                    player.sendMessage(ChatColor.YELLOW + "Your mute has expired. You can now talk in chat.");
                }
            }
            if (punishment.isJailed()) {
                if (punishment.getJail() != null) {
                    if (!punishment.getJail().inJail(player.getLocation())) {
                        player.teleport(punishment.getJail().getLocation());
                    }
                }
                if (punishment.getJailedTime() + punishment.getJailPeriod() < System.currentTimeMillis()) {
                    punishment.unjail(new Mediator(null));
                    player.sendMessage(ChatColor.YELLOW + "Your jail period is over. You can now leave jail.");
                }
            }
        }
    }

}
