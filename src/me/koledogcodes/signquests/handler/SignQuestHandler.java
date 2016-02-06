package me.koledogcodes.signquests.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.koledogcodes.signquests.SignQuest;
import me.koledogcodes.signquests.configs.ConfigFile;
import me.koledogcodes.signquests.configs.DelayDataFile;
import me.koledogcodes.signquests.configs.PlayerDataFile;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.timer.SignQuestTimer;

public class SignQuestHandler {

		//private SignQuest plugin;
	public SignQuestHandler(SignQuest i) {
		//plugin = i;
	}
	
	public int messageDelay = ConfigFile.getCustomConfig().getInt("message-delay");
	public static HashMap<CommandSender, String> eventCommandBypass = new HashMap<CommandSender, String>();
	private HashMap<CommandSender, Integer> i = new HashMap<CommandSender, Integer>();
	private HashMap<CommandSender, Integer> j = new HashMap<CommandSender, Integer>();
	private HashMap<UUID, Integer> k = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> l = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> m = new HashMap<UUID, Integer>();
	private HashMap<UUID, DelayDataFile> delayFile = new HashMap<UUID, DelayDataFile>();
	private HashMap<UUID, Object[]> delayTasks = new HashMap<UUID, Object[]>();
	private HashMap<UUID, List<String>> delayTaskList = new HashMap<UUID, List<String>>();
	private HashMap<UUID, List<String>> delayTaskListContainer = new HashMap<UUID, List<String>>();
	public HashMap<CommandSender, QuestDataFile> questFile = new HashMap<CommandSender, QuestDataFile>();
	private HashMap<CommandSender, List<Boolean>> questCheck = new HashMap<CommandSender, List<Boolean>>();
	private HashMap<CommandSender, List<Boolean>> questCompleteCheck = new HashMap<CommandSender, List<Boolean>>();
	private HashMap<CommandSender, List<String>> questMessages = new HashMap<CommandSender, List<String>>();
	private HashMap<CommandSender, List<String>> questRewardCommands = new HashMap<CommandSender, List<String>>();
	private HashMap<CommandSender, List<ItemStack>> questItems = new HashMap<CommandSender, List<ItemStack>>();
	private HashMap<Player, Inventory> questItemsInventory = new HashMap<Player, Inventory>();
	private static HashMap<Player, Long> day = new HashMap<Player, Long>();
	private static HashMap<Player, Long> hour = new HashMap<Player, Long>();
	private static HashMap<Player, Long> min = new HashMap<Player, Long>();
	private static HashMap<Player, Long> s = new HashMap<Player, Long>();
	private static HashMap<Player, Long> time = new HashMap<Player, Long>();
	private HashMap<Player, String> data = new HashMap<Player, String>();
	private HashMap<Player, PlayerDataFile> dataFile = new HashMap<Player, PlayerDataFile>();
	
	//TODO Parse Location to String
	public String parseLocationToString(Location loc){
		return loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
	}
	
	//TODO Parse String to Location
	public Location parseStringToLocation(String loc){
		return new Location(Bukkit.getWorld(loc.split(" ")[0]), Double.parseDouble(loc.split(" ")[1]), Double.parseDouble(loc.split(" ")[2]), Double.parseDouble(loc.split(" ")[3]));
	}
	
	//TODO Message Delay Timer
	public static void messageDelayTime(Player player, HashMap<Player, Long> time){
		day.put(player, (long) 0);
		hour.put(player, (long) 0); 
		min.put(player, (long) 0); 
		s.put(player, (long) 0); 
		
		//Day
		if (time.get(player) > (((1000 * 60) * 60) * 24)){
			day.put(player, time.get(player) / (((1000 * 60) * 60) * 24));
			time.put(player, time.get(player) - day.get(player));
		}
		
		//Hour
		if (time.get(player) > ((1000 * 60) * 60)){
			hour.put(player, time.get(player) / ((1000 * 60) * 60) % 24);
			time.put(player, time.get(player) - hour.get(player));
		}
		
		//Minutes
		if (time.get(player) > (1000 * 60)){
			min.put(player, time.get(player) / (1000 * 60) % 60);
			time.put(player, time.get(player) - min.get(player));
		}
		
		//Seconds
		if (time.get(player) > 1000){
			s.put(player, time.get(player) / 1000 % 60);
			time.put(player, time.get(player) - s.get(player));
		}
		
		if (day.get(player) != 0){
			ChatUtili.sendTranslatedMessage(player, "&4Please wait: &c" + new String(day.get(player) + "d " + hour.get(player) + "h ") + min.get(player) + "m " + s.get(player) + "s to do this quest again.");
			return;
		}
		
		if (hour.get(player) != 0){
			ChatUtili.sendTranslatedMessage(player, "&4Please wait: &c" + new String(hour.get(player) + "h " + min.get(player) + "m " + s.get(player) + "s to do this quest again."));
			return;
		}
		
		if (min.get(player) != 0){
			ChatUtili.sendTranslatedMessage(player, "&4Please wait: &c" + new String(min.get(player) + "m " + s.get(player) + "s to do this quest again."));
			return;
		}
		
		if (s.get(player) != 0){
			ChatUtili.sendTranslatedMessage(player, "&4Please wait: &c" + new String(s.get(player) + "s to do this quest again."));
			return;
		}
		
		ChatUtili.sendTranslatedMessage(player, "&4Please wait: &c" + new String(day.get(player) + "d " + hour.get(player) + "h ") + min.get(player) + "m " + s.get(player) + "s to do this quest again.");

	}

	//TODO Create Quest
	public void createQuest(CommandSender player, String quest){
		if (questExists(quest)){
			ChatUtili.sendTranslatedMessage(player, "&c&lSorry, &7quest &e" + quest + " &7already exists!");
		}
		else {
			if (quest.length() > 16){
				ChatUtili.sendTranslatedMessage(player, "&cQuest names can only be 1-15 characters long!");
				return;
			}
			
			if (quest.equalsIgnoreCase("none")){
				ChatUtili.sendTranslatedMessage(player, "&cThis is a reserved for secert plugin uses.");
				return;
			}
			
			if (quest.contains(".") || quest.contains(":")){
				ChatUtili.sendTranslatedMessage(player, "&cYou cannot use '.' or ':' in your quest names.");
				return;
			}
			QuestDataFile file = new QuestDataFile(quest);
			file.getConfig().set("messages", new ArrayList<String>());
			file.reloadConfig();
			ChatUtili.sendTranslatedMessage(player, "&aQuest '&a&o" + quest + "&a' has been created!");
		}
	}
	
	//TODO Delete Quest
	public void deleteQuest(CommandSender player, String quest){
		if (questExists(quest) == false){
			ChatUtili.sendTranslatedMessage(player, "&c&lSorry, &7quest &e" + quest + " &7does not exist!");
		}
		else {
			if (new File(SignQuest.dataFolder + "/Quests", quest + ".yml").delete()){
				ChatUtili.sendTranslatedMessage(player, "&aQuest '&a&o" + quest + "&a' has been deleted!");
			}
			else {
				ChatUtili.sendTranslatedMessage(player, "&cQuest '&c&o" + quest + "&c' has failed to delete!");
			}
		}
	}
	
	//TODO Quest exists
	public boolean questExists(String quest){
		if (new File(SignQuest.dataFolder + "/Quests", quest.toLowerCase() + ".yml").exists()){
			quest = null;
			return true;
		}
		else {
			quest = null;
			return false;
		}
	}
	
	//TODO Color convert
	public String colorConvert(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	//TODO Add quest message
	public void addQuestMessage(CommandSender player, String quest, String message){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("quest-messages"));
		questMessages.get(player).add(message);
		questFile.get(player).getConfig().set("quest-messages", questMessages.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Quest message '" + message + "&7' added!");
	}
	
	//TODO Remove quest message
	public void removeQuestMessage(CommandSender player, String quest, int line){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("quest-messages"));
		
		if (line > questMessages.get(player).size() || line < 0){
			ChatUtili.sendTranslatedMessage(player, "&cThat message line does not exist.");
			return;
		}
		
		questMessages.get(player).remove(line);
		questFile.get(player).getConfig().set("quest-messages", questMessages.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Quest message line &e'" + line + "&7' &7deleted!");
	}
	
	//TODO Add quest redeem message
	public void addQuestRedeemMessage(CommandSender player, String quest, String message){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("redeem-messages"));
		questMessages.get(player).add(message);
		questFile.get(player).getConfig().set("redeem-messages", questMessages.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Redeem message '" + message + "&7' added!");
	}
	
	//TODO Remove quest redeem message
	public void removeQuestRedeemMessage(CommandSender player, String quest, int line){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("quest-messages"));
		
		if (line > questMessages.get(player).size() || line < 0){
			ChatUtili.sendTranslatedMessage(player, "&cThat message line does not exist.");
			return;
		}
		
		questMessages.get(player).remove(line);
		questFile.get(player).getConfig().set("redeem-messages", questMessages.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Redeem message line &e'" + line + "&7' &7deleted!");
	}
	
	//TODO Add quest reward command
	public void addQuestRewardCommand(Player player, String quest, String command){
		questFile.put(player, new QuestDataFile(quest));
		questRewardCommands.put(player, questFile.get(player).getConfig().getStringList("reward-commands"));
		questRewardCommands.get(player).add(command);
		questFile.get(player).getConfig().set("reward-commands", questRewardCommands.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Quest reward command '" + command + "&7' added!");
	}
	
	//TODO Remove quest reward command
	public void removeQuestRewardCommand(CommandSender player, String quest, int line){
		questFile.put(player, new QuestDataFile(quest));
		questRewardCommands.put(player, questFile.get(player).getConfig().getStringList("reward-commands"));
		
		if (line > questRewardCommands.get(player).size() || line < 0){
			ChatUtili.sendTranslatedMessage(player, "&cThat command line does not exist.");
			return;
		}
		
		questRewardCommands.get(player).remove(line);
		questFile.get(player).getConfig().set("reward-commands", questRewardCommands.get(player));
		questFile.get(player).saveConfig();
		ChatUtili.sendTranslatedMessage(player, "&7Quest reward command line &e'" + line + "&7' &7deleted!");
	}
	
	//TODO Open quest item rewards
	@SuppressWarnings("unchecked")
	public void openQuestRewardItemInventory(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		
		if (questFile.get(player).getConfig().getList("reward-items") == null){
			questItemsInventory.put(player, Bukkit.createInventory(null, 36, quest + ":ritem"));
			player.openInventory(questItemsInventory.get(player));
			return;
		}
		
		questItems.put(player, (List<ItemStack>) questFile.get(player).getConfig().getList("reward-items"));
		questItemsInventory.put(player, Bukkit.createInventory(null, 36, quest + ":ritem"));
		
		for (i.put(player, 0); i.get(player) < questItems.get(player).size(); i.put(player, i.get(player) + 1)){
		questItemsInventory.get(player).setItem(i.get(player), questItems.get(player).get(i.get(player)));
		}
		
		player.openInventory(questItemsInventory.get(player));
	}
	
	//TODO Open quest item requirements
	@SuppressWarnings("unchecked")
	public void openQuestItemRequirementInventory(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		
		if (questFile.get(player).getConfig().getList("Objective.items") == null){
			questItemsInventory.put(player, Bukkit.createInventory(null, 36, quest + ":ireq"));
			player.openInventory(questItemsInventory.get(player));
			return;
		}
		
		questItems.put(player, (List<ItemStack>) questFile.get(player).getConfig().getList("Objective.items"));
		questItemsInventory.put(player, Bukkit.createInventory(null, 36, quest + ":ireq"));
		
		for (i.put(player, 0); i.get(player) < questItems.get(player).size(); i.put(player, i.get(player) + 1)){
		questItemsInventory.get(player).setItem(i.get(player), questItems.get(player).get(i.get(player)));
		}
		
		player.openInventory(questItemsInventory.get(player));
	}
	
	//TODO Message quest messages
	public void messageQuestMessages(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("quest-messages"));
		for (i.put(player, 0); i.get(player) < questMessages.get(player).size(); i.put(player, i.get(player) + 1)){
			player.sendMessage(questMessages.get(player).get(i.get(player)).replaceAll("<player>", player.getName()));
		}
	}
	
	//TODO Message quest redeem messages
	public void messageQuestRedeemMessages(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		questMessages.put(player, questFile.get(player).getConfig().getStringList("redeem-messages"));
		for (i.put(player, 0); i.get(player) < questMessages.get(player).size(); i.put(player, i.get(player) + 1)){
			player.sendMessage(questMessages.get(player).get(i.get(player)).replaceAll("<player>", player.getName()));
		}
	}
	
	//TODO Can complete quest
	public boolean canCompleteQuest(Player player, String quest){
		questCompleteCheck.put(player, new ArrayList<Boolean>());
		questFile.put(player, new QuestDataFile(quest));
		delayFile.put(player.getUniqueId(), new DelayDataFile(player.getUniqueId().toString()));
		time.put(player, delayFile.get(player.getUniqueId()).getConfig().getLong("delay." + quest));
		dataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		
		if (playerInQuestDelay(player, quest)){
			messageDelayTime(player, time);
			return false;
		}
		
		//Quest [ITEMS] Objective
		if (questFile.get(player).getConfig().getList("Objective.items") != null){
		if (!questFile.get(player).getConfig().getList("Objective.items").isEmpty()){
		if (questFile.get(player).getConfig().getStringList("Objective.items") != null){
		if (dataFile.get(player).getConfig().getString(quest + ".objective.items") == null){
		if (playerHasQuestItems(player, quest)){
			removeQuestItems(player, quest);
			dataFile.get(player).getConfig().set(quest + ".objective.items", true);
			dataFile.get(player).saveConfig();
			ChatUtili.sendTranslatedMessage(player, "&7Gather task for quest &e" + quest + " &7achieved!");
		}
		else {
			questCompleteCheck.get(player).add(false);
		}
		}
		}
		}
		}
		
		if (questCompleteCheck.get(player).contains(false)){
			return false;
		}
		else {
			if (questHasDelay(quest)){
				delayNewQuest(player, quest);
			}
			
			messageQuestRedeemMessages(player, quest);
			giveQuestRewards(player, quest);
			clearPlayerObjectives(player, quest);
			return true;
		}
	}
	
	//TODO Player has all quest items
	@SuppressWarnings("unchecked")
	public boolean playerHasQuestItems(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		questCheck.put(player, new ArrayList<Boolean>());
		if (questFile.get(player).getConfig().getList("Objective.items") == null){
			return true;
		}
		
		questItems.put(player, (List<ItemStack>) questFile.get(player).getConfig().getList("Objective.items"));
		
		if (questItems.get(player).isEmpty()){
			return true;
		}
		
		for (j.put(player, 0); j.get(player) < questItems.get(player).size(); j.put(player, j.get(player) + 1)){
			if (player.getInventory().containsAtLeast(questItems.get(player).get(j.get(player)), questItems.get(player).get(j.get(player)).getAmount())){
				questCheck.get(player).add(true);
			}
			else {
				questCheck.get(player).add(false);
				ChatUtili.sendTranslatedMessage(player, "&7You require the following item: ");
				if (questItems.get(player).get(j.get(player)).getItemMeta().getDisplayName() != null){
				ChatUtili.sendTranslatedMessage(player, "&7Display Name: &e" + questItems.get(player).get(j.get(player)).getItemMeta().getDisplayName());
				}
				ChatUtili.sendTranslatedMessage(player, "&7Item Id: &e" + questItems.get(player).get(j.get(player)).getType().name());
				ChatUtili.sendTranslatedMessage(player, "&7Type Id: &e" + getTypeID(player, questItems.get(player).get(j.get(player))));
				ChatUtili.sendTranslatedMessage(player, "&7Amount: &e" + questItems.get(player).get(j.get(player)).getAmount());
				ChatUtili.sendTranslatedMessage(player, "&7Durability: &e" + questItems.get(player).get(j.get(player)).getDurability());
				if (questItems.get(player).get(j.get(player)).getEnchantments().isEmpty() == false){
				ChatUtili.sendTranslatedMessage(player, "&7Enchants: &e" + questItems.get(player).get(j.get(player)).getEnchantments());
				}
				if (questItems.get(player).get(j.get(player)).getItemMeta().getLore() != null){
				ChatUtili.sendTranslatedMessage(player, "&7Lore: &e" + questItems.get(player).get(j.get(player)).getItemMeta().getLore());
				}
				break;
			}
		}
		
		if (questCheck.get(player).contains(false)){
			return false;
		}
		else {
			return true;
		}
	}
	
	//TODO Remove quest items
	@SuppressWarnings("unchecked")
	public void removeQuestItems(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		questCheck.put(player, new ArrayList<Boolean>());
		if (questFile.get(player).getConfig().getList("Objective.items") == null){
			return;
		}
		
		questItems.put(player, (List<ItemStack>) questFile.get(player).getConfig().getList("Objective.items"));
		
		if (questItems.get(player).isEmpty()){
			return;
		}
		
		if (playerHasQuestItems(player, quest)){
			for (j.put(player, 0); j.get(player) < questItems.get(player).size(); j.put(player, j.get(player) + 1)){
				player.getInventory().removeItem(questItems.get(player).get(j.get(player)));
			}	
			
			player.updateInventory();
		}
		else {
			return;
		}
	}
	
	//TODO Start quest delay
	public void startQuestTimer(final UUID uuid){
		delayFile.put(uuid, new DelayDataFile(uuid.toString()));
		SignQuestTimer timer = new SignQuestTimer();
		timer.registerNewRepeatingTimer(new TimerTask(){

			@Override
			public void run() {
				delayTaskListContainer.put(uuid, getDelayedQuests(uuid));
				for (k.put(uuid, 0); k.get(uuid) < delayTaskListContainer.get(uuid).size(); k.put(uuid, k.get(uuid) + 1)){
					
					if ((delayFile.get(uuid).getConfig().getLong("delay." + delayTaskListContainer.get(uuid).get(k.get(uuid))) - 1000) < 1000){
						delayFile.get(uuid).getConfig().set("delay." + delayTaskListContainer.get(uuid).get(k.get(uuid)), null);
						delayFile.get(uuid).saveConfig();
						
						if (Bukkit.getServer().getPlayer(uuid) != null){
							ChatUtili.sendTranslatedMessage(Bukkit.getServer().getPlayer(uuid), "&7You may complete quest '&a" + delayTaskListContainer.get(uuid).get(k.get(uuid)) + "&7' again.");
						}
						continue;
					}
					
					delayFile.get(uuid).getConfig().set("delay." + delayTaskListContainer.get(uuid).get(k.get(uuid)), delayFile.get(uuid).getConfig().getLong("delay." + delayTaskListContainer.get(uuid).get(k.get(uuid))) - 1000);
					delayFile.get(uuid).saveConfig();
				}

			}
			
		}, 1000, 1000);
	}
	
	//TODO Delay new quest
	public void delayNewQuest(Player player, String quest){
		delayFile.put(player.getUniqueId(), new DelayDataFile(player.getUniqueId().toString()));
		
		if (delayFile.get(player.getUniqueId()).getConfig().getBoolean("Timer-Active") == false || delayFile.get(player.getUniqueId()).getConfig().getString("Timer-Active") == null){
			startQuestTimer(player.getUniqueId());
			delayFile.get(player.getUniqueId()).getConfig().set("Timer-Active", true);
			delayFile.get(player.getUniqueId()).saveConfig();
		}
		
		delayFile.get(player.getUniqueId()).getConfig().set("delay." + quest, new QuestDataFile(quest).getConfig().getLong("delay"));
		delayFile.get(player.getUniqueId()).saveConfig();
	}
	
	//TODO Quest has delay
	public boolean questHasDelay(String quest){
		if (new QuestDataFile(quest).getConfig().getString("delay") == null){
			return false;
		}
		else {
			return true;
		}
	}

	//TODO Player is in quest delay
	public boolean playerInQuestDelay(Player player, String quest){
		delayFile.put(player.getUniqueId(), new DelayDataFile(player.getUniqueId().toString()));
		if (delayFile.get(player.getUniqueId()).getConfig().getString("delay." + quest) == null){
			return false;
		}
		else {
			return true;
		}
	}
	
	//TODO Get player delayed quests
	public List<String> getDelayedQuests(UUID uuid){
		delayTaskList.put(uuid, new ArrayList<String>());
		delayFile.put(uuid, new DelayDataFile(uuid.toString()));
		if (delayFile.get(uuid).getConfig().getString("delay") == null){
			return new ArrayList<String>();
		}
		delayTasks.put(uuid, delayFile.get(uuid).getConfig().getConfigurationSection("delay").getKeys(false).toArray());
		for (l.put(uuid, 0); l.get(uuid) < delayTasks.get(uuid).length; l.put(uuid, l.get(uuid) + 1)){
			delayTaskList.get(uuid).add(delayTasks.get(uuid)[l.get(uuid)].toString());
		}
		
		return delayTaskList.get(uuid);
	}
	
	//TODO Give quest rewards
	@SuppressWarnings("unchecked")
	public void giveQuestRewards(Player player, String quest){
		questFile.put(player, new QuestDataFile(quest));
		
		if (questFile.get(player).getConfig().getString("reward-commands") == null && questFile.get(player).getConfig().getString("reward-items") == null ){
			ChatUtili.sendTranslatedMessage(player, "&cThere are no quest rewards.");
			return;
		}
		
		if (questFile.get(player).getConfig().getString("reward-commands") != null){
		questRewardCommands.put(player, questFile.get(player).getConfig().getStringList("reward-commands"));
		for (m.put(player.getUniqueId(), 0); m.get(player.getUniqueId()) < questRewardCommands.get(player).size(); m.put(player.getUniqueId(), m.get(player.getUniqueId()) + 1)){
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), questRewardCommands.get(player).get(m.get(player.getUniqueId())).replaceAll("<player>", player.getName()));
		}
		}

		if (questFile.get(player).getConfig().getString("reward-items") != null){
		questItems.put(player, (List<ItemStack>) questFile.get(player).getConfig().getList("reward-items"));
		for (m.put(player.getUniqueId(), 0); m.get(player.getUniqueId()) < questItems.get(player).size(); m.put(player.getUniqueId(), m.get(player.getUniqueId()) + 1)){
			if (player.getInventory().firstEmpty() == -1){
				player.getWorld().dropItem(player.getLocation(), questItems.get(player).get(m.get(player.getUniqueId())));
			}
			else {
				player.getInventory().addItem(questItems.get(player).get(m.get(player.getUniqueId())));
			}
		}
	}
		
	}
	
	//TODO Clear player quest objective
	public void clearPlayerObjectives(Player player, String quest){
		dataFile.put(player, new PlayerDataFile(player.getUniqueId().toString()));
		dataFile.get(player).getConfig().set(quest + ".objective", null);
		dataFile.get(player).saveConfig();
	}
	
	//TODO Get type item id
	@SuppressWarnings({ "deprecation" })
	private String getTypeID(Player player, ItemStack itemstack){
	if (itemstack.getType().getMaxDurability() > 0)
	data.put(player, "0");
	else if (itemstack.getDurability() > 0)
	data.put(player, Short.toString(itemstack.getDurability()));
	else if (itemstack.getData().getData() > 0)
	data.put(player, Byte.toString(itemstack.getData().getData()));
	else {
	data.put(player, "0");
	}
	return data.get(player);
	}
	
	//TODO Import old SignQuests
	@SuppressWarnings("deprecation")
	public void importOldSignQuests(CommandSender sender){
		File file = new File(SignQuest.dataFolder, "Quests.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		QuestDataFile questDataFile;
		List<String> quests = config.getStringList("Quests");
		List<String> questMessages;
		List<String> rewardCommands;
		List<ItemStack> questItems = new ArrayList<ItemStack>();
		String quest;
		ChatUtili.sendTranslatedMessage(sender, "&a(" + quests.size() + ") quest(s) found!");
		for (int a = 0; a < quests.size(); a++){
			quest = quests.get(a);
			System.out.println("[SignQuests] Quest '" + quest + "' is being imported.");
			ChatUtili.sendTranslatedMessage(sender, "&aImporting quest '" + quest + "'.");
			
			//Creating new quest or overide.
			createQuest(null, quest);
			
			//Moving delay
			questDataFile = new QuestDataFile(quest);
			questDataFile.getConfig().set("delay", (config.getLong(quest + ".Delay") * 1000));
			questDataFile.saveConfig();
			
			//Moving Quest Messages
			questMessages = config.getStringList(quest + ".messages"); 
			for (int b = 0; b < questMessages.size(); b++){
				addQuestMessage(null, quest, questMessages.get(b));
			}
			
			//Moving Quest Redeem Messages
			questMessages = config.getStringList(quest + ".redeem-messages"); 
			for (int b = 0; b < questMessages.size(); b++){
				addQuestRedeemMessage(null, quest, questMessages.get(b));
			}
			
			//Moving Quest Reward commands
			rewardCommands = config.getStringList(quest + ".RewardCommands"); 
			for (int b = 0; b < rewardCommands.size(); b++){
				addQuestRewardCommand(null, quest, rewardCommands.get(b));
			}
			
			//Moving quest items
			try {
				questItems.clear();
				int c = 1;
				while (config.getString(quest + ".Items.Item-" + c) != null){
					questDataFile = new QuestDataFile(quest);
					ItemStack item = new ItemStack(config.getInt(quest + ".Items.Item-" + c + ".Item-Id"), config.getInt(quest + ".Items.Item-" + c + ".Amount"), (short) config.getInt(quest + ".Items.Item-" + c + ".Type-Id"));
					item.setDurability((short) config.getInt(quest + ".Items.Item-" + c + ".Dura"));
					item.setAmount(config.getInt(quest + ".Items.Item-" + c + ".Amount"));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(config.getString(quest + ".Items.Item-" + c + ".Display-Name"));
					meta.setLore(config.getStringList(quest + ".Items.Item-" + c + ".Lore"));
					item.setItemMeta(meta);
					questItems.add(item);
					
					questDataFile.getConfig().set("Objective.items", questItems);
					questDataFile.saveConfig();
					c++;
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			//Moving quest reward items
			try {
				questItems.clear();
				int c = 1;
				while (config.getString(quest + ".RewardItem-" + c) != null){
					questDataFile = new QuestDataFile(quest);
					ItemStack item1 = new ItemStack(config.getInt(quest + ".RewardItem-" + c + ".Item-Id"), config.getInt(quest + ".RewardItem-" + c + ".Amount"), (short) config.getInt(quest + ".RewardItem-" + c + ".Type-Id"));
					item1.setDurability((short) config.getInt(quest + ".RewardItem-" + c + ".Dura"));
					ItemMeta meta = item1.getItemMeta();
					meta.setDisplayName(config.getString(quest + ".RewardItem-" + c + ".Display-Name"));
					meta.setLore(config.getStringList(quest + ".RewardItem-" + c + ".Lore"));
					item1.setItemMeta(meta);
					questItems.add(item1);
					
					questDataFile.getConfig().set("reward-items", questItems);
					questDataFile.saveConfig();
					c++;
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
			System.out.println("[SignQuests] Quest '" + quest + "' has been imported.");
			ChatUtili.sendTranslatedMessage(sender, "&aQuest '" + quest + "' has been imported.");
		}
	}
	
	public String getQuestList(){
		String quest = "";
		File getQuestsFolder = new File(SignQuest.dataFolder + "/Quests");
		File[] getQuests = getQuestsFolder.listFiles();
		
		for (int i = 0; i < getQuests.length; i++){
			if (getQuests[i].isFile()){
				if (getQuests[i].getName().split("\\.")[1].equals("yml")){
					if (i == (getQuests.length - 1)){
						quest += getQuests[i].getName().split("\\.")[0];
					}
					else {
						quest += getQuests[i].getName().split("\\.")[0] + ", ";
					}
				}
			}
		}
		
		return quest;
	}
}
