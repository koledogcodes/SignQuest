package me.koledogcodes.signquests.events;

import org.bukkit.event.Listener;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class SignQuestObjectiveEvents implements Listener {
	
	private SignQuest plugin;
	public SignQuestObjectiveEvents(SignQuest i) {
		plugin = i;
	}
	
	//private HashMap<Player, PlayerDataFile> dataFile = new HashMap<Player, PlayerDataFile>();
	SignQuestHandler handler = new SignQuestHandler(plugin);
	
}
