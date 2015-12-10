package me.koledogcodes.signquests.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.api.events.PlayerInteractSignEvent;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class SignQuestSignEvents implements Listener {
	
	private SignQuest plugin;
	public SignQuestSignEvents(SignQuest i) {
		plugin = i;
	}
	
	SignQuestHandler handler = new SignQuestHandler(plugin);
	private final String questSignLine1 = handler.colorConvert("&f[&aQuest&f]");
	private final String questSignLine3 = handler.colorConvert("&b-&eClick&b-");
	
	private final String redeemSignLine1 = handler.colorConvert("&f[&cDeliver&f]");
	private final String redeemSignLine3 = handler.colorConvert("&b-&eClick&b-");
	
	//TODO Read Quest and Redeem Quest
	@EventHandler
	public void onPlayerSignInteractQuest(PlayerInteractSignEvent e){
		Player player = e.getPlayer();
		if (e.getLine(0).equalsIgnoreCase(questSignLine1)){
			if (handler.questExists(ChatColor.stripColor(e.getLine(1)).toLowerCase())){
				if (e.getLine(2).equalsIgnoreCase(questSignLine3)){
					handler.messageQuestMessages(player, ChatColor.stripColor(e.getLine(1)).toLowerCase());
				}
			}
		}
		else if (e.getLine(0).equalsIgnoreCase(redeemSignLine1)){
			if (handler.questExists(ChatColor.stripColor(e.getLine(1)).toLowerCase())){
				if (e.getLine(2).equalsIgnoreCase(redeemSignLine3)){
					if (handler.canCompleteQuest(player, ChatColor.stripColor(e.getLine(1)).toLowerCase())){
						ChatUtili.sendTranslatedMessage(player, "&aYou have completed quest '" + ChatColor.stripColor(e.getLine(1)).toLowerCase() + "'!");
					}
					else {
						return;
					}
				}
			}	
		}
		else {
			
		}
	}
}
