package com.github.mineGeek.ZoneReset.Utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Zones {

	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	
	public static Zone getZone( String tag ) {
		return zones.get( tag );
	}
	
	public static Map<String, Zone> getZones() {
		return zones;
	}
	
	
	public static void addZone( String tag, ConfigurationSection c ) {
		
		Zone r = new Zone();

		
		/**
		 * Unique name for Zone
		 */
		r.setTag( c.getString("tag") );		
		
		
		/**
		 * Set world name for Zone and cuboid
		 */
		String worldName = c.getString( "worldName", "world" );
		List<Integer> ne = c.getIntegerList( "ne" );
		List<Integer> sw = c.getIntegerList( "sw" );
		Area a = new Area( Bukkit.getWorld( worldName ), ne, sw );
		r.setArea( a );
		

		/**
		 * Remove all entities before reset?
		 */
		r.setKillEntities( c.getBoolean("entities.remove", false ) );
		r.setKillEntityExceptions( c.getStringList( "entities.exceptions") ) ;
		
		/**
		 * Remove any spawnPoints?
		 */
		r.setRemoveSpawnPoints( c.getBoolean("actions.removeSpawnPointsInArea", false ) );
		
		
		/**
		 * Require zone to be void of humans?
		 */
		r.setRequireNoPlayers( c.getBoolean("requirements.noPlayersInArea", false) );
		
		
		/**
		 *  Set players in cuboids new spawnpoint?
		 */
		if ( c.isSet( "actions.setSpawn" ) ) {
			
			World world = Bukkit.getWorld( worldName );
			double x = c.getDouble("actions.setSpawn.x");
			double y = c.getDouble("actions.setSpawn.y");
			double z = c.getDouble("actions.setSpawn.z");
			
			r.setResetSpawnPoints( new Location( world, x, y, z ) );
			
		}
		
		
		/**
		 * Spawn any specific entities
		 */
		if ( c.isSet("actions.spawnEntities") ) {
			
			
			List<EntityLocation> list = new ArrayList<EntityLocation>();
			
				
			List<?> spawnList = c.getList( "actions.spawnEntities" );			
			Iterator<?> i = spawnList.iterator();
			while ( i.hasNext() ) {
				@SuppressWarnings("unchecked")
				List<Object> l = (List<Object>) i.next();
				String eName = l.get(0).toString();
				Integer x = (Integer)l.get(1);
				Integer y = (Integer)l.get(2);
				Integer z = (Integer)l.get(3);				
				
				EntityLocation e = new EntityLocation( eName, Bukkit.getWorld( worldName ), x, y, z );
				list.add( e );
			}
			
			r.setSpawnEntities( list );
			
			
		}
		
		
		/**
		 * Move players in Zone to specific point on reset
		 */
		if ( c.isSet( "actions.movePlayers") ) {
			List<Integer> l = c.getIntegerList( "actions.movePlayers" );
			r.setTransportPlayers( new Location( Bukkit.getWorld( worldName), l.get(0), l.get(1), l.get(2) ) );
		}
		

		/**
		 * Specify specific snapshot name? Null for last snapshot
		 */
		if ( c.isSet( "snapshot") ) r.setSnapShotName( c.getString("snapshot") );
		
		
		/**
		 * Set schedule for reset
		 */
		if ( c.isSet( "reset") ) {
			
			if ( c.isSet( "reset.onPlayerJoin") ) {
				
				r.setOnPlayerJoin( c.getBoolean("reset.onPlayerJoin", false ) );
				if ( r.isOnPlayerJoin() ) {
					r.setOnPlayerJoinList( c.getStringList("reset.onPlayerJoin") );
				}
				
			}
			
			if ( c.isSet( "reset.onPlayerQuit") ) {
				
				r.setOnPlayerQuit( c.getBoolean("reset.onPlayerQuit", false ) );
				if ( r.isOnPlayerQuit() ) {
					r.setOnPlayerQuitList( c.getStringList("reset.onPlayerQuit") );
				}
				
			}
			
			if ( c.isSet( "reset.onMinutes") ) {
				r.setOnMinutes( c.getInt( "reset.onMinutes") );
			}
			
			if ( c.isSet("reset.onInteract") ) {
				List<Integer> l = c.getIntegerList("reset.onInteract");
				r.setOnInteractLocation(  new Location( Bukkit.getWorld( worldName), l.get(0), l.get(1), l.get(2) ) );
			}
			
			
		}
		
	}
	
	
	public static int count() {
		return zones.size();
	}
	
	
	public static void close() {
		
		if ( !zones.isEmpty() ) {
			
			for ( Zone z : zones.values() ) {
				z.close();
			}
			zones.clear();	
		}
		
		zones = null;
	}
	
}
