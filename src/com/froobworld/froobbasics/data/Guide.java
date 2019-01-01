package com.froobworld.froobbasics.data;

import org.bukkit.plugin.Plugin;

public class Guide {

	public Guide(Plugin plugin, String name, String perm, Page[] pages) {
		
	}

	
	public class Page {
		private String name;
		private String[] content;
		
		public Page(String name, String[] content) {
			this.name = name;
			this.content = content;
		}
		
		
		public String getName() {
			
			return name;
		}
		
		public String[] getContent() {
			
			return content;
		}
	}
}
