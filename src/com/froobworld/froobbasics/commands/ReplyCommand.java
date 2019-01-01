package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.managers.MessageManager;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReplyCommand extends PlayerCommandExecutor {
    private MessageManager messageManager;
    private PlayerManager playerManager;
    private PunishmentManager punishmentManager;

    public ReplyCommand(MessageManager messageManager, PlayerManager playerManager, PunishmentManager punishmentManager) {
        this.messageManager = messageManager;
        this.playerManager = playerManager;
        this.punishmentManager = punishmentManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 1) {
            player.sendMessage("/" + cl + " <message>");
            return false;
        }
        String message = StringUtils.join(args, " ");

        messageManager.sendReply(player, message, playerManager, punishmentManager);
        return true;
    }

    @Override
    public String command() {

        return "reply";
    }

    @Override
    public String perm() {

        return "froobbasics.reply";
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
;