package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.ContainerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends ContainerCommandExecutor {

    public FriendCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        addSubCommand(new Friend_ListCommand(playerManager, uuidManager), 0, "list", "/friend list");
        addSubCommand(new Friend_AddCommand(playerManager, uuidManager), 0, "add", "/friend add <player>");
        addSubCommand(new Friend_RemoveCommand(playerManager, uuidManager), 0, "remove", "/friend remove <player>");
        addSubCommand(new Friend_RequestsCommand(playerManager, uuidManager), 0, "requests", "/friend requests");
        addSubCommand(new Friend_AcceptCommand(playerManager, uuidManager), 0, "accept", "/friend accept <player>");
        addSubCommand(new Friend_DenyCommand(playerManager, uuidManager), 0, "deny", "/friend deny <player>");
        addSubCommand(new Friend_TogglechatCommand(playerManager), 0, "togglechat", "/friend togglechat");
    }

    @Override
    public String command() {

        return "friend";
    }

    @Override
    public String perm() {

        return "froobbasics.friend";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("list");
            completions.add("add");
            completions.add("remove");
            completions.add("requests");
            completions.add("accept");
            completions.add("deny");
            completions.add("togglechat");
            completions = StringUtil.copyPartialMatches(args[0], completions, new ArrayList<String>(completions.size()));

            return completions;
        } else {
            for (SubCommand subCommand : getSubCommands()) {
                if (args.length > subCommand.getArgIndex() + 1) {
                    if (subCommand.getArg().equalsIgnoreCase(args[subCommand.getArgIndex()])) {
                        return subCommand.getExecutor().tabCompletions(sender, command, cl, args);
                    }
                }
            }
        }

        return completions;
    }

}
