package me.koledogcodes.signquests.api.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractSignEvent extends Event implements Cancellable {

	Player player;
	Sign sign;
	boolean cancelled;
	
	public PlayerInteractSignEvent(Player player, Sign s) {
		this.player = player;
		this.sign = s;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Sign getSign(){
		return sign;
	}
	
	public void setLine(int line, String text){
		sign.setLine(line, text);
		sign.update();
	}
	
	public String getLine(int line){
		return sign.getLine(line);
	}
	
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
}
