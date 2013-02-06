package com.github.mineGeek.ZoneReset.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ZoneBlocks implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector ne;
	private Vector sw;
	private String worldName;
	
	private List<ZoneBlock> blocks = new ArrayList<ZoneBlock>();
	
	public ZoneBlocks( String worldName, Vector ne, Vector sw ) {
		this.ne = ne;
		this.sw = sw;
		this.worldName = worldName;
	}
	
	public void setBlocks() {
		
		Location upperRight = ne.toLocation( Bukkit.getWorld( this.worldName ) );
		Location lowerLeft = sw.toLocation( Bukkit.getWorld( this.worldName ) );
		
		int fromX = Math.min( lowerLeft.getBlockX(), upperRight.getBlockX() );
		int fromY = Math.min( lowerLeft.getBlockY(), upperRight.getBlockY() );
		int fromZ = Math.min( lowerLeft.getBlockZ(), upperRight.getBlockZ() );
		
		int toX = Math.max( lowerLeft.getBlockX(), upperRight.getBlockX() );
		int toY = Math.max( lowerLeft.getBlockY(), upperRight.getBlockY() );
		int toZ = Math.max( lowerLeft.getBlockZ(), upperRight.getBlockZ() );		
		
		
		for ( int x = fromX; x <= toX; x++ ) {
			for ( int z=fromZ; z <= toZ; z++ ) {
				for (int y=fromY; y<=toY; y++ ) {
					blocks.add( new ZoneBlock( Bukkit.getWorld( this.worldName).getBlockAt( new Location( Bukkit.getWorld( this.worldName), x, y, z))));
				}
			}
		}
		
	}
	
	
}
