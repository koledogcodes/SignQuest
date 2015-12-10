package me.koledogcodes.signquests.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.koledogcodes.signquests.SignQuest;

public class QuestDataFile {
	
		//private SignQuest plugin;
	public QuestDataFile(SignQuest i){
		//plugin = i;
	}
	
	@SuppressWarnings("unused")
	private String quest = "";
	private File file;
	private FileConfiguration customConfig = null;

	public QuestDataFile(String quest) {
		this.quest = quest;
		this.file = new File(SignQuest.dataFolder + "/Quests", quest + ".yml");
		
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public FileConfiguration getConfig(){
		if (customConfig == null) {
			reloadConfig(); 
		}
		return customConfig;
	}
	
	public void saveConfig(){
		try {
			getConfig().save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reloadConfig(){
		if (file.exists() == false){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		customConfig = YamlConfiguration.loadConfiguration(file);
		
	}

}