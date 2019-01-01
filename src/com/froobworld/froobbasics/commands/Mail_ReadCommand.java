package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.data.Playerdata.Mail;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mail_ReadCommand extends PlayerCommandExecutor {
    private static final int PAGE_LENGTH = 5;
    private PlayerManager playerManager;
    private UUIDManager uuidManager;

    public Mail_ReadCommand(PlayerManager playerManager, UUIDManager uuidManager) {
        this.playerManager = playerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Playerdata data = playerManager.getPlayerdata(player);
        ArrayList<Mail> mail = new ArrayList<Mail>();
        for (Mail m : data.getMail()) {
            mail.add(m);
        }
        Collections.reverse(mail);
        int page = 1;
        int pages = PageUtils.pages(mail, PAGE_LENGTH);
        int total = mail.size();

        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                player.sendMessage(Messages.PAGE_NOT_A_NUMBER);
                return false;
            }
        }

        if (total == 0) {
            player.sendMessage(ChatColor.YELLOW + "You have no mail.");
            return true;
        }
        mail = PageUtils.page(mail, PAGE_LENGTH, page);
        if (mail == null) {
            player.sendMessage(Messages.PAGE_NOT_EXIST);
            return false;
        }

        player.sendMessage(ChatColor.YELLOW + "You have " + (total == 1 ?
                "one message." : (total + " messages.")) + " Showing page " + page + " of " + pages);
        for (Mail m : mail) {
            player.sendMessage(m.toString(uuidManager));
        }
        return true;
    }


    @Override
    public String command() {

        return "mail read";
    }


    @Override
    public String perm() {

        return "froobbasics.mail";
    }


    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("read");
        }
        if (args.length == 2) {
            Playerdata data = playerManager.getPlayerdata(player);
            int pages = PageUtils.pages(data.getMail(), PAGE_LENGTH);
            for (int i = 1; i <= pages; i++) {
                completions.add(i + "");
            }
            completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }

}
