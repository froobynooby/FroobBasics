package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Punishment;
import com.froobworld.froobbasics.data.Punishment.Mediator;
import com.froobworld.froobbasics.managers.PunishmentManager;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnbanCommand extends CommandExecutor {
    private PunishmentManager punishmentManager;
    private UUIDManager uuidManager;

    public UnbanCommand(PunishmentManager punishmentManager, UUIDManager uuidManager) {
        this.punishmentManager = punishmentManager;
        this.uuidManager = uuidManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/" + cl + " <player>");
            return false;
        }
        Punishment punishment = punishmentManager.commandSearchForPlayer(args[0], sender, uuidManager);
        if (punishment == null) {
            sender.sendMessage(ChatColor.RED + "If the ban is very old, you may need to use /pardon instead.");
            return false;
        }
        if (!punishment.isBanned()) {
            sender.sendMessage(ChatColor.RED + "That player is not banned.");
            return false;
        }

        Mediator mediator = (sender instanceof Player) ? new Mediator(((Player) sender).getUniqueId()) : new Mediator(null);
        punishment.unban(mediator);
        Bukkit.getBanList(Type.NAME).pardon(uuidManager.getUUIDDataFull(punishment.getUUID()).getLastName());
        sender.sendMessage(ChatColor.YELLOW + "Player unbanned.");
        return true;
    }

    @Override
    public String command() {

        return "unban";
    }

    @Override
    public String perm() {

        return "froobbasics.unban";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            return CommandUtils.tabCompletePlayerList(args[0], true, false, uuidManager);
        }

        return completions;
    }

}
