package com.github.mineGeek.ZoneReset.Spawners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;





abstract class SpawnBase implements SpawnInterface {

	private int qty;
	private byte data;
	
	private String worldName;
	private boolean hasLocation;
	private int x;
	private int y;
	private int z;
	
	private ZRSPAWNTYPE type;
	
	
	public ZRSPAWNTYPE getType() {
		return this.type;
	}
	
	public void setType( ZRSPAWNTYPE type ) {
		this.type = type;
	}
	
	/**
	 * @return the qty
	 */
	public int getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(int qty) {
		this.qty = qty;
	}

	/**
	 * @return the data
	 */
	public byte getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte data) {
		this.data = data;
	}

	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}

	/**
	 * @param worldName the worldName to set
	 */
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	/**
	 * @return the hasLocation
	 */
	public boolean isHasLocation() {
		return hasLocation;
	}

	/**
	 * @param hasLocation the hasLocation to set
	 */
	public void setHasLocation(boolean hasLocation) {
		this.hasLocation = hasLocation;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	public Location getLocation() {
		
		if ( this.hasLocation ) {
		
			return new Location( Bukkit.getWorld( this.getWorldName() ), this.getX(), this.getY(), this.getZ() );
		}
		
		return null;
	}
	
	public void setLocation( Location l ) {
		
		this.setWorldName( l.getWorld().getName() );
		this.setX( l.getBlockX() );
		this.setY( l.getBlockY() );
		this.setZ( l.getBlockZ() );
		this.setHasLocation( true );
		
		
	}
	
	public void setList( Map<String, Object> list ) {
				
		if ( list.containsKey("data") && list.get("data") != null ) this.setData( Byte.valueOf( String.valueOf( list.get("data") ) ) );
		if ( list.containsKey("qty") ) this.setQty( (Integer) list.get("qty") );
		if ( list.containsKey("world") ) this.setWorldName( (String) list.get("world") );
		if ( list.containsKey("location") ) {
			@SuppressWarnings("unchecked")
			List<Integer> loc = (List<Integer>) list.get("location");
			this.setX( loc.get(0) );
			this.setY( loc.get(1) );
			this.setZ( loc.get(2) );
			this.setHasLocation( true );
		}
		
	}
	
	public Map<String, Object> getList( SpawnBase i ) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("type", this.getType().toString().toLowerCase() );
		if ( i.getData() != 0 ) r.put( "data", this.getData() );
		if ( i.getQty() != 0 ) r.put("qty", this.getQty() );
		//if ( i.getWorldName() != null ) r.put("world", this.getWorldName() );
		
		if ( this.isHasLocation() ) {
			r.put("location", new ArrayList<Integer>( Arrays.asList( this.x, this.y, this.z  )));
		}
		
		return r;
		
	}	
	
}
