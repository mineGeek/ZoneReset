package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.Location;
import org.bukkit.Material;

public class TriggerOnInteract extends Trigger {

	public TriggerOnInteract(String tag) {
		super(tag);
	}

	public Location location = null;
	public int materialId;
	
	public boolean run( Material m, Location l ) {
		
		return l.equals( location ) && m.getId() == materialId;

	}
	
	@Override
	public void close() {
		this.location = null;
	}
	
}
