package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class EntityLocation {

	public EntityType entityType;
	public Location location;
	
	public EntityLocation( String entity, World world, Integer x, Integer y, Integer z ) {
		this.entityType = EntityType.fromName(entity.replace(" ", "_").toUpperCase());
		this.location = new Location( world, x, y, z );
	}
	
	

}
