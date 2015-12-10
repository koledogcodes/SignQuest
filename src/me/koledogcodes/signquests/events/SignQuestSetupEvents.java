package me.koledogcodes.signquests.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.api.events.PlayerInteractSignEvent;
import me.koledogcodes.signquests.configs.ObjectiveDataFile;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;

public class SignQuestSetupEvents implements Listener {

	private HashMap<Player, QuestDataFile> questFile = new HashMap<Player, QuestDataFile>();
	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	private HashMap<Player, List<ItemStack>> questItems = new HashMap<Player, List<ItemStack>>();
	SignQuestHandler handler;
	private SignQuest plugin;
	public SignQuestSetupEvents(SignQuest i) {
		plugin = i;
		handler = new SignQuestHandler(plugin);
	}
	
	//TODO Save Quest Item 
	@EventHandler
	public void onQuestInventoryClose(InventoryCloseEvent e){
		Player player = (Player) e.getPlayer();
		if (e.getInventory().getName().split(":").length != 2){ return; }
		if (handler.questExists(e.getInventory().getName().split(":")[0]) == false){ return; }
		questItems.put(player, new ArrayList<ItemStack>());
		questFile.put(player, new QuestDataFile(e.getInventory().getName().split(":")[0]));
		
		if (e.getInventory().getName().split(":")[1].equalsIgnoreCase("ireq")){
		for (i.put(player, 0); i.get(player) < e.getInventory().getSize(); i.put(player, i.get(player) + 1)){
			if (e.getInventory().getItem(i.get(player)) == null || e.getInventory().getItem(i.get(player)).getType() == Material.AIR){
				break;
			}
			questItems.get(player).add(e.getInventory().getItem(i.get(player)));
		}
		
		questFile.get(player).getConfig().set("Objective.items", questItems.get(player));
		questFile.get(player).saveConfig();
		
		ChatUtili.sendTranslatedMessage(player, "&7Saved required quest items for quest '&e" + e.getInventory().getName().split(":")[0] + "&7'.");
		}
		else if (e.getInventory().getName().split(":")[1].equalsIgnoreCase("ritem")){
			for (i.put(player, 0); i.get(player) < e.getInventory().getSize(); i.put(player, i.get(player) + 1)){
				if (e.getInventory().getItem(i.get(player)) == null || e.getInventory().getItem(i.get(player)).getType() == Material.AIR){
					break;
				}
				questItems.get(player).add(e.getInventory().getItem(i.get(player)));
			}
			
			questFile.get(player).getConfig().set("reward-items", questItems.get(player));
			questFile.get(player).saveConfig();
			
			ChatUtili.sendTranslatedMessage(player, "&7Saved quest reward items for quest '&e" + e.getInventory().getName().split(":")[0] + "&7'.");
		}
	}
	
	//TODO Setting [PlayerInteractSignEvent]
	@EventHandler
	public void onPlayerQuestInteract(PlayerInteractEvent e){
		Player player = (Player) e.getPlayer();
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK){ return; }
		if (e.getClickedBlock().getType() == Material.AIR){ return; }
		if (e.getClickedBlock().getState() == null){ return; }
		if (e.getClickedBlock().getState() instanceof Sign == false){ return; }
		Bukkit.getServer().getPluginManager().callEvent(new PlayerInteractSignEvent(player, ((Sign) e.getClickedBlock().getState())));
	}
	
	//TODO Player Chat Quest Objective [PlayerInteractSignEvent]
	@EventHandler
	public void onPlayerQuestObjChat(AsyncPlayerChatEvent e){
		Player player = (Player) e.getPlayer();
		if (SignQuestHandler.eventCommandBypass.containsKey(player) == false){ return; }
		if (SignQuestHandler.eventCommandBypass.get(player).split("\\:")[0].equalsIgnoreCase("TASK-CHAT") == false){ return; }
		e.setCancelled(true);
		handler.questFile.put(player, new QuestDataFile(SignQuestHandler.eventCommandBypass.get(player).split("\\:")[1].toString().toLowerCase()));
		
		if (handler.questFile.get(player).getConfig().getString("Objective.say") != null){
		ObjectiveDataFile.getCustomConfig().set(handler.questFile.get(player).getConfig().getString("Objective.say"), null);
		ObjectiveDataFile.saveCustomConfig();
		handler.questFile.get(player).getConfig().set("Objective.say", null);
		handler.questFile.get(player).saveConfig();
		}
		
		handler.questFile.get(player).getConfig().set("Objective.say", ChatColor.stripColor(e.getMessage()).toLowerCase());
		handler.questFile.get(player).saveConfig();
		ObjectiveDataFile.getCustomConfig().set("(chat) " + ChatColor.stripColor(e.getMessage()).toLowerCase(), SignQuestHandler.eventCommandBypass.get(player).split("\\:")[1]);
		ObjectiveDataFile.saveCustomConfig();
		
		ChatUtili.sendTranslatedMessage(player, "&7Say objective set: &e'" + ChatColor.stripColor(e.getMessage()) + "'.");
		SignQuestHandler.eventCommandBypass.remove(player);
	}
	
}
