package com.froobworld.froobbasics.data;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

public class HelpObject {
    private Plugin plugin;
    private Command command;

    public HelpObject(Plugin plugin, Command command) {
        this.plugin = plugin;
        this.command = command;
    }


    public Plugin getPlugin() {

        return plugin;
    }

    public Command getCommand() {

        return command;
    }

    @Override
    public String toString() {

        return ChatColor.RED + command.getName() + ChatColor.WHITE + ": " + command.getDescription();
    }

}
