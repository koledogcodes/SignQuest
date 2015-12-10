package me.koledogcodes.signquests.commands;

import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class setSignQuestCommand implements CommandExecutor {
	
	SignQuestHandler handler;
	private SignQuest plugin;
	public setSignQuestCommand(SignQuest i) {
		plugin = i;
		handler = new SignQuestHandler(plugin);
	}
	
	@SuppressWarnings("deprecation")
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
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " <quest name> <quest|redeem>");
			return true;
		}
		
		if (args.length == 1){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			ChatUtili.sendTranslatedMessage(sender, "&4Usage: &c/" + cmd.getName() + " <quest name> <quest|redeem>");
			return true;
		}
		
		if (args.length == 2){
			if (handler.questExists(args[0].toLowerCase()) == false){
				ChatUtili.sendTranslatedMessage(sender, "&c&lSorry, &7quest &e" + args[0].toLowerCase() + " &7does not exist!");
				return true;
			}
			
			Block wall_sign = player.getTargetBlock((HashSet<Byte>) null, 10);
			if (wall_sign.getState() instanceof Sign == false){ ChatUtili.sendTranslatedMessage(player, "&cPlease look at a sign within 10 meters."); return true;}
			Sign sign = (Sign) wall_sign.getState();
			
			if (args[1].equalsIgnoreCase("quest")){
				sign.setLine(0, handler.colorConvert("&f[&aQuest&f]"));
				sign.setLine(1, handler.colorConvert("&f&l" + args[0].toLowerCase()));
				sign.setLine(2, handler.colorConvert("&b-&eClick&b-"));
				sign.update();
				ChatUtili.sendTranslatedMessage(player, "&7Quest &e" + args[0] + " &7sign has been created!");
			}
			else if (args[1].equalsIgnoreCase("redeem")){
				sign.setLine(0, handler.colorConvert("&f[&cDeliver&f]"));
				sign.setLine(1, handler.colorConvert("&f&l" + args[0].toLowerCase()));
				sign.setLine(2, handler.colorConvert("&b-&eClick&b-"));
				sign.update();
				ChatUtili.sendTranslatedMessage(player, "&7Quest redeem &e" + args[0] + " &7sign has been created!");
			}
			else {
				ChatUtili.sendTranslatedMessage(sender, "&cInvalid arguments!");
			}
			
			return true;
		}
		
		if (args.length >= 3){
			ChatUtili.sendTranslatedMessage(player, "&cToo many arguments.");
			return true;
		}
		
		
		return false;
	}

}
