package me.koledogcodes.signquests.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class setQuestRewardCommand implements CommandExecutor {

	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	private HashMap<CommandSender, StringBuilder> builder = new HashMap<CommandSender, StringBuilder>();
	SignQuestHandler handler;
	private SignQuest plugin;
	HashMap<Player,Long> delay = new HashMap<Player,Long>();
	public setQuestRewardCommand(SignQuest i) {
		plugin = i;
		handler = new SignQuestHandler(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

	//Permission
	if (sender.hasPermission("quests.admin.*") == false){
		ChatUtili.sendTranslatedMessage(sender, "&cYou do not have permission to perform this command."); 
		return true;
	}
		
	if (sender instanceof Player == false){
	return true;
	}
		
	//Setting Variables
	Player p = (Player) sender;
	
	//Permission
	if (p.hasPermission("quests.admin.*") == false){
		ChatUtili.sendTranslatedMessage(p, "&cYou do not have permission to perform this command."); 
		return true;
	}
	
	if (args.length == 0){
		ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestreward <quest> <command|item> [add|remove] [command]"); 
		return true;
	}
	
	if (args.length == 1){
		if (handler.questExists(args[0].toLowerCase()) == false){
			ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
			return true;
		}
		
		ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestreward " + args[0] + " <command|item> [add|remove] [command]");
		return true;
	}
	
	if (args.length == 2){
		if (handler.questExists(args[0].toLowerCase()) == false){
			ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("command")){
			ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestreward " + args[0] + " command <add|remove> <command>");
		}
		else if (args[1].equalsIgnoreCase("item")){
			handler.openQuestRewardItemInventory(p, args[0].toLowerCase());
		}
		else {
			ChatUtili.sendTranslatedMessage(p, "&cInvalid arguments.");
		}
		return true;
	}
	
	if (args.length == 3){
		if (handler.questExists(args[0].toLowerCase()) == false){
			ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("command")){
			if (args[2].equalsIgnoreCase("add")){
				ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestreward " + args[0] + " command add <command>");
			}
			else if (args[2].equalsIgnoreCase("remove")){
				ChatUtili.sendTranslatedMessage(p, "&4Usage: &c/setquestreward " + args[0] + " command remove <command line>");
			}
			else {
				ChatUtili.sendTranslatedMessage(p, "&cInvalid sub argument.");
			}
		}
		else if (args[1].equalsIgnoreCase("item")){
			ChatUtili.sendTranslatedMessage(p, "&cToo Many Arguments.");
		}
		else {
			ChatUtili.sendTranslatedMessage(p, "&cInvalid arguments.");
		}
		return true;
	}
	
	if (args.length >= 4){
		if (handler.questExists(args[0].toLowerCase()) == false){
			ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
			return true;
		}
		
		if (args[1].equalsIgnoreCase("command")){
			if (args[2].equalsIgnoreCase("add")){
				builder.put(p, new StringBuilder());
				for (i.put(p, 3); i.get(p) < args.length; i.put(p, i.get(p) + 1)){
					builder.get(p).append(args[i.get(p)] + " ");
				}
				handler.addQuestRewardCommand(p, args[0].toLowerCase(), builder.get(p).toString());
			}
			else if (args[2].equalsIgnoreCase("remove")){
				try {
					handler.removeQuestRewardCommand(p, args[0].toLowerCase(), Integer.parseInt(args[3]));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(p, "&cInvalid command line.");
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(p, "&cInvalid sub argument.");
			}
		}
		else if (args[1].equalsIgnoreCase("item")){
			ChatUtili.sendTranslatedMessage(p, "&cToo Many Arguments.");
		}
		else {
			ChatUtili.sendTranslatedMessage(p, "&cInvalid arguments.");
		}
		return true;
	}

	
	
		return false;
	}


}
