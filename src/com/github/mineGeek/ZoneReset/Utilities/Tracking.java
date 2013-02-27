package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;


public class Tracking {

	private static Map<Integer, Track> chunks = new HashMap<Integer,Track>();
	private static Map<String, Integer> playerToChunkMap = new HashMap<String, Integer>();
	private static Map<Integer, List<String>> chunkToPlayersMap = new HashMap<Integer, List<String>>();
	
	public static void clear() {
		chunks.clear();
		playerToChunkMap.clear();
		chunkToPlayersMap.clear();
	}
	
	public static void add( int chunkSig, Track track ) {

		chunks.put( chunkSig, track );
	}
	
	public static void playerMove( Player p ) {
		playerMove( p, p.getLocation() );
	}
	
	public static void updatePlayerChunkMap( Player p ) {
		
		if ( playerToChunkMap.containsKey( p.getName() ) && playerToChunkMap.get( p.getName() ) != p.getLocation().getChunk().hashCode() ) {
			if ( Config.debug_area_chunkChange ) p.sendMessage( "new chunk: " + p.getLocation().getChunk().hashCode() );
			Integer i = playerToChunkMap.get( p.getName() );
			if (chunkToPlayersMap.containsKey(i) ) chunkToPlayersMap.get( i ).remove( p.getName() );
			if ( chunkToPlayersMap.containsKey( p.getLocation().getChunk().hashCode() ) ) {
				chunkToPlayersMap.get( p.getLocation().getChunk().hashCode() ).add( p.getName() );
			} else {
				chunkToPlayersMap.put( p.getLocation().getChunk().hashCode(), new ArrayList<String>( Arrays.asList( p.getName() ) ) );
			}
		} else if ( !playerToChunkMap.containsKey( p.getName() ) ) {
			playerToChunkMap.put( p.getName(), p.getLocation().getChunk().hashCode() );
		}
	}
	
	public static void playerMove( Player p, Location to ) {

		if ( chunks.containsKey( to.getChunk().hashCode() ) ) {
			
			chunks.get( to.getChunk().hashCode() ).run( p );
		}
		
	}
	
	
	public static void loadZones() {
		
		chunks.clear();
		List<Chunk> chunkList = new ArrayList<Chunk>();
		
		for ( Zone z : Zones.getZones().values() ) {
			
			if ( z.getArea().ne() != null && z.getArea().sw() != null ) {
				chunkList.addAll( getChunksFromArea(  z.getArea().ne(), z.getArea().sw() ) );
			}
			
		}
		
	}
	
	public static List<String> getPlayersInZone( String zone ) {
		
		return getPlayersInZone( Zones.getZone( zone ) );
		
	}
	
	public static List<String> getPlayersInZone( Zone zone ) {
		
		List<Chunk> chunkList = getChunksFromArea( zone.getArea().ne(), zone.getArea().sw() );
		List<String> players = new ArrayList<String>();
		
		if ( !chunkList.isEmpty() ) {
			
			for ( Chunk c : chunkList ) {
				
				if ( chunkToPlayersMap.containsKey( c.hashCode() ) ) players.addAll( chunkToPlayersMap.get( c.hashCode() ) );
				
			}
			
		}
		
		return players;
	}
	
	public static void add( Track track ) {
		
		chunks.clear();
		List<Chunk> chunkList = getChunksFromArea( track.area.ne(), track.area.sw() );
		
		if ( !chunkList.isEmpty() ) {
			for ( Chunk c : chunkList ) {
				add( c.hashCode(), track );
			}
		}
				
		
	}
	
	
	public static List<Chunk> getChunksFromArea( Location ne, Location sw ) {
		
		List<Chunk> list = new ArrayList<Chunk>();
		
		int fromX = (int)Math.floor( Math.min( ne.getChunk().getX(), sw.getChunk().getX() ) );
		int toX =   (int)Math.ceil( Math.max( ne.getChunk().getX(), sw.getChunk().getX() ) );
		
		int fromZ = (int)Math.floor( Math.min( ne.getChunk().getZ(), sw.getChunk().getZ() ) );
		int toZ = (int)Math.ceil( Math.max( ne.getChunk().getZ(), sw.getChunk().getZ() ) );
		
		for( int x = fromX; x <= toX; x++ ) {
			
			for ( int z = fromZ; z <= toZ; z++) {
				list.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		return list;
		
	}
	
	public static void close() {
		
		if ( !chunks.isEmpty() ) {
			for ( Track t : chunks.values() ) {
				t.close();
			}
			
			chunks.clear();
			chunks = null;
		}
		
	}
	
}
