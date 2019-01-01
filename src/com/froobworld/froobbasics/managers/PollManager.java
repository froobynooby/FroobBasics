package com.froobworld.froobbasics.managers;

import java.io.File;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.froobbasics.data.Poll;
import com.froobworld.frooblib.data.Manager;

public class PollManager extends Manager {
	
	private Poll poll;

	@Override
	public void ini() {
		File file = new File(FroobBasics.getPlugin().getDataFolder() + "/poll.yml");
		if(file.exists()) {
			poll = new Poll(file);
		}else {
			poll = null;
		}
	}
	
	public Poll getPoll() {
		return poll;
	}

}
