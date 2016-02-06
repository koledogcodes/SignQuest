package me.koledogcodes.signquests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.koledogcodes.signquests.commands.questCommand;
import me.koledogcodes.signquests.commands.setQuestDelayCommand;
import me.koledogcodes.signquests.commands.setQuestMessageCommand;
import me.koledogcodes.signquests.commands.setQuestObjectiveCommand;
import me.koledogcodes.signquests.commands.setQuestRedeemMessageCommand;
import me.koledogcodes.signquests.commands.setQuestRewardCommand;
import me.koledogcodes.signquests.commands.setSignQuestCommand;
import me.koledogcodes.signquests.configs.ConfigFile;
import me.koledogcodes.signquests.configs.DelayDataFile;
import me.koledogcodes.signquests.configs.ObjectiveDataFile;
import me.koledogcodes.signquests.configs.PlayerDataFile;
import me.koledogcodes.signquests.configs.QuestDataFile;
import me.koledogcodes.signquests.events.SignQuestObjectiveEvents;
import me.koledogcodes.signquests.events.SignQuestSetupEvents;
import me.koledogcodes.signquests.events.SignQuestSignEvents;
import me.koledogcodes.signquests.handler.ChatUtili;
import me.koledogcodes.signquests.handler.SignQuestHandler;
import me.koledogcodes.signquests.timer.SignQuestTimer;

public class SignQuest extends JavaPlugin {

	public static File dataFolder;
	private SignQuestHandler handler;
	public static List<Timer> activeTimers = new ArrayList<Timer>();
	public static List<TimerTask> activeTasks = new ArrayList<TimerTask>();
	
	public void onEnable(){
		//Configuration
		generateConfiguration();
		reloadConfig();
		new File("plugins/SignQuests").mkdirs();
		new File("plugins/SignQuests/Quests").mkdirs();
		new File("plugins/SignQuests/Userdata").mkdirs();
		new File("plugins/SignQuests/Delay").mkdirs();
		dataFolder = new File("plugins/SignQuests");
		
		//Load classes
		new ConfigFile(this);
		new ChatUtili();
		new SignQuestTimer();
		new QuestDataFile(this);
		new PlayerDataFile(this);
		new SignQuestHandler(this);
		new DelayDataFile(this);
		new ObjectiveDataFile(this);
		handler = new SignQuestHandler(this);
		ChatUtili.messagePrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		
		//Events
		Bukkit.getServer().getPluginManager().registerEvents(new SignQuestSetupEvents (this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SignQuestSignEvents (this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SignQuestObjectiveEvents (this), this);
		
		//Commands
		getCommand("quest").setExecutor(new questCommand (this));
		getCommand("setquestmsg").setExecutor(new setQuestMessageCommand (this));
		getCommand("setredeemmsg").setExecutor(new setQuestRedeemMessageCommand (this));
		getCommand("setquestobj").setExecutor(new setQuestObjectiveCommand (this));
		getCommand("setsignquest").setExecutor(new setSignQuestCommand (this));
		getCommand("setquestdelay").setExecutor(new setQuestDelayCommand(this));
		getCommand("setquestreward").setExecutor(new setQuestRewardCommand(this));
		
		//Restart delays
		File getDelayFolder = new File(dataFolder + "/Delay");
		File[] delaydataFiles = getDelayFolder.listFiles();
		
		getLogger().info("---------------------------------------------");
		getLogger().info("(" + delaydataFiles.length + ") Delay(s) found!");
		
		getLogger().info("(" + delaydataFiles.length + ") Delay(s) are about to restart!");
		for (int i = 0; i < delaydataFiles.length; i++){
			if (delaydataFiles[i].isFile()){
				if (delaydataFiles[i].getName().contains(".yml")){
					try {
						handler.startQuestTimer(UUID.fromString(delaydataFiles[i].getName().replaceAll(".yml", "")));
					}
					catch (Exception e){
						continue;
					}
				}
			}
		}
		getLogger().info("(" + delaydataFiles.length + ") Delay(s) restarted!");
		getLogger().info("---------------------------------------------");
		
		//Reload Userdata
		File getUserdataFolder = new File(dataFolder + "/Userdata");
		File[] userdataFiles = getUserdataFolder.listFiles();
		
		getLogger().info("(" + userdataFiles.length + ") UUID(s) found!");
		
		getLogger().info("(" + userdataFiles.length + ") UUID(s) are about to reload!");
		for (int i = 0; i < userdataFiles.length; i++){
			if (userdataFiles[i].isFile()){
				if (userdataFiles[i].getName().contains(".yml")){
					try {
						PlayerDataFile playerDataFile = new PlayerDataFile(userdataFiles[i].getName().replaceAll(".yml", ""));
						playerDataFile.reloadConfig();
					}
					catch (Exception e){
						continue;
					}
				}
			}
		}
		getLogger().info("(" + userdataFiles.length + ") UUID(s) have reloaded!");
		getLogger().info("---------------------------------------------");
		
		//Reload Quest
		File getQuestFolder = new File(dataFolder + "/Quests");
		File[] questFiles = getQuestFolder.listFiles();
		
		getLogger().info("(" + questFiles.length + ") Quests(s) found!");
		
		getLogger().info("(" + questFiles.length + ") Quests(s) are about to reload!");
		for (int i = 0; i < questFiles.length; i++){
			if (questFiles[i].isFile()){
				if (questFiles[i].getName().contains(".yml")){
					try {
						QuestDataFile questDataFile = new QuestDataFile(questFiles[i].getName().replaceAll(".yml", ""));
						questDataFile.reloadConfig();
					}
					catch (Exception e){
						continue;
					}
				}
			}
		}
		getLogger().info("(" + questFiles.length + ") Quests(s) have reloaded!");
		getLogger().info("---------------------------------------------");
		
		getLogger().info("Plugin has enabled!");
	}
	
	public void onDisable(){
		SignQuestTimer timer = new SignQuestTimer();
		timer.cancelAllTimers(true);
		
		File getUserdataFolder = new File("plugins/SignQuests/Delay");
		File[] userdataFiles = getUserdataFolder.listFiles();
		
		DelayDataFile file;
		for (int i = 0; i < userdataFiles.length; i++){
			file = new DelayDataFile(userdataFiles[i].getName().replaceAll(".yml", ""));
			file.saveConfig();
		}
	}
	
	private void generateConfiguration(){
		if (getConfig().getString("prefix") == null){
			getConfig().set("prefix", "&8&lQuests &e&l> &7");
			saveConfig();
		}
	}
}
