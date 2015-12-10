package me.koledogcodes.signquests.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class setQuestDelayCommand implements CommandExecutor {

	SignQuestHandler handler;
	private SignQuest plugin;
	HashMap<Player,Long> delay = new HashMap<Player,Long>();
	public setQuestDelayCommand(SignQuest i) {
		plugin = i;
		handler = new SignQuestHandler(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	if (sender instanceof Player == false){
	return true;
	}
		
	//Setting Variables
	Player p = (Player) sender;
	
	//Permission
	if (sender.hasPermission("quests.admin.*") == false){
		ChatUtili.sendTranslatedMessage(p, "&cYou do not have permission to perform this command."); 
		return true;
	}
	
	if (args.length == 0){
		ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestdelay <quest> <delay>"); 
		return true;
	}
	
	if (args.length == 1){
	if (handler.questExists(args[0].toLowerCase()) == false){
		ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
		return true;
	}	
	ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestdelay <quest> <delay>");
	return true;
	}
	
	if (args.length == 2){
	if (handler.questExists(args[0].toLowerCase()) == false){
		ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
		return true;
	}
	
	try {
			QuestDataFile file = new QuestDataFile(args[0].toLowerCase());
			
			if (args[1].equalsIgnoreCase("max")){
			delay.put(p, Long.MAX_VALUE);
			if (delay.get(p) <= 0){ delay.put(p, delay.get(p) - delay.get(p) - delay.get(p)); }
			file.getConfig().set("delay", Long.parseLong(String.valueOf(delay.get(p))));
			file.saveConfig();
			ChatUtili.sendTranslatedMessage(p, "&7Quest &e" + args[0] + " &7delay set to: &e" + args[1] + ".");
			return true;
			}
			
			else if (args[1].contains("day") || args[1].contains("d")){
			delay.put(p, (long) ((((1000 * 60) * 60) * 24) * Long.parseLong( (args[1].replaceAll("day", "")).replaceAll("d", "") )));
			if (delay.get(p) <= 0){ delay.put(p, delay.get(p) - delay.get(p) - delay.get(p)); }
			file.getConfig().set("delay", Long.parseLong(String.valueOf(delay.get(p))));
			file.saveConfig();
			ChatUtili.sendTranslatedMessage(p, "&7Quest &e" + args[0] + " &7delay set to: &e" + args[1] + ".");
			return true;
			}
			
			else if (args[1].contains("hr") || args[1].contains("h")){
			delay.put(p, (long) (((1000 * 60) * 60) * Long.parseLong( (args[1].replaceAll("hr", "")).replaceAll("h", "") )));
			if (delay.get(p) <= 0){ delay.put(p, delay.get(p) - delay.get(p) - delay.get(p)); }
			file.getConfig().set("delay", Long.parseLong(String.valueOf(delay.get(p))));
			file.saveConfig();
			ChatUtili.sendTranslatedMessage(p, "&7Quest &e" + args[0] + " &7delay set to: &e" + args[1] + ".");
			return true;
			}
			
			else if (args[1].contains("min") || args[1].contains("m")){
			delay.put(p, (long) (((1000 * 60) * Integer.parseInt( (args[1].replaceAll("min", "")).replaceAll("m", "") ))));
			if (delay.get(p) <= 0){ delay.put(p, delay.get(p) - delay.get(p) - delay.get(p)); }
			file.getConfig().set("delay", Long.parseLong(String.valueOf(delay.get(p))));
			file.saveConfig();
			ChatUtili.sendTranslatedMessage(p, "&7Quest &e" + args[0] + " &7delay set to: &e" + args[1] + ".");
			return true;
			}
			
			else if (args[1].contains("sec") || args[1].contains("s")){
			delay.put(p, (long) ((1000 * Integer.parseInt( (args[1].replaceAll("sec", "")).replaceAll("s", "") ))));
			if (delay.get(p) <= 0){ delay.put(p, delay.get(p) - delay.get(p) - delay.get(p)); }
			file.getConfig().set("delay", Long.parseLong(String.valueOf(delay.get(p))));
			file.saveConfig();
			ChatUtili.sendTranslatedMessage(p, "&7Quest &e" + args[0] + " &7delay set to: &e" + args[1] + ".");
			return true;
			}
			
			else {
			ChatUtili.sendTranslatedMessage(p, "&cPlease provide a time format.");	
			}	
	}
	catch (Exception e){
		ChatUtili.sendTranslatedMessage(p, "&cInvalid delay format suppilied.");	
	}
	
	}
	
	if (args.length >= 3){
	ChatUtili.sendTranslatedMessage(p, "&cToo Many Arguments.");
	return true;
	}
	
		return false;
	}


}
