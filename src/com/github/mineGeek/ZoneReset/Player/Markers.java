package com.github.mineGeek.ZoneReset.Player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Markers {

	public Location ne;
	public Location sw;
	String worldName;
	
	List<Marker> markers = new ArrayList<Marker>();
	
	public void setNE( Player player, Location ne ) {
		this.ne = ne;
		this.worldName = ne.getWorld().getName();
	}
	
	public void setSW( Player player, Location sw ) {
		this.sw = sw;
		this.worldName = sw.getWorld().getName();
	}
	
	public void setMarkers( Player player ) {
		
		clearMarkers( player );
		
		if ( ne != null && sw != null ) {
				
			int minX = Math.min( ne.getBlockX(), sw.getBlockX() ) - 1;
			int maxX = Math.max( ne.getBlockX(), sw.getBlockX() ) + 1;
			
			int minY = Math.min( ne.getBlockY(), sw.getBlockY() ) - 1;
			int maxY = Math.max( ne.getBlockY(), sw.getBlockY() ) + 1;
			
			int minZ = Math.min( ne.getBlockZ(), sw.getBlockZ() ) - 1;
			int maxZ = Math.max( ne.getBlockZ(),  sw.getBlockZ() ) + 1;
			
			
			addMarker( minX, minY+1, minZ, minX+1, minZ+1 );
			addMarker( maxX, minY+1, minZ, maxX-1, minZ+1 );
			addMarker( minX, minY+1, maxZ, minX+1, maxZ-1 );
			addMarker( maxX, minY+1, maxZ, maxX-1, maxZ-1 );			

			addMarker( minX, maxY-1, minZ, minX+1, minZ+1 );
			addMarker( maxX, maxY-1, minZ, maxX-1, minZ+1 );
			addMarker( minX, maxY-1, maxZ, minX+1, maxZ-1 );
			addMarker( maxX, maxY-1, maxZ, maxX-1, maxZ-1 );
			
		} else if ( ne != null ) {
			addMarker( ne );
		} else if ( sw != null ) {
			addMarker( sw );
			
		}
		
	}
	
	public void highlight( Player player ) {
		
		for ( Marker m : markers ) {
			m.highlight( player );
		}
		
	}
	
	public void unhighlight( Player player ) {
		
		for ( Marker m : markers ) {
			m.unhighlight( player );
		}
	}
	
	public void addMarker( Location l ) {
		addMarker( l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getBlockX(), l.getBlockZ() );
	}
	
	public void addMarker( int x, int y, int z, int xFlag, int zFlag ) {
		
		World world = Bukkit.getWorld( this.worldName );
		Block b1 = world.getBlockAt(x, y, z);
		Block b2 = world.getBlockAt( xFlag, y, z );
		Block b3 = world.getBlockAt( x, y, zFlag );
		
		markers.add( new Marker( b1, b2, b3 ) );
		
		
		
	}
	
	
	public void clearMarkers( Player player ) {
		
		this.unhighlight( player );		
		this.markers.clear();
		
	}
	
}
