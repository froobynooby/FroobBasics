package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;

public class Friend_AddCommand extends PlayerCommandExecutor{
	private PlayerManager playerManager;
	private UUIDManager uuidManager;
	
	public Friend_AddCommand(PlayerManager playerManager, UUIDManager uuidManager){
		this.playerManager = playerManager;
		this.uuidManager = uuidManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length <= 1){
			player.sendMessage("/" + cl + " add <player>");
			return false;
		}
		Player friend = Bukkit.getPlayerExact(args[1]);
		Playerdata data = playerManager.getPlayerdata(player);
		Playerdata friendData = playerManager.commandSearchForPlayer(args[1], player, uuidManager);
		if(friendData == null){
			return false;
		}
		if(player == friend){
			player.sendMessage(ChatColor.RED + "That's just sad.");
			return false;
		}
		if(friendData.ignoreList().contains(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "That player is ignoring you.");
			return false;
		}
		if(friendData.getFriends().contains(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You are already friends with that person.");
			return false;
		}
		if(friendData.getFriendRequests().contains(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You have already requested to be that person's friend");
			return false;
		}
		if(data.getFriendRequests().contains(friendData.getUUID())){
			player.sendMessage(ChatColor.RED + "They have already sent you a request, use /friend accept instead.");
			return false;
		}
		
		friendData.addFriendRequest(player.getUniqueId());
		data.addPendingFriend(friendData.getUUID());
		player.sendMessage(ChatColor.YELLOW + "Friend request successfully sent.");
		if(friend != null){
			friend.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.YELLOW + 
					" has requested to be your friend.");
			friend.sendMessage(ChatColor.YELLOW + "To accept type /friend accept " + player.getName());
			friend.sendMessage(ChatColor.YELLOW + "To decline type /friend deny " + player.getName());
		}
		return true;
	}

	@Override
	public String command() {
		
		return "friend add";
	}

	@Override
	public String perm() {
		
		return "froobbasics.friend";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length  == 1) {
			completions.add("add");
		}
		if(args.length == 2) {
			completions = CommandUtils.tabCompletePlayerList(args[1], true, true, uuidManager);
		}
			
		return completions;
	}
	
}
