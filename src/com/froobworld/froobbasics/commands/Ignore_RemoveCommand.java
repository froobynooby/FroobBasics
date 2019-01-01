package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.UUIDUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Ignore_RemoveCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Ignore_RemoveCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 2) {
            player.sendMessage("/" + cl + " remove <player>");
            return false;
        }
        Playerdata ignoring = playerManager.commandSearchForPlayer(args[1], player, uuidManager);
        if (ignoring == null) {
            return false;
        }
        Playerdata data = playerManager.getPlayerdata(player);
        if (!data.ignoreList().contains(ignoring.getUUID())) {
            player.sendMessage(ChatColor.RED + "You are not ignoring that player");
            return false;
        }

        data.unignore(ignoring.getUUID());
        player.sendMessage(ChatColor.YELLOW + "You will no longer ignore that player.");
        return true;
    }

    @Override
    public String command() {

        return "ignore remove";
    }

    @Override
    public String perm() {

        return "froobbasics.ignore";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("remove");
        }
        if (args.length == 2) {
            Playerdata data = playerManager.getPlayerdata(player);
            completions = UUIDUtils.convertToNames(data.ignoreList(), uuidManager, false);

            completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
