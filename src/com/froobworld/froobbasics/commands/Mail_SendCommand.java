package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Playerdata.Mail;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mail_SendCommand extends PlayerCommandExecutor {
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Mail_SendCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 3) {
            player.sendMessage("/mail send <player> <message>");
            return false;
        }
        Playerdata recipientData = playerManager.commandSearchForPlayer(args[1], player, uuidManager);
        if (recipientData == null) {
            return false;
        }

        recipientData.sendMail(new Mail(player.getUniqueId(),
                StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ")));
        player.sendMessage(ChatColor.YELLOW + "Mail sent.");
        return true;
    }


    @Override
    public String command() {

        return "mail send";
    }


    @Override
    public String perm() {

        return "froobbasics.mail";
    }


    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("send");
        }
        if (args.length == 2) {
            return CommandUtils.tabCompletePlayerList(args[1], true, true, uuidManager);
        }

        return completions;
    }

}
