package com.froobworld.froobbasics.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;

public class VanishCommand extends PlayerCommandExecutor {
	private PlayerManager playerManager;
	
	public static final String PERM_SEE_VANISH = "froobbasics.vanish.see";

	public VanishCommand(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		playerManager.toggleVanish(player);
		return true;
	}

	@Override
	public String command() {
		
		return "vanish";
	}

	@Override
	public String perm() {
		
		return "froobbasics.vanish";
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
			
		return completions;
	}

	
}
