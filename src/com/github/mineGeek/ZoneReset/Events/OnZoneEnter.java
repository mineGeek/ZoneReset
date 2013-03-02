package com.github.mineGeek.ZoneReset.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class OnZoneEnter extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public OnZoneEnter( Zone zone, Player player ) {
		
		if ( zone.triggers.getOnEnter().message != null ) player.sendMessage( zone.triggers.getOnEnter().message );
		Utilities.pvpAddPlayer( player.getName(), zone.pvpMode );
		player.sendMessage( zone.pvpMode.toString() + " 00 " + player.getWorld().getPVP() );
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
		this.setCancelled( cancel );
		
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
