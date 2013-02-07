package com.github.mineGeek.ZoneReset.Utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class Utilities {

	
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
	
	
	public static void resetZoneSpawnPoints( Zone zone ) {
		resetZoneSpawnPoints( zone.getArea(), zone.getResetSpawnPoints() );
	}
	
	public static void resetZoneSpawnPoints( Area area, Location location ) {
		
		Server server = Bukkit.getServer();		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getBedSpawnLocation() ) )  {
				p.setBedSpawnLocation( location, true );
			}
			
		}		
		
		
	}
	
	public static void movePlayersInZone( Zone zone ) {
		movePlayersInZoneTo( zone.getArea(), zone.getTransportPlayers() );
	}
	
	public static void movePlayersInZoneTo( Area area, Location destination ) {
		
		Server server = Bukkit.getServer();
		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getLocation() ) )  {
				p.teleport( destination );
			}
			
		}
		
	}
	
	
	public static void clearZoneOfEntities( Zone zone ) {
		clearLocationOfEntities( zone.getArea(), zone.getKillEntityExceptions() );
	}
	
	public static void clearLocationOfEntities( Area area, List<EntityType> exclusions ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    
		int fromX = ( (int)area.ne.getX()/16) -1 ;
		int toX = ( (int)area.sw.getX()/16) + 1;
		
		int fromZ = ( (int)area.ne.getZ()/16) - 1;
		int toZ = ( (int)area.sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {

			chunks.add( area.ne.getWorld().getChunkAt( x, fromZ ) );
			
			for ( int z = fromZ; z <= toZ; z++) {
				chunks.add( area.ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		if ( chunks.size() > 0 ) {

			for ( Chunk chunk : chunks ) {

				for( Entity e : chunk.getEntities()) {

					if ( !exclusions.contains( e.getType() ) ) { 
						if ( area.intersectsWith( e.getLocation() ) ) {
							e.remove();
						}
					}
				}

			}
			
		}
		
		
	}
	
	
	public static void spawnEntitiesInZone( Zone zone ) {
		spawnEntities( zone.getWorldName(), zone.getSpawnEntities() );
	}
	
	public static void spawnEntities( String worldName, List< EntityLocation> list ) {
		
		if ( list.size() > 0  ) {
			
			World world = Bukkit.getServer().getWorld( worldName );
			
			for ( EntityLocation e : list ) {
				
				world.spawnEntity( e.location, e.entityType );
				
			}
			
		}
		
		
	}
	
	public static void setZone() {
		//247,69, 241
		//256, 61, 208
		World world = Bukkit.getWorld( "pickleMasherd");
		Location ne = new Location( world, 247, 69, 241 );
		Location sw = new Location( world, 256, 61, 208 );
		
		
		ZoneBlocks z = new ZoneBlocks( ne, sw );
		
		z.setBlocks();
		
        FileOutputStream fileOut;
		try {

			fileOut = new FileOutputStream( Config.snapShotFolder + File.separator + "employee.ser");
	        ObjectOutputStream out =  new ObjectOutputStream(fileOut);
	        out.writeObject( z );
	        out.close();
	        fileOut.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void loadZone() {
		
		ZoneBlocks z = null;
		
		try {
			FileInputStream fileIn = new FileInputStream( Config.snapShotFolder + File.separator + "employee.ser" );
			ObjectInputStream in = new ObjectInputStream( fileIn );
			z = ( ZoneBlocks ) in.readObject();
			in.close();
			fileIn.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
