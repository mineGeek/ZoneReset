package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class EntityLocation {

	public EntityType entityType;
	private Location location;
	
	private String worldName;
	private int x;
	private int y;
	private int z;
	
	public short id = 0;
	
	public EntityLocation( String entity, World world, Integer x, Integer y, Integer z ) {
		this.setEntityTypeFromName( entity );
		this.location = new Location( world, x, y, z );
	}
	
	public EntityLocation( String entity, String worldName, int x, int y, int z ) {
		this.setEntityTypeFromName( entity );
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public EntityLocation( String entity, short id, String worldName, int x, int y, int z ) {
		this.setEntityTypeFromName( entity );
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = 0;
	}	
	
	public void setEntityTypeFromName( String name ) {
		
		if ( EntityType.fromName(name) == null ) {
			if ( name.equalsIgnoreCase("ocelot") ) setEntityTypeFromName( "ozelot");
		} else {
			this.entityType = EntityType.fromName(name);
		}
		
		
	}
	
	public Location getLocation() {
		
		if ( this.location == null ) {
			
			World w = Bukkit.getWorld( this.worldName );
			
			if ( w != null ) {
				return new Location( w, x, y, z );
			}			
		}
		
		return this.location;
		
	}
	
	public void close() {
		this.location = null;
	}
	
	

}
