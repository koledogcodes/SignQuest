package me.koledogcodes.signquests.events;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.ObjectiveDataFile;
import me.koledogcodes.signquests.configs.PlayerDataFile;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class SignQuestObjectiveEvents implements Listener {
	
	private SignQuest plugin;
	public SignQuestObjectiveEvents(SignQuest i) {
		plugin = i;
	}
	
	private HashMap<Player, PlayerDataFile> dataFile = new HashMap<Player, PlayerDataFile>();
	SignQuestHandler handler = new SignQuestHandler(plugin);
	
	//TODO Reach Objective [PlayerMoveEvent]
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()){
			return;
		}
	
		if (ObjectiveDataFile.getCustomConfig().getString(handler.parseLocationToString(player.getLocation())) == null){ return; }
		dataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		if (dataFile.get(player).getConfig().getString(ObjectiveDataFile.getCustomConfig().getString(handler.parseLocationToString(player.getLocation())) + ".objective.reach") != null){ return; }
		dataFile.get(player).getConfig().set(ObjectiveDataFile.getCustomConfig().getString(handler.parseLocationToString(player.getLocation())) + ".objective.reach", true);
		dataFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Reach location task for quest &e" + ObjectiveDataFile.getCustomConfig().getString(handler.parseLocationToString(player.getLocation())) + "&7 achieved!");
	}
	
	//TODO Say Objective [AsyncPlayerChatEvent]
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if (ObjectiveDataFile.getCustomConfig().getString("(chat) " + ChatColor.stripColor(e.getMessage()).toLowerCase()) == null){ return; }
		dataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		if (dataFile.get(player).getConfig().getString(ObjectiveDataFile.getCustomConfig().getString("(chat) " + ChatColor.stripColor(e.getMessage()).toLowerCase()) + ".objective.say") != null){ return; }
		
		e.setCancelled(true);
		dataFile.get(player).getConfig().set(ObjectiveDataFile.getCustomConfig().getString("(chat) " + ChatColor.stripColor(e.getMessage()).toLowerCase()) + ".objective.say", true);
		dataFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Player chat task for quest &e" + ObjectiveDataFile.getCustomConfig().getString("(chat) " + ChatColor.stripColor(e.getMessage()).toLowerCase()) + "&7 achieved!");
	}
	
	//TODO Kill Objective [AsyncPlayerChatEvent]
	@EventHandler
	public void onPlayerKillEntity(EntityDeathEvent e){
		if (e.getEntity().getKiller() instanceof Player){ return; }
		if (ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name().toUpperCase()) == null){ return; }
		
		Player player = (Player) e.getEntity().getKiller();
		dataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		handler.questFile.put(player, new QuestDataFile(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name())));
		
		if (dataFile.get(player).getConfig().getString(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills") == null){
			dataFile.get(player).getConfig().set(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills", 1);
			dataFile.get(player).saveConfig();
			return;
		}
		
		if (dataFile.get(player).getConfig().getString(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills") != null){
			if (dataFile.get(player).getConfig().getInt(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills") >= (handler.questFile.get(player).getConfig().getInt("Objective.kill.number-of-kills") - 1)){
				dataFile.get(player).getConfig().set(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.kill", true);
				dataFile.get(player).saveConfig();
				ChatUtili.sendTranslatedMessage(player, "&7Mob kill task for quest &e" + ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + "&7 achieved!");
			}
			else {
			dataFile.get(player).getConfig().set(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills", dataFile.get(player).getConfig().getInt(ObjectiveDataFile.getCustomConfig().getString("(kill) " + e.getEntity().getType().name()) + ".objective.mob-kills") + 1);
			dataFile.get(player).saveConfig();
			}
		}
	}
}
