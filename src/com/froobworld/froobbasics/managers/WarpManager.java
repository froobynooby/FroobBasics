package com.froobworld.froobbasics.managers;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Warp;
import com.froobworld.frooblib.data.Manager;
import com.froobworld.frooblib.data.Storage;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class WarpManager extends Manager {
    private Storage storage;

    private ArrayList<Warp> warps;

    @Override
    public void ini() {
        storage = new Storage(FroobBasics.getPlugin().getDataFolder().getPath() + "/warps");

        warps = new ArrayList<Warp>();
        for (File file : storage.listFiles()) {
            warps.add(new Warp(file));
        }
    }

    public ArrayList<Warp> getWarps() {

        return warps;
    }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }

        return null;
    }

    public void createWarp(String name, Player player) {
        warps.add(new Warp(storage.getFile(name + ".yml"), name, player.getLocation(), player.getUniqueId()));
    }

    public void deleteWarp(String name) {
        Warp warp = getWarp(name);
        if (warp != null) {
            warp.getFile().delete();
            warps.remove(warp);
        }
    }

}
