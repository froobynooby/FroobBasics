package com.froobworld.froobbasics.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Poll {
    private File file;

    private String name;

    private ArrayList<PollEntry> pollEntries;
    private ArrayList<UUID> voted;

    private long expiryTime;

    public Poll(File file) {
        this.file = file;
        load();
    }


    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        this.name = config.getString("name");
        this.expiryTime = config.getLong("expiry-time");

        pollEntries = new ArrayList<PollEntry>();
        for (String string : config.getStringList("entries")) {
            pollEntries.add(PollEntry.deserialise(string));
        }

        voted = new ArrayList<UUID>();
        for (String string : config.getStringList("voted")) {
            voted.add(UUID.fromString(string));
        }
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("name", name);
        config.set("expiry-time", expiryTime);

        ArrayList<String> pollEntriesStrings = new ArrayList<String>();
        for (PollEntry pollEntry : pollEntries) {
            pollEntriesStrings.add(pollEntry.serialise());
        }
        config.set("entries", pollEntriesStrings);

        ArrayList<String> votedStrings = new ArrayList<String>();
        for (UUID uuid : voted) {
            votedStrings.add(uuid.toString());
        }
        config.set("voted", votedStrings);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {

        return name;
    }

    public long getExpiryTime() {

        return expiryTime;
    }

    public ArrayList<PollEntry> getPollEntries() {

        return pollEntries;
    }

    public PollEntry getPollEntry(String name) {
        for (PollEntry pollEntry : pollEntries) {
            if (pollEntry.getName().equalsIgnoreCase(name)) {
                return pollEntry;
            }
        }

        return null;
    }

    public ArrayList<UUID> getVoted() {

        return voted;
    }

    public void vote(Player player, PollEntry pollEntry) {
        if (!voted.contains(player.getUniqueId())) {
            pollEntry.addVote();
            voted.add(player.getUniqueId());
            save();
        }
    }

    public static class PollEntry {
        private String name;
        private String description;
        private int votes;

        public PollEntry(String name, String description, int votes) {
            this.name = name;
            this.description = description;
            this.votes = votes;
        }

        public static PollEntry deserialise(String string) {
            String[] split = string.split(";");

            return new PollEntry(split[0], split[1], Integer.valueOf(split[2]));
        }

        public String getName() {

            return name;
        }

        public String getDescription() {

            return description;
        }

        public int getVotes() {

            return votes;
        }

        public void addVote() {
            votes = votes + 1;
        }

        public String serialise() {

            return name + ";" + description + ";" + votes;
        }
    }
}
