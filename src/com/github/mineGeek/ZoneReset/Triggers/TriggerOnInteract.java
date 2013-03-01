package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.Location;
import org.bukkit.Material;

import com.github.mineGeek.ZoneReset.Data.Zones;

public class TriggerOnInteract extends Trigger {

	public TriggerOnInteract(String tag) {
		super(tag);
	}

	public Location location = null;
	public int materialId;
	
	public boolean run( Material m, Location l ) {
		
		if ( l.equals( location ) && m.getId() == materialId ) {
			Zones.getZone(tag).reset();
			return true;
		}
		
		return false;

	}
	
	@Override
	public void close() {
		this.location = null;
	}
	
}
