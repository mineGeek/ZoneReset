package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.MovementMonitor;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;



public class Utilities {


	public static Map<String, List<String> > playersByChunkSig = new HashMap<String, List<String>>();
	public static Map<String, String> playersLastChunkSig = new HashMap<String, String>();
	
	public static ZoneReset plugin;
	
	public static String getChunkSig( Location l ) {
		return l.getWorld().getName() + "|" + l.getChunk().getX() + "|" + l.getChunk().getZ();
	}
	
	public static String getChunkSig( Chunk c ) {
		return c.getWorld().getName() + "|" + c.getX() + "|" + c.getZ();
	}	
	
	public static void checkAllPlayerChunks() {
		
		playersByChunkSig.clear();
		playersLastChunkSig.clear();
		
		for ( Player p : Bukkit.getOnlinePlayers() ) {
			
			p.removeMetadata("ZRChunk", plugin );
			checkPlayerChunk(p);
			
		}
		
	}
	
	public static void checkPlayerChunk( Player p ) {
		checkPlayerChunk(p, p.getLocation() );
	}
	
	public static void checkPlayerChunk( Player p, Location l ) {
		
    	String chunkSig = getChunkSig( l.getChunk() );
    	
    	if ( p.hasMetadata("ZRChunk") ) {
    		
    		if ( !p.getMetadata("ZRChunk").get(0).asString().equals( chunkSig ) ) {
    			Utilities.addPlayerByChunk( chunkSig, p.getName() );
    			//p.sendMessage("new chunk " + chunkSig );
    		}
    		
    	} else {
    		Utilities.addPlayerByChunk(chunkSig, p.getName() );
    	}
    	
		p.setMetadata("ZRChunk",  new FixedMetadataValue( Bukkit.getPluginManager().getPlugin("ZoneReset"), chunkSig ) );		
		
	}
	
	public static void addPlayerByChunk( String chunkSig, String playerName ) {
		
		if ( playersLastChunkSig.containsKey( playerName ) ) {
			if ( !chunkSig.equals( playersLastChunkSig.get(playerName) ) ) {
				playersByChunkSig.get( playersLastChunkSig.get(playerName ) ).remove( playerName );
			}
		}
		
		playersLastChunkSig.put( playerName, chunkSig );
		
		if ( playersByChunkSig.containsKey( chunkSig ) ) {
			playersByChunkSig.get( chunkSig ).add( playerName );
		} else {
			playersByChunkSig.put( chunkSig, new ArrayList<String>( Arrays.asList( playerName ) ) );
			
		}
		
	}
	
	public static void removePlayerFromChunks( Player p ) {
		
		if ( playersLastChunkSig.containsKey( p.getName() ) ) {
			playersByChunkSig.get( playersLastChunkSig.get( p.getName() ) ).remove( p.getName() );			
			playersLastChunkSig.remove( p.getName() );
		}
		
		p.removeMetadata("ZRChunk", plugin );
		
		
		
	}
	
	public static List<String> getPlayersNearZone( Location ne, Location sw ) {
		
		List<String> list = new ArrayList<String>();

		if ( ne == null || sw == null ) {
			return list;
		}
		
		List<String> chunks = getChunkSigsFromArea( ne, sw );
		
		if ( !chunks.isEmpty() && !playersByChunkSig.isEmpty() ) {
			
			for ( String chunk : chunks ) {
				
				if ( playersByChunkSig.containsKey( chunk ) ) {
					list.addAll(playersByChunkSig.get( chunk ) );
				}
				
			}
			
			
		}
		
		return list;		
		
	}
	
	public static List<String> getPlayersNearZone( Area area ) {
		return getPlayersNearZone( area.ne(), area.sw() );
	}
	
	public static List<String> getPlayersNearZone( Zone zone ) {
		return getPlayersNearZone( zone.getArea() );
	}
	
	public static List<String> getChunkSigsFromArea( Area a ) {
		return getChunkSigsFromArea( a.ne(), a.sw() );
	}
	
	public static List<String> getChunkSigsFromArea( Location ne, Location sw ) {
		
		List<Chunk> chunks = getChunksFromArea( ne, sw );
		List<String> list = new ArrayList<String>();
		
		if ( !chunks.isEmpty() ) {
			for ( Chunk c : chunks ) {
				list.add( Utilities.getChunkSig(c) );
			}
		}
		
		return list;
		
	}
	
	public static List<Chunk> getChunksFromArea( Area a ) {
		return getChunksFromArea( a.ne(), a.sw() );
	}
	
	public static List<Chunk> getChunksFromArea( Location ne, Location sw ) {
		
		List<Chunk> list = new ArrayList<Chunk>();
		
		int fromX = ( (int)ne.getX()/16) -1 ;
		int toX = ( (int)sw.getX()/16) + 1;
		
		int fromZ = ( (int)ne.getZ()/16) - 1;
		int toZ = ( (int)sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {
			
			for ( int z = fromZ; z <= toZ; z++) {
				list.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		return list;
		
	}
	
	
	public static void startAllZones() {
		
		for ( Zone z : Zones.getZones().values() ) 	z.start();		
	}
	
	
	public static boolean zoneHasPlayers( Zone zone ) {
		return zoneHasPlayers( zone.getArea() );
	}
	
	public static boolean zoneHasPlayers( Area area ) {
		
		Server server = Bukkit.getServer();
		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return false;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getLocation() ) )  {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	
	public static Long getSecondsFromText( String value ) {
		
		
		if ( value == null ) return 0L;
		
		try {
			Matcher match = Pattern.compile("(?:(-?))?(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?").matcher( value );
			Long secs = (long) 0;	
			boolean negative = false;
			
			if ( match.find() ) {

				if ( match.group(1).equals( "-")  ) negative = true;
				if ( match.group(2) != null ) secs = 60*60*24 * Long.parseLong( match.group(2) );
				if ( match.group(3) != null ) secs += 60*60* Long.parseLong( match.group(3) );
				if ( match.group(4) != null ) secs += 60* Long.parseLong( match.group(4) );
				if ( match.group(5) != null ) secs += Long.parseLong( match.group(5) );
				
			}
		
			return ( negative ? secs * -1 : secs );
			
		} catch ( Exception e ) {}
		
		return null;
		
	}
	
	public static String getTimeStampAsString( Long timeStamp ) {
		
		return getTimeStampAsString( (int) (timeStamp/1000) );
		
	}	
	
	public static String getTimeStampAsString( int timeStamp ) {
		
		int sec = 	(timeStamp % 60 );
		int min = 	(timeStamp * 60) % 60;
		int hours = (timeStamp *60*60) % 24;
		int days = 	(timeStamp *60*60*24) % 7;
		int weeks = (timeStamp *60*60*24*7); 
  
		String result = null;
		if ( weeks > 0 ) 	result = weeks + " week" + ( weeks == 1 ? "" : "s");
		if ( days > 0) 		result = ( result.length() > 0 ? ", " : "" ) + days + " day"  	+ ( days == 1 ? "" : "s");
		if ( hours > 0) 	result = ( result.length() > 0 ? ", " : "" ) + hours + " hour"  + ( hours == 1 ? "" : "s");
		if ( min > 0) 		result = ( result.length() > 0 ? ", " : "" ) + min + " minute" 	+ ( min == 1 ? "" : "s");
		if ( sec > 0) 		result = ( result.length() > 0 ? ", " : "" ) + sec + " second" 	+ ( sec == 1 ? "" : "s");
		
		return result;
		
	}	
	
	public static void clearPlayerMetaData( Player p ) {
		
		Plugin plug = plugin;
		String[] keys = {"zra", "zrinteract" };
		
		for ( String x : keys ) {
			p.removeMetadata( x , plug );
		}
		
		try {
			((MovementMonitor)p.getMetadata("ZRMM").get(0).value()).close();
			p.removeMetadata("ZRMM", plug );
		} catch (Exception e ) {}
		
		
		
	}
	
	
	public static List<Chunk> getChunksInArea( Area area ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    
		Location ne = area.ne();
		Location sw = area.sw();
		
		int fromX = ( (int)ne.getX()/16) -1 ;
		int toX = ( (int)sw.getX()/16) + 1;
		
		int fromZ = ( (int)ne.getZ()/16) - 1;
		int toZ = ( (int)sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {

			chunks.add( ne.getWorld().getChunkAt( x, fromZ ) );
			
			for ( int z = fromZ; z <= toZ; z++) {
				chunks.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}
		
		return chunks;
		
	}
	
}
