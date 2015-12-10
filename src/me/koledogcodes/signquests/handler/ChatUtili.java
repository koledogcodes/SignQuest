package me.koledogcodes.signquests.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtili {

	/* Constructor */
	public ChatUtili() {
	}
	
	public static String messagePrefix = "&8[&aSignQuests&8]";
	
	/**
	 * Sends a player a message with color codes auto translated.
	 * @param player - Player to send message to
	 * @param message - Message to be sent to player
	 */
	public static void sendTranslatedMessage(CommandSender player, String message){
	if (messagePrefix != null){
		if (player != null){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagePrefix + " " + message));
		}
	}
	else {
		if (player != null){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
	}
	
	public static String colorConvert(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	/**
	 * Sends a broadcast message, supports color codes.
	 * @param message - Message to be broadcasted
	 */
	public static void sendTranslatedBroadcastMessage(String message){
	Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	
}
