package com.froobworld.froobbasics;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {
    private static int AFK_TIME_FOR_ANNOUNCEMENT;
    private static int AFK_TIME_FOR_KICK;
    private static int RANDOM_TELEPORT_MAX;
    private static int RANDOM_TELEPORT_MAXX;
    private static int RANDOM_TELEPORT_MAXZ;
    private static int RANDOM_TELEPORT_MINX;
    private static int RANDOM_TELEPORT_MINZ;
    private static String FIRST_JOIN_MESSAGE;
    private static String BAN_APPEAL_URL;
    private static ArrayList<String> MOTD;
    private static ArrayList<String> RULES;
    private static ArrayList<String> COMMANDS_USABLE_IN_JAIL;
    private static HashMap<String, Integer> MAX_HOMES;
    private static boolean DISABLE_CREATIVE;
    private static boolean BROADCAST_ENABLED;
    private static long BROADCAST_DELAY;
    private static ArrayList<String> BROADCAST_MESSAGES;
    private File file;

    public Config(FroobBasics froobbasics) {
        file = new File(froobbasics.getDataFolder(), "config.yml");
        if (!file.exists()) {
            createDefaultConfig(froobbasics);
        }
        load();
    }

    public static int getTimeForAfkAnnouncement() {

        return AFK_TIME_FOR_ANNOUNCEMENT;
    }

    public static int getTimeForAfkKick() {

        return AFK_TIME_FOR_KICK;
    }

    public static int getMaxRandomTeleports() {

        return RANDOM_TELEPORT_MAX;
    }

    public static int getRandomTeleportMaxX() {

        return RANDOM_TELEPORT_MAXX;
    }

    public static int getRandomTeleportMaxZ() {

        return RANDOM_TELEPORT_MAXZ;
    }

    public static int getRandomTeleportMinX() {

        return RANDOM_TELEPORT_MINX;
    }

    public static int getRandomTeleportMinZ() {

        return RANDOM_TELEPORT_MINZ;
    }

    public static String getFirstJoinMessage() {

        return FIRST_JOIN_MESSAGE;
    }

    public static String getBanAppealUrl() {

        return BAN_APPEAL_URL;
    }

    public static ArrayList<String> getMotd() {

        return MOTD;
    }

    public static ArrayList<String> getRules() {

        return RULES;
    }

    public static ArrayList<String> getCommandsUsableInJail() {

        return COMMANDS_USABLE_IN_JAIL;
    }

    public static HashMap<String, Integer> getMaxHomes() {

        return MAX_HOMES;
    }

    public static boolean isCreativeDisabled() {

        return DISABLE_CREATIVE;
    }

    public static boolean isBroadcastEnabled() {

        return BROADCAST_ENABLED;
    }

    public static long getBroadcastDelay() {

        return BROADCAST_DELAY;
    }

    public static ArrayList<String> getBroadcastMessages() {

        return BROADCAST_MESSAGES;
    }

    public static int getMaxHomes(Player player) {
        int maxHomes = 1;
        for (String perm : Config.getMaxHomes().keySet()) {
            if (player.hasPermission(perm)) {
                if (Config.getMaxHomes().get(perm) > maxHomes) {
                    maxHomes = Config.getMaxHomes().get(perm);
                }
            }
        }

        return maxHomes;
    }

    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        AFK_TIME_FOR_ANNOUNCEMENT = config.getInt("afk.time-for-announcement");
        AFK_TIME_FOR_KICK = config.getInt("afk.time-for-kick");

        RANDOM_TELEPORT_MAX = config.getInt("random-teleport.max-teleports");
        RANDOM_TELEPORT_MAXX = config.getInt("random-teleport.max-x");
        RANDOM_TELEPORT_MAXZ = config.getInt("random-teleport.max-z");
        RANDOM_TELEPORT_MINX = config.getInt("random-teleport.min-x");
        RANDOM_TELEPORT_MINZ = config.getInt("random-teleport.min-z");

        FIRST_JOIN_MESSAGE = config.getString("first-join-message");
        BAN_APPEAL_URL = config.getString("ban-appeal-url");

        MOTD = (ArrayList<String>) config.getStringList("motd");
        RULES = (ArrayList<String>) config.getStringList("rules");
        COMMANDS_USABLE_IN_JAIL = (ArrayList<String>) config.getStringList("commands-usable-in-jail");

        MAX_HOMES = new HashMap<String, Integer>();
        for (String string : config.getStringList("max-homes")) {
            String[] split = string.split(":");
            MAX_HOMES.put(split[0], Integer.valueOf(split[1]));
        }

        DISABLE_CREATIVE = config.getBoolean("disable-creative");

        BROADCAST_ENABLED = config.getBoolean("broadcast.enabled");
        BROADCAST_DELAY = config.getLong("broadcast.delay");
        BROADCAST_MESSAGES = (ArrayList<String>) config.getStringList("broadcast.messages");
    }

    private void createDefaultConfig(FroobBasics froobbasics) {
        File file = new File(froobbasics.getDataFolder(), "config.yml");
        if (!file.exists()) {
            InputStream link = froobbasics.getClass().getResourceAsStream("resources/config.yml");
            try {
                Files.copy(link, file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                System.out.println("Unable to create default config file.");
            }
        }
    }
}
