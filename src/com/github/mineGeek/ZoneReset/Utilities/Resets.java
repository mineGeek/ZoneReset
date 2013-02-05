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
import org.bukkit.entity.EntityType;

import com.github.mineGeek.ChallengeMe.Utilities.EntityLocation;

public class Resets {

	private static Map<String, RestoreSection> sections = new HashMap<String, RestoreSection>();
	
	public static RestoreSection getSection( String tag ) {
		return sections.get( tag );
	}
	
	public static void addSection( String tag, ConfigurationSection c ) {
		
		RestoreSection r = new RestoreSection();

		
		/**
		 * Unique name for Zone
		 */
		r.tag = c.getString("tag");		
		
		
		/**
		 * Set world name for Zone and cuboid
		 */
		String worldName = c.getString( "worldName", "world" );
		List<Integer> ne = c.getIntegerList( "ne" );
		List<Integer> sw = c.getIntegerList( "sw" );
		Area a = new Area( Bukkit.getWorld( worldName ), ne, sw );
		r.area = a;
		

		/**
		 * Remove all entities before reset?
		 */
		r.killEntities = c.getBoolean("entities.remove", false );
		r.setKillEntityExceptions( c.getStringList( "entities.exceptions") ) ;
		
		/**
		 * Remove any spawnPoints?
		 */
		r.removeSpawnPoints = c.getBoolean("actions.removeSpawnPointsInArea", false );
		
		
		/**
		 * Require zone to be void of humans?
		 */
		r.requireNoPlayers = c.getBoolean("requirements.noPlayersInArea", false);
		
		
		/**
		 *  Set players in cuboids new spawnpoint?
		 */
		if ( c.isSet( "actions.setSpawn" ) ) {
			
			World world = Bukkit.getWorld( worldName );
			double x = c.getDouble("actions.setSpawn.x");
			double y = c.getDouble("actions.setSpawn.y");
			double z = c.getDouble("actions.setSpawn.z");
			
			r.resetSpawnPoints = new Location( world, x, y, z );
			
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
			
			r.spawnEntities = list;
			
			
		}
		
		
		/**
		 * Move players in Zone to specific point on reset
		 */
		if ( c.isSet( "actions.movePlayers") ) {
			List<Integer> l = c.getIntegerList( "actions.movePlayers" );
			r.transportPlayers = new Location( Bukkit.getWorld( worldName), l.get(0), l.get(1), l.get(2) );
		}
		

		/**
		 * Specify specific snapshot name? Null for last snapshot
		 */
		if ( c.isSet( "snapshot") ) r.snapShotName = c.getString("snapshot");
		
		
		/**
		 * Set schedule for reset
		 */
		if ( c.isSet( "reset") ) {
			
			if ( c.isSet( "reset.onPlayerJoin") ) {
				
				r.onPlayerJoin = c.getBoolean("reset.onPlayerJoin", false );
				if ( r.onPlayerJoin ) {
					r.onPlayerJoinList = c.getStringList("reset.onPlayerJoin");
				}
				
			}
			
			if ( c.isSet( "reset.onPlayerQuit") ) {
				
				r.onPlayerQuit = c.getBoolean("reset.onPlayerQuit", false );
				if ( r.onPlayerQuit ) {
					r.onPlayerJoinQuit = c.getStringList("reset.onPlayerQuit");
				}
				
			}
			
			if ( c.isSet( "reset.onMinutes") ) {
				r.onMinutes = c.getInt( "reset.onMinutes");
			}
			
			if ( c.isSet("reset.onInteract") ) {
				List<Integer> l = c.getIntegerList("reset.onInteract");
				r.onInteractLocation = new Location( Bukkit.getWorld( worldName), l.get(0), l.get(1), l.get(2) );
			}
			
			
		}
		
	}
	
	
}
