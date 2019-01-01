package com.froobworld.froobbasics.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class NamesCommand extends CommandExecutor {
    private UUIDManager uuidManager;

    public NamesCommand(UUIDManager uuidManager) {
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/" + cl + " <player>");
            return false;
        }
        ArrayList<UUIDData> uuidData = uuidManager.getUUIDData(args[0]);

        if (uuidData.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }
        for (UUIDData data : uuidData) {
            UUIDData fullData = uuidManager.getUUIDDataFull(data.getUUID());
            sender.sendMessage(ChatColor.YELLOW + data.getLastName() + "(" + data.getUUID().toString() + ") "
                    + "has used " + (fullData.getPreviousNames().size() == 1 ? "one name." : (fullData.getPreviousNames().size() + " names.")));
            sender.sendMessage(PageUtils.toString(fullData.getPreviousNames()));
        }
        return true;
    }

    @Override
    public String command() {

        return "names";
    }

    @Override
    public String perm() {

        return "froobbasics.names";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            return CommandUtils.tabCompletePlayerList(args[0], true, true, uuidManager);
        }

        return completions;
    }


}
