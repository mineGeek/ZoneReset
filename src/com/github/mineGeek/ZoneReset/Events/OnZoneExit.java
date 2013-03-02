package com.github.mineGeek.ZoneReset.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zone.ZRPVPMode;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class OnZoneExit extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public OnZoneExit( Zone zone, Player player ) {
		
		if ( zone.triggers.getOnExit().message != null ) player.sendMessage( zone.triggers.getOnExit().message );
		Utilities.pvpAddPlayer( player.getName(), ZRPVPMode.DEFAULT);
		player.sendMessage( zone.pvpMode.toString() );
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
