package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EntityInfoBase implements EntityInfoInterface {

	public enum EntityInfoType {MOB, HUMAN, ITEM};
	
	private EntityType entityType;
	private String entityName;
	private EntityInfoType entityInfoType;
	private String worldName;
	private int x;
	private int y;
	private int z;
	
	
	@Override
	public String getEntityName() {
		return this.entityName;
	}
	
	public void setEntityName( String value  ) {
		this.entityName = value;
	}
	
	@Override
	public EntityInfoType getEntityInfoType() {
		return this.entityInfoType;
	}
	
	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getEntityType()
	 */
	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		
		
		if ( EntityType.fromName( entityType.name() ) == null ) {
			if ( entityType.name().equalsIgnoreCase("ocelot") ) {
				this.setEntityType( EntityType.fromName("ozelot") );
			}
		}
		
		this.entityType = entityType;
		this.entityName = entityType.name();
		
	}

	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getWorldName()
	 */
	@Override
	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getZ()
	 */
	@Override
	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public EntityInfoBase() {}
	
	public EntityInfoBase( EntityType entity, String worldName, int x, int y, int z ) {
		
		this.setEntityType( entity );
		this.setWorldName( worldName );
		this.setX( x );
		this.setY( y );
		this.setZ( z );
		
	}
	
	public EntityInfoBase( EntityType entity, Location l ) {
		
		this.setEntityType( entity );
		this.setWorldName( l.getWorld().getName() );
		this.setX( l.getBlockX() );
		this.setY( l.getBlockY() );
		this.setZ( l.getBlockZ() );
		
	}
	
	/* (non-Javadoc)
	 * @see com.github.mineGeek.ZoneReset.Utilities.EntityInfoI#getLocation()
	 */
	@Override
	public Location getLocation() {
		
		return new Location( Bukkit.getWorld( this.getWorldName() ), this.getX(), this.getY(), this.getZ() );
		
	}
	
	
}
