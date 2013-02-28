package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;



public class Utilities {


	public static ZoneReset plugin;	
	
	public static void startAllZones() {
		
		for ( Zone z : Zones.getZones().values() ) 	z.start();		
	}
	
	
	public static boolean zoneHasPlayers( Zone zone ) {
		return !zone.getPlayers().isEmpty();
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
	
	public static String getTimeStampAsShorthand( int timeStamp ) {
		
		int sec = 	(timeStamp % 60 );
		int min = 	(timeStamp / 60) % 60;
		int hours = (timeStamp / (60*60 ) ) % 24;
		int days = 	(timeStamp / (60*60*24 ) ) % 7;
		int weeks = (timeStamp / (60*60*24*7)); 
  
		String result = "";
		if ( weeks > 0 ) 	result = weeks + "w";
		if ( days > 0) 		result = result + days + "d";
		if ( hours > 0) 	result = result + hours + "h";
		if ( min > 0) 		result = result + min + "m";
		if ( sec > 0) 		result = result + sec + "s";
		
		return result;
		
	}	
	
	public static String getTimeStampAsString( Long timeStamp ) {
		
		return getTimeStampAsString( (int) (timeStamp/1000) );
		
	}	
	
	public static String getTimeStampAsString( int timeStamp ) {
		
		int sec = 	( timeStamp % 60 );
		int min = 	( timeStamp / 60) % 60;
		int hours = ( timeStamp / (60*60 ) ) % 24;
		int days = 	( timeStamp / (60*60*24) ) % 7;
		int weeks = ( timeStamp / (60*60*24*7) ); 
  
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
