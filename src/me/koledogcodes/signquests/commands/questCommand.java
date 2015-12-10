package me.koledogcodes.signquests.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class questCommand implements CommandExecutor {

	
	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	SignQuestHandler handler;
	private SignQuest plugin;
	public questCommand(SignQuest i) {
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
			ChatUtili.sendTranslatedMessage(sender, "&f------- &cQuest Help Page &f-------");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest create <quest> &f- &eCreates a new quest.");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest delete <quest> &f- &eDeletes a current quest.");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest reload  &f- &eReloads the SignQuest plugin.");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest import  &f- &eImports old signquests quests.");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest list  &f- &eList current quests.");
			ChatUtili.sendTranslatedMessage(sender, "&7/quest info <quest> <msg:redeem-msg:reward-cmd:obj>  &f- &eShow certain info of a quest.");
			return true;
		}
		
		if (args.length == 1){
			if (args[0].equalsIgnoreCase("create")){
				ChatUtili.sendTranslatedMessage(sender, "&cPlease provide a quest to create.");
			}
			else if (args[0].equalsIgnoreCase("delete")){
				ChatUtili.sendTranslatedMessage(sender, "&cPlease provide a quest to delete.");
			}
			else if (args[0].equalsIgnoreCase("reload")){
				Bukkit.getServer().getPluginManager().disablePlugin(plugin);
				Bukkit.getServer().getPluginManager().enablePlugin(plugin);
				ChatUtili.sendTranslatedMessage(sender, "&aSignQuests has been reloaded.");
			}
			else if (args[0].equalsIgnoreCase("import")){
				ChatUtili.sendTranslatedMessage(sender, "&aImporting old signquests quests.");
				handler.importOldSignQuests(sender);
				ChatUtili.sendTranslatedMessage(sender, "&aOld signquests quest have been imported.");
			}
			else if (args[0].equalsIgnoreCase("list")){
				sender.sendMessage(handler.colorConvert("&6Quests: &f" + handler.getQuestList()));
			}
			else if (args[0].equalsIgnoreCase("info")){
				ChatUtili.sendTranslatedMessage(sender, "&cPlease provide a quest.");
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cType &4/quest &cto see the help page");
			}
			return true;
		}
		
		if (args.length == 2){
			if (args[0].equalsIgnoreCase("create")){
				handler.createQuest(sender, args[1].toLowerCase());
			}
			else if (args[0].equalsIgnoreCase("delete")){
				handler.deleteQuest(sender, args[1].toLowerCase());
			}
			else if (args[0].equalsIgnoreCase("info")){
				if (handler.questExists(args[1].toLowerCase()) == false){
					ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[1].toLowerCase() + " &7does not exist!");
					return true;
				}	

				ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/quest info " + args[1] + " <msg:redeem-msg:reward-cmd:obj>");
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cType &4/quest &cto see the help page");
			}
			return true;
		}
		
		if (args.length == 3){
			if (args[0].equalsIgnoreCase("info")){
				if (handler.questExists(args[1].toLowerCase()) == false){
					ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[1].toLowerCase() + " &7does not exist!");
					return true;
				}	
				
				handler.questFile.put(sender, new QuestDataFile(args[1].toLowerCase()));
				
				if (args[2].equalsIgnoreCase("msg")){
					ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' messages &a(Message lines)");
					for (i.put(sender, 0); i.get(sender) < handler.questFile.get(sender).getConfig().getStringList("quest-messages").size(); i.put(sender, i.get(sender) + 1)){
						ChatUtili.sendTranslatedMessage(sender, i.get(sender) + ". " + handler.questFile.get(sender).getConfig().getStringList("quest-messages").get(i.get(sender)));
					}
				}
				else if (args[2].equalsIgnoreCase("redeem-msg")){
					ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' redeem messages &a(Message lines)");
					for (i.put(sender, 0); i.get(sender) < handler.questFile.get(sender).getConfig().getStringList("redeem-messages").size(); i.put(sender, i.get(sender) + 1)){
						ChatUtili.sendTranslatedMessage(sender, i.get(sender) + ". " + handler.questFile.get(sender).getConfig().getStringList("redeem-messages").get(i.get(sender)));
					}
				}
				else if (args[2].equalsIgnoreCase("reward-cmd")){
					ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' reward commands &a(Command lines)");
					for (i.put(sender, 0); i.get(sender) < handler.questFile.get(sender).getConfig().getStringList("reward-commands").size(); i.put(sender, i.get(sender) + 1)){
						ChatUtili.sendTranslatedMessage(sender, i.get(sender) + ". " + handler.questFile.get(sender).getConfig().getStringList("reward-commands").get(i.get(sender)));
					}
				}
				else if (args[2].equalsIgnoreCase("obj")){
					ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' currently active objectives &c(Below)");
					if (handler.questFile.get(sender).getConfig().getString("Objective.reach") != null){
						ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' reach objective location: &e" + handler.questFile.get(sender).getConfig().getString("Objective.reach"));
					}
					if (handler.questFile.get(sender).getConfig().getString("Objective.say") != null){
						ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' say objective text: &e" + handler.questFile.get(sender).getConfig().getString("Objective.say"));
					}
					if (handler.questFile.get(sender).getConfig().getString("Objective.kill") != null){
						ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' kill objective mob: &e" + handler.questFile.get(sender).getConfig().getString("Objective.kill.number-of-kills") + "x " + handler.questFile.get(sender).getConfig().getString("Objective.kill.type"));
					}
					if (handler.questFile.get(sender).getConfig().getString("Objective.items") != null){
						if (!handler.questFile.get(sender).getConfig().getString("Objective.items").isEmpty()){
							ChatUtili.sendTranslatedMessage(sender, "&7Quest '&a" + args[1] + "&7' gather objective status: &e(Active)");
						}
					}
				}
				else {
					ChatUtili.sendTranslatedMessage(sender, "&cType &4/quest &cto see the help page");
				}
				
			}
			else {
			ChatUtili.sendTranslatedMessage(sender, "&cToo many arguments.");
			}
			return true;
		}
		
		return false;
	}

}
