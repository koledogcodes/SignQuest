package me.koledogcodes.signquests.configs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.koledogcodes.signquests.SignQuest;

public class ConfigFile {
	
	private static SignQuest plugin;
	public ConfigFile(SignQuest i){
		plugin = i;
	}
	
	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;
	
	public static void reloadCustomConfig(){
	if (customConfigFile == null) {
	customConfigFile = new File(plugin.getDataFolder(), "config.yml"); 
	}
	
	customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	InputStream defConfigStream = plugin.getResource("config.yml");
	if (defConfigStream != null) {
	@SuppressWarnings("deprecation")
	YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	customConfig.setDefaults(defConfig);
	}
	}
	 
	public static FileConfiguration getCustomConfig() {
	if (customConfig == null) {
	reloadCustomConfig(); 
	}
	return customConfig;
	}
	 
	public static void saveCustomConfig() {
	if (customConfig == null || customConfigFile == null) {
	return;
	}
	try {
	getCustomConfig().save(customConfigFile);
	} catch (IOException ex) {
	plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	}
	}
	 
}
