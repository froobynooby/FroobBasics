package com.froobworld.froobbasics.listeners;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;

public class DiscordListener {
	
    @Subscribe(priority = ListenerPriority.MONITOR)
    public void onMessageProcessEvent(GameChatMessagePreProcessEvent e){
    	if(e.getMessage().endsWith("§S")){
    	   e.setCancelled(true);
    	}
    }
    

}
