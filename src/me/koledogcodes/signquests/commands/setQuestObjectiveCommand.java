package me.koledogcodes.signquests.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.ObjectiveDataFile;
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
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " <quest> <item|reach|say|kill>");
			return true;
		}
		
		if (args.length == 1){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " " + args[0] + " <item|reach|say|kill>");			
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
			else if (args[1].equalsIgnoreCase("reach")){
				if (handler.questFile.get(player).getConfig().getString("Objective.reach") != null){
				ObjectiveDataFile.getCustomConfig().set(handler.questFile.get(player).getConfig().getString("Objective.reach"), null);
				ObjectiveDataFile.saveCustomConfig();
				handler.questFile.get(player).getConfig().set("Objective.reach", null);
				handler.questFile.get(player).saveConfig();
				}
				
				handler.questFile.get(player).getConfig().set("Objective.reach", handler.parseLocationToString(player.getLocation()));
				handler.questFile.get(player).saveConfig();
				ObjectiveDataFile.getCustomConfig().set(handler.parseLocationToString(player.getLocation()), args[0].toLowerCase());
				ObjectiveDataFile.saveCustomConfig();
				ChatUtili.sendTranslatedMessage(player, "&7Reach objective set at &ecurrent &7location.");
			}
			else if (args[1].equalsIgnoreCase("say")){
				if (handler.questFile.get(player).getConfig().getString("Objective.say") != null){
				ObjectiveDataFile.getCustomConfig().set("(chat) " + handler.questFile.get(player).getConfig().getString("Objective.say"), null);
				ObjectiveDataFile.saveCustomConfig();
				handler.questFile.get(player).getConfig().set("Objective.say", null);
				handler.questFile.get(player).saveConfig();
				}				
				
				SignQuestHandler.eventCommandBypass.put(player, "TASK-CHAT:" + args[0].toLowerCase());
				ChatUtili.sendTranslatedMessage(player, "&aPlease type the 'say' objective in chat.");
			}
			else if (args[1].equalsIgnoreCase("kill")){
				ChatUtili.sendTranslatedMessage(player, "&cPlease provide a mob.");
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
			else if (args[1].equalsIgnoreCase("reach")){
				if (args[2].equalsIgnoreCase("none")){
					ObjectiveDataFile.getCustomConfig().set(handler.questFile.get(player).getConfig().getString("Objective.reach"), null);
					ObjectiveDataFile.saveCustomConfig();
					handler.questFile.get(player).getConfig().set("Objective.reach", null);
					handler.questFile.get(player).saveConfig();
					ChatUtili.sendTranslatedMessage(player, "&7Reach objective has been deleted for quest &e" + args[0] + ".");	
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cValid arguments: none");
				}
			}
			else if (args[1].equalsIgnoreCase("say")){
				if (args[2].equalsIgnoreCase("none")){
					ObjectiveDataFile.getCustomConfig().set("(chat) " + handler.questFile.get(player).getConfig().getString("Objective.say"), null);
					ObjectiveDataFile.saveCustomConfig();
					handler.questFile.get(player).getConfig().set("Objective.say", null);
					handler.questFile.get(player).saveConfig();
					ChatUtili.sendTranslatedMessage(player, "&7Say objective has been deleted for quest &e" + args[0] + ".");	
				}
				else {
					ChatUtili.sendTranslatedMessage(player, "&cValid arguments: none");
				}
			}
			else if (args[1].equalsIgnoreCase("kill")){
				if (args[2].equalsIgnoreCase("none")){
					ObjectiveDataFile.getCustomConfig().set("(kill) " + handler.questFile.get(player).getConfig().getString("Objective.kill.type"), null);
					ObjectiveDataFile.saveCustomConfig();
					handler.questFile.get(player).getConfig().set("Objective.kill", null);
					handler.questFile.get(player).saveConfig();
					ChatUtili.sendTranslatedMessage(player, "&7Kill objective has been deleted for quest &e" + args[0] + ".");
				}
				else {
					try {
						//EntityType.fromName(args[2].toUpperCase());
						ChatUtili.sendTranslatedMessage(player, "&cPlease provide the number of kills.");
					}
					catch (Exception e){
						ChatUtili.sendTranslatedMessage(player, "&cMob &4" + args[2] + " &cis invalid.");
					}
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
		}
		else if (args[1].equalsIgnoreCase("say")){
			if (args[2].equalsIgnoreCase("none")){
				ObjectiveDataFile.getCustomConfig().set(handler.questFile.get(player).getConfig().getString("Objective.sawy"), null);
				ObjectiveDataFile.saveCustomConfig();
				handler.questFile.get(player).getConfig().set("Objective.say", null);
				handler.questFile.get(player).saveConfig();
				ChatUtili.sendTranslatedMessage(player, "&7Say objective has been deleted for quest &e" + args[0] + ".");	
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			
			return true;
		}
		
		if (args.length == 4){
			if (args[1].equalsIgnoreCase("kill")){
				try {		
					handler.questFile.put(player, new QuestDataFile(args[0].toLowerCase()));
					handler.questFile.get(player).getConfig().set("Objective.kill.type", args[2].toUpperCase());
					handler.questFile.get(player).getConfig().set("Objective.kill.number-of-kills", Integer.parseInt(args[3]));
					handler.questFile.get(player).saveConfig();
					ObjectiveDataFile.getCustomConfig().set("(kill) " + args[2].toUpperCase(), args[0].toLowerCase());
					ObjectiveDataFile.saveCustomConfig();
					ChatUtili.sendTranslatedMessage(player, "&7Kill objective has been set for quest &e" + args[0] + ".");	
				}
				catch (Exception e){
					e.printStackTrace();
					ChatUtili.sendTranslatedMessage(player, "&cMob &4" + args[2] + " &cis invalid or number of kills is invalid.");
				}
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			return true;
		}
		
		if (args.length >= 5){
			ChatUtili.sendTranslatedMessage(player, "&cToo many arguments.");
			return true;
		}
		
		if (args.length >= 4){
			ChatUtili.sendTranslatedMessage(player, "&cToo many arguments.");
			return true;
		}
		
		
		return false;
	}

}
