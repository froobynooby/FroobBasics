package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Home;
import com.froobworld.frooblib.data.Manager;
import com.froobworld.frooblib.data.Storage;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeManager extends Manager {
    private Storage storage;

    private HashMap<Player, ArrayList<Home>> homes;

    @Override
    public void ini() {
        storage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/homes");
        homes = new HashMap<Player, ArrayList<Home>>();
    }


    public void loadHomes(Player player) {
        ArrayList<Home> playerHomes = new ArrayList<Home>();
        File directory = storage.getDirectory(player.getUniqueId().toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        for (File file : directory.listFiles()) {
            playerHomes.add(new Home(file));
        }

        homes.put(player, playerHomes);
    }

    public ArrayList<Home> getHomes(Player player) {
        ArrayList<Home> playerHomes = homes.get(player);
        if (playerHomes == null) {
            loadHomes(player);
            playerHomes = homes.get(player);
        }

        return playerHomes;
    }

    public Home getHome(Player player, String name) {
        for (Home home : getHomes(player)) {
            if (home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }

        return null;
    }

    public void setHome(Player player, String name) {
        Home home = getHome(player, name);
        if (home != null) {
            delHome(player, name);
        }
        getHomes(player).add(new Home(storage.getFile(player.getUniqueId().toString() + "/" + name + ".yml"),
                name, player.getLocation()));
    }

    public void delHome(Player player, String name) {
        Home home = getHome(player, name);
        if (home != null) {
            home.getFile().delete();
            getHomes(player).remove(home);
        }
    }

}
