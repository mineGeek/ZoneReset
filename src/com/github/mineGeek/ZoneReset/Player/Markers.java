package com.github.mineGeek.ZoneReset.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Utilities.Zone;

public class Markers {

	private static Map<String, List<Marker> > markers = new HashMap<String, List<Marker>>();
	public static boolean enabled;
	
	public static void showZoneBoundaries( Player p, Location ne, Location sw ) {
		
		
		if ( markers.containsKey( p.getName()) && !markers.get( p.getName() ).isEmpty() ) {
			hideZoneBoundaries( p );
		}
		
		if ( !p.hasMetadata("zr") ) return;
		
		List<Marker> l = getMarkers( ne, sw );
		
		if ( !l.isEmpty() ) {
			for ( Marker m : l ) {
				m.highlight( p );
			}
		}

		markers.put( p.getName(), l );		
		
	}
	
	public static void showZoneBoundaries( Player p, Zone z ) {
		
		showZoneBoundaries( p, z.getArea().ne(), z.getArea().sw() );
		
	}
	
	public static void hideZoneBoundaries( Player p ) {
		
		if ( markers.containsKey( p.getName() ) ) {
			
			List<Marker> l = markers.get( p.getName() );
			
			if ( !l.isEmpty() ) {
				for ( Marker m : l ) {
					m.unhighlight( p );
				}
			}
			
			markers.get( p.getName() ).clear();
			
		}
		
	}
	
	
	public static List<Marker> getMarkers( Location ne, Location sw ) {
		
		String worldName = ne.getWorld().getName();
		List<Marker> l = new ArrayList<Marker>();
		
		if ( ne != null && sw != null ) {
				
			int minX = Math.min( ne.getBlockX(), sw.getBlockX() ) ;
			int maxX = Math.max( ne.getBlockX(), sw.getBlockX() ) ;
			
			int minY = Math.min( ne.getBlockY(), sw.getBlockY() ) ;
			int maxY = Math.max( ne.getBlockY(), sw.getBlockY() ) ;
			
			int minZ = Math.min( ne.getBlockZ(), sw.getBlockZ() ) ;
			int maxZ = Math.max( ne.getBlockZ(),  sw.getBlockZ() );
			
			
			l.add( getMarker( worldName, minX, minY+1, minZ, minX+1, minZ+1 ) );
			l.add( getMarker( worldName, maxX, minY+1, minZ, maxX-1, minZ+1 ) );
			l.add( getMarker( worldName, minX, minY+1, maxZ, minX+1, maxZ-1 ) );
			l.add( getMarker( worldName, maxX, minY+1, maxZ, maxX-1, maxZ-1 ) );			

			l.add( getMarker( worldName, minX, maxY-1, minZ, minX+1, minZ+1 ) );
			l.add( getMarker( worldName, maxX, maxY-1, minZ, maxX-1, minZ+1 ) );
			l.add( getMarker( worldName, minX, maxY-1, maxZ, minX+1, maxZ-1 ) );
			l.add( getMarker( worldName, maxX, maxY-1, maxZ, maxX-1, maxZ-1 ) );
			
		} else if ( ne != null && sw==null ) {
			l.add( getMarker(  ne ) );
		} else if ( sw != null ) {
			l.add( getMarker(  sw ) );
			
		}
		
		return l;
		
	}
	
	public static Marker getMarker( Location l ) {
		return getMarker( l.getWorld().getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getBlockX(), l.getBlockZ() );
	}
	
	public static Marker getMarker( String worldName, int x, int y, int z, int xFlag, int zFlag ) {
		
		World world = Bukkit.getWorld( worldName );
		Block b1 = world.getBlockAt(x, y, z);
		Block b2 = world.getBlockAt( xFlag, y, z );
		Block b3 = world.getBlockAt( x, y, zFlag );
		
		return new Marker( b1, b2, b3 );
		
	}
	
	public static void close() {
		
		if ( markers != null && !markers.isEmpty() ) {
			
			for ( String x : markers.keySet() ) {
				
				Player p = Bukkit.getPlayer(x);
				
				if ( p != null && p.isOnline() ) {
					hideZoneBoundaries( p );
				}
				
			}
			
			if ( markers.values().isEmpty() ) {
				for ( List<Marker> m : markers.values() ) {
					m.clear();
				}
			}
			markers.clear();
			
		}
		
	}
	
}
