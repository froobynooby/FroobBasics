package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.commands.VanishCommand;
import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.data.TaskManager;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapCommonAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerManager extends TaskManager {
    private Storage storage;

    private ArrayList<Playerdata> playerdata;
    private ArrayList<Playerdata> saveQueue;

    public PlayerManager() {
        super(FroobBasics.getPlugin());
    }

    @Override
    public void ini() {
        storage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/playerdata");
        playerdata = new ArrayList<Playerdata>();
        saveQueue = new ArrayList<Playerdata>();
        updateVanish();

        addTask(0, 600, new Runnable() {

            @Override
            public void run() {
                task();
            }
        });
    }

    public Playerdata getPlayerdata(UUID uuid) {
        for (Playerdata data : playerdata) {
            if (data.getUUID().equals(uuid)) {
                return data;
            }
        }

        File file = storage.getFile(uuid.toString() + ".yml");
        if (!file.exists()) {
            return null;
        }

        Playerdata data = new Playerdata(this, file);
        playerdata.add(data);
        return data;
    }

    public Playerdata getPlayerdata(Player player) {
        Playerdata data = getPlayerdata(player.getUniqueId());
        if (data == null) {
            data = new Playerdata(this, storage.getFile(player.getUniqueId().toString() + ".yml"), player);
            playerdata.add(data);
        }

        return data;
    }

    public void update(Player player) {
        getPlayerdata(player).update(player);
    }

    public void addToSaveQueue(Playerdata data) {
        if (!saveQueue.contains(data)) {
            saveQueue.add(data);
        }
    }

    public Playerdata commandSearchForPlayer(String name, CommandSender sender, UUIDManager uuidManager) {
        Playerdata data = null;

        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            data = getPlayerdata(player);
        } else {
            ArrayList<UUIDData> uuids = uuidManager.getUUIDData(name);
            if (uuids.size() > 1) {
                sender.sendMessage(ChatColor.RED + "There are multiple people who last played with that name:");
                sender.sendMessage(PageUtils.toString(uuids));
                sender.sendMessage(ChatColor.RED + "Please use their UUID in place of their name.");
                return null;
            }
            if (uuids.size() == 1) {
                data = getPlayerdata(uuids.get(0).getUUID());
            }
        }
        if (data == null) {
            UUID uuid = null;
            try {
                uuid = UUID.fromString(name);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
                return null;
            }
            if (uuid != null) {
                data = getPlayerdata(uuid);
            } else {
                sender.sendMessage(ChatColor.RED + "A player with that UUID could not be found.");
                return null;
            }
        }
        if (data == null) {
            sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
            return null;
        }

        return data;
    }

    public String getPlayerList(Permissible permissible, AfkManager afkManager) {
        String list = "";
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (list != "") {
                list += ", ";
            }
            Playerdata data = getPlayerdata(player);
            list += (data.isVanished() && permissible.hasPermission(VanishCommand.PERM_SEE_VANISH) ?
                    ChatColor.GRAY + "(Hidden)" + ChatColor.RESET : "") + (afkManager.isAFK(player) ?
                    ChatColor.GRAY + "(AFK)" + ChatColor.RESET : "") + player.getDisplayName() + ChatColor.RESET;
        }

        return list;
    }

    public void toggleVanish(Player player) {
        Playerdata data = getPlayerdata(player);
        data.toggleVanish(player);
        if (data.isVanished()) {
            player.sendMessage(ChatColor.YELLOW + "You are now vanished. You will teleport back to this location"
                    + " when you unvanish.");
            Bukkit.broadcast(player.getDisplayName() + ChatColor.YELLOW + " has vanished.", VanishCommand.PERM_SEE_VANISH);
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are no longer vanished.");
            Bukkit.broadcast(player.getDisplayName() + ChatColor.YELLOW + " has unvanished.", VanishCommand.PERM_SEE_VANISH);
        }
        updateVanish();

        final Plugin dynmap = Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (dynmap != null) {
            DynmapCommonAPI api = ((DynmapCommonAPI) dynmap);
            api.assertPlayerInvisibility(player.getName(), data.isVanished(), "FroobBasics");
        }
    }

    public void updateVanish() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Playerdata data = getPlayerdata(player);
            for (Player playero : Bukkit.getOnlinePlayers()) {
                if (data.isVanished()) {
                    if (playero != player && !playero.hasPermission(VanishCommand.PERM_SEE_VANISH)) {
                        playero.hidePlayer(FroobBasics.getPlugin(), player);
                        continue;
                    }
                }
                playero.showPlayer(FroobBasics.getPlugin(), player);
            }
        }
    }

    public void task() {
        for (Playerdata data : saveQueue) {
            data.save();
        }

        saveQueue.clear();
    }

}
