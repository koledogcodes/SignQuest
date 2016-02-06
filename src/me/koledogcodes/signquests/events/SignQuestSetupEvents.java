package me.koledogcodes.signquests.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.api.events.PlayerInteractSignEvent;
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
	
}
