package me.koledogcodes.signquests.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class setQuestRedeemMessageCommand implements CommandExecutor {

	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	private HashMap<CommandSender, StringBuilder> builder = new HashMap<CommandSender, StringBuilder>();
	
	SignQuestHandler handler;
	private SignQuest plugin;
	public setQuestRedeemMessageCommand(SignQuest i) {
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
		
		if (args.length == 0){
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " <quest> <add|remove> <message>");
			return true;
		}
		
		if (args.length == 1){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " " + args[0].toLowerCase() + " <add|remove> <message>");
			
			return true;
		}
		
		if (args.length == 2){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("add")){
				ChatUtili.sendTranslatedMessage(sender, "&cPlease provide a message to add.");
			}
			else if (args[1].equalsIgnoreCase("remove")){
				ChatUtili.sendTranslatedMessage(sender, "&cPlease provide a message line to remove.");
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			
			return true;
		}
		
		if (args.length == 3){
			if (args[1].equalsIgnoreCase("remove")){
				try {
					handler.removeQuestRedeemMessage(sender, args[0].toLowerCase(), Integer.parseInt(args[2]));
				}
				catch (Exception e){
					ChatUtili.sendTranslatedMessage(sender, "&cInvalid message line suppilied.");
				}
				return true;
			}
		}
		
		if (args.length >= 3){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("add")){
				builder.put(sender, new StringBuilder());
				
				for (i.put(sender, 2); i.get(sender) < args.length; i.put(sender, i.get(sender) + 1)){
					builder.get(sender).append(args[i.get(sender)] + " ");
				}
				
				handler.addQuestRedeemMessage(sender, args[0].toLowerCase(), handler.colorConvert(builder.get(sender).toString()));
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			return true;
		}
		
		
		return false;
	}

}
