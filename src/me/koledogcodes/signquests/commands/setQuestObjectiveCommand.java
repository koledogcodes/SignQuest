package me.koledogcodes.signquests.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class setQuestObjectiveCommand implements CommandExecutor {

	//private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	//private HashMap<CommandSender, StringBuilder> builder = new HashMap<CommandSender, StringBuilder>();
	
	SignQuestHandler handler;
	private SignQuest plugin;
	public setQuestObjectiveCommand(SignQuest i) {
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
			ChatUtili.sendTranslatedMessage(sender, "&cYou must be ingame.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0){
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " <quest> <item>");
			return true;
		}
		
		if (args.length == 1){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " " + args[0] + " <item>");			
			return true;
		}
		
		if (args.length == 2){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			handler.questFile.put(player, new QuestDataFile(args[0].toLowerCase()));
			
			if (args[1].equalsIgnoreCase("item")){
				handler.openQuestItemRequirementInventory(player, args[0].toLowerCase());
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			
			return true;
		}
		
		if (args.length == 3){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			handler.questFile.put(player, new QuestDataFile(args[0].toLowerCase()));
			
			if (args[1].equalsIgnoreCase("item")){
				if (args[2].equalsIgnoreCase("none")){
					handler.questFile.get(player).getConfig().set("Objective.items", new ArrayList<ItemStack>());
					handler.questFile.get(player).saveConfig();
					ChatUtili.sendTranslatedMessage(player, "&7Hand in item objective has been deleted for quest &e" + args[0] + ".");	
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cValid arguments: none");
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
		}
		
		if (args.length >= 4){
			ChatUtili.sendTranslatedMessage(player, "&cToo many arguments.");
			return true;
		}
		
		
		return false;
	}

}
