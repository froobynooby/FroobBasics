package com.froobworld.froobbasics.commands;

import com.froobworld.froobbasics.data.Poll;
import com.froobworld.froobbasics.data.Poll.PollEntry;
import com.froobworld.froobbasics.managers.PollManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TimeUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PollCommand extends PlayerCommandExecutor {
    private PollManager pollManager;

    public PollCommand(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Poll poll = pollManager.getPoll();
        if (poll == null) {
            player.sendMessage(ChatColor.RED + "There is no active poll.");
            return false;
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("vote")) {
                PollEntry pollEntry = poll.getPollEntry(args[1]);
                if (poll.getExpiryTime() < System.currentTimeMillis()) {
                    player.sendMessage(ChatColor.RED + "This poll has expired.");
                    return false;
                }
                if (pollEntry == null) {
                    player.sendMessage(ChatColor.RED + "That is not a valid option.");
                    return false;
                }
                if (poll.getVoted().contains(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You can only vote once in this poll.");
                    return false;
                }
                poll.vote(player, pollEntry);
                player.sendMessage(ChatColor.YELLOW + "Thank you for voting.");
                return true;
            }
        }

        player.sendMessage(ChatColor.RED + "---- " + ChatColor.WHITE + poll.getName() + ChatColor.RED + " ----");
        if (poll.getExpiryTime() < System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "Expired" + ChatColor.WHITE + ": " + TimeUtils.getDaysHoursMinutes(System.currentTimeMillis() - poll.getExpiryTime()) + " ago");
            player.sendMessage(ChatColor.RED + "-- " + ChatColor.WHITE + "Results" + ChatColor.RED + " --");
            for (PollEntry pollEntry : poll.getPollEntries()) {
                player.sendMessage(ChatColor.RED + pollEntry.getName() + ChatColor.WHITE + " received " + pollEntry.getVotes() + " votes");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Expires in" + ChatColor.WHITE + ": " + TimeUtils.getDaysHoursMinutes(poll.getExpiryTime() - System.currentTimeMillis()));
            player.sendMessage(ChatColor.RED + "-- " + ChatColor.WHITE + "Options" + ChatColor.RED + " --");
            for (PollEntry pollEntry : poll.getPollEntries()) {
                player.sendMessage(ChatColor.RED + pollEntry.getName() + ChatColor.WHITE + ": " + pollEntry.getDescription());
                player.sendMessage(ChatColor.GRAY + "/" + cl + " vote " + pollEntry.getName());
            }
        }
        return true;
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (pollManager.getPoll() != null) {
            if (args.length == 1) {
                completions.add("vote");
            }
            if (args.length == 2) {
                for (PollEntry pollEntry : pollManager.getPoll().getPollEntries()) {
                    completions.add(pollEntry.getName());
                }
            }
        }
        completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
        return completions;
    }

    @Override
    public String command() {

        return "poll";
    }

    @Override
    public String perm() {

        return "froobbasics.poll";
    }

}
