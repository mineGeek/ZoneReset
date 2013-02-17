package com.github.mineGeek.ZoneReset.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.DataStore;
import com.github.mineGeek.ZoneReset.Messaging.Message;
import com.github.mineGeek.ZoneReset.Messaging.Message.ZRMessageType;
import com.github.mineGeek.ZoneReset.Spawners.ItemSpawn;
import com.github.mineGeek.ZoneReset.Spawners.MobSpawner;
import com.github.mineGeek.ZoneReset.Spawners.SignSpawn;
import com.github.mineGeek.ZoneReset.Spawners.SpawnContainer;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;
import com.github.mineGeek.ZoneReset.Utilities.Zone.ZRMethod;

public class Zones {

	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	private static Map<String, String> interactKeys = new HashMap<String, String>();

	
	
	public static Zone getZone( String tag ) {
		return zones.get( tag );
	}
	
	
	public static void loadDataZones() {
		
		if ( !zones.isEmpty() ) {
			for ( Zone z : zones.values() ) {
				loadDataZone( z );
			}
		}
		
	}
	public static void loadDataZone( Zone zone ) {
		
		DataStore o = new DataStore( Config.folderZones );
		o.setFileName( zone.getTag() );
		o.load();
		zone.setLastResetMethod( ZRMethod.valueOf( o.getAsString( "lastResetMethod", "NONE") ) );
		zone.setLastReset( o.getAsLong( "lastReset", null ) );
		zone.setLastTimedReset( o.getAsLong( "lastTimedRest", null ) );
		
	}

	
	public static void saveDataZones() {
		
		if ( !zones.isEmpty() ) {
			for ( Zone z : zones.values() ) {
				saveZoneData( z );
			}
		}
		
	}	
	public static void saveZoneData( Zone zone ) {
		
		DataStore o = new DataStore( Config.folderZones );
		o.setFileName( zone.getTag() );
		o.set("lastResetMethod", zone.getLastResetMethod().toString() );
		o.set("lastReset", zone.getLastReset() );
		o.set("lastTimedReset", zone.getLastTimedReset() );
		o.save();
		
	}
	
	
	public static Map<String, Zone> getZones() {
		return zones;
	}
	
	public static void loadInteractKeys() {
		
		interactKeys.clear();
		
		for ( Zone z : zones.values() ) {
			if ( z.getOnInteractLocation() != null ) {
				Location l = z.getOnInteractLocation();
				String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + z.getOnInteractMaterialId();
				Bukkit.getLogger().info( i );
				interactKeys.put(i, z.getTag() );
			}
		}
		
	}
	
	public static void triggerPlayerJoin( Player p ) {
		
		for ( Zone z : zones.values() ) {
			
			if ( z.isTrigOnPlayerJoin() ) {
				z.reset( ZRMethod.ONJOIN );
			} else if ( z.getTrigOnPlayerJoinList().contains( p.getName() ) ) {
				z.reset( ZRMethod.ONJOIN );
			}
			
		}
		
	}
	
	public static void triggerPlayerQuit( Player p ) {
		
		for ( Zone z : zones.values() ) {
			
			if ( z.isTrigOnPlayerQuit() ) {
				z.reset( ZRMethod.ONQUIT );
			} else if ( z.getTrigOnPlayerQuitList().contains( p.getName() ) ) {
				z.reset( ZRMethod.ONQUIT );
			}
			
		}
		
	}
	
	public static void triggerInteract( Material m, Location l ) {

		
		String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + m.getId();
		//Bukkit.getLogger().info( i );
		
		//for ( String x : interactKeys.keySet() ) {
			//Bukkit.getLogger().info( x );
		//}
		
		if ( interactKeys.containsKey(i) ) {
		
			Zone z = zones.get( interactKeys.get(i) );
			if ( z != null ) z.reset( ZRMethod.ONINTERACT );
			
		}
		
	}	
	
	@SuppressWarnings("unchecked")
	public static void addZone( String tag, ConfigurationSection c ) {
		
		Zone r = new Zone();

		
		/**
		 * Unique name for Zone
		 */
		r.setTag( tag );		
		
		
		/**
		 * Set world name for Zone and cuboid
		 */
		String worldName = c.getString( "world", "world" );
		
		r.setWorldName( worldName );
		
		if ( c.isSet( "ne" ) && c.isSet( "sw") ) {
			List<Integer> ne = c.getIntegerList( "ne" );
			List<Integer> sw = c.getIntegerList( "sw" );
			Area a = new Area( worldName, ne, sw );
			r.setArea( a );
		}
		
		
		if ( c.isSet( "messages") ) {
			
			List<Map<?, ?>> messages = c.getMapList("messages.timed");
			
			for ( Map<?, ?> message : messages ) {
				
				ZRMessageType scope = ZRMessageType.SERVER;
				String timeText = null;				
				if ( message.containsKey( "scope") ) scope = ZRMessageType.valueOf( message.get( "scope").toString().toUpperCase() );
				if ( message.containsKey("time") ) timeText = (String) message.get("time");
				
				
				Message m = new Message( scope, (String) message.get("message"));
				m.timeText = timeText;
				m.zoneTag = tag;
				r.addTimedMessages( m );
				
			}
			
		}
		
		

		/**
		 * Remove all entities before reset?
		 */
		r.setPreNoMobs( c.getBoolean("pre.removeEntities", false ) );
		r.setPreNoMobsExceptionList( c.getStringList( "pre.keepEntities") ) ;
		
		/**
		 * Remove any spawnPoints?
		 */
		r.setPreNoSpawns( c.getBoolean("pre.removeSpawnPoints", false ) );
		
		
		/**
		 * Require zone to be void of humans?
		 */
		r.setRequireNoPlayers( c.getBoolean("requirements.noPlayers", false) );
		
		
		/**
		 *  Set players in cuboids new spawnpoint?
		 */
		if ( c.isSet( "pre.setSpawn" ) ) {
			List<Integer> l = c.getIntegerList( "pre.setSpawn.location" );
			String spawnWorldName = c.getString("pre.setSpawn.world", worldName );
			r.setPreSpawnLocation( spawnWorldName, l.get(0), l.get(1), l.get(2) );				
			
		}
		
		
		/**
		 * Set players inventory
		 */
		//r.clearPlayerInventory();
		if ( c.isSet( "post.setInventory") ) {
			
			List<Map<?, ?>> items = c.getMapList("post.playerInventory");
			
			if ( !items.isEmpty() ) {
				
				for ( Map<?, ?> item : items ) {
					ItemSpawn i = new ItemSpawn();
					i.setWorldName( worldName );
					i.setList( (Map<String, Object>) item );
					r.addSpawn( ZRSPAWNTYPE.INVENTORY, i );
				}
				
			}
			
		}
		
		if ( c.isSet("post.spawnMobs") ) {
			
			List<Map<?, ?>> mobs = c.getMapList("post.spawnMobs");
			
			if ( !mobs.isEmpty() ) {
				
				for ( Map<?, ?> mob : mobs ) {
					MobSpawner mobItem = new MobSpawner();
					mobItem.setWorldName( worldName );
					mobItem.setList( (Map<String, Object>) mob );
					r.addSpawn( ZRSPAWNTYPE.MOB, mobItem );
				}
				
			}
			
		}
		
		if ( c.isSet("post.spawnSigns") ) {
			
			List<Map<?, ?>> signs = c.getMapList("post.spawnSigns");
			
			if ( !signs.isEmpty() ) {
				
				for ( Map<?, ?> sign : signs ) {
					SignSpawn s = new SignSpawn();
					s.setWorldName( worldName );
					s.setList( (Map<String, Object>) sign );
					r.addSpawn( ZRSPAWNTYPE.SIGN, s );
				}
				
			}
			
		}		
		
		
		if ( c.isSet( "post.spawnItems") ) {
			
			List< Map<?, ?> > spawns = c.getMapList( "post.spawnItems");
			
			if ( !spawns.isEmpty() ) {
				
				for ( Map<?, ?> spawn : spawns ) {
					
					SpawnInterface si = null;
					
					if ( spawn.containsKey( "type") ) {
						
						if ( ZRSPAWNTYPE.valueOf( spawn.get("type").toString().toUpperCase() ).equals( ZRSPAWNTYPE.ITEM ) ) {
							si = new ItemSpawn();
						} else if ( ZRSPAWNTYPE.valueOf( spawn.get("type").toString().toUpperCase() ).equals( ZRSPAWNTYPE.CONTAINER ) ) {
							si = new SpawnContainer();
						}
						
					}
					
					if ( si != null ) {
						si.setWorldName( worldName );
						si.setList( (Map<String, Object>) spawn );
						r.addSpawn( ZRSPAWNTYPE.valueOf( spawn.get("type").toString().toUpperCase() ), si);
					}

				}
				
			}
			
		}
		
		
		/**
		 * Move players in Zone to specific point on reset
		 */
		if ( c.isSet( "pre.movePlayers") ) {
			List<Integer> l = c.getIntegerList( "pre.movePlayers.location" );
			r.setPreNewLocation( c.getString("pre.movePlayers.world", worldName ), l.get(0), l.get(1), l.get(2) );
		}
		
		
		/**
		 * Set schedule for reset
		 */
		if ( c.isSet( "trigger") ) {
			
			if ( c.isSet( "trigger.onPlayerJoin") ) {
				
				r.setTrigOnPlayerJoin( c.getBoolean("trigger.onPlayerJoin", false ) );
				
			}
			
			if ( c.isSet( "trigger.whenPlayersJoin" ) ) {
				r.setTrigOnPlayerJoinList( c.getStringList("trigger.whenPlayersJoin") );
			}
			
			if ( c.isSet( "trigger.onPlayerQuit") ) {
				
				r.setTrigOnPlayerQuit( c.getBoolean("trigger.onPlayerQuit", false ) );
				
			}
			
			if ( c.isSet( "trigger.whenPlayersQuit" ) ) {
				r.setTrigOnPlayerQuitList( c.getStringList("trigger.whenPlayersQuit") );
			}			
			
			if ( c.isSet( "trigger.onTimer") ) {
				r.setTrigTimerText( c.getString( "trigger.onTimer") );
			}
			
			if ( c.isSet("trigger.onInteract.location") ) {
				r.setOnInteractMaterialId( c.getInt("trigger.onInteract.item", 0));
				List<Integer> l = c.getIntegerList("trigger.onInteract.location");
				r.setOnInteractLocation( c.getString("trigger.onInteract.world", worldName), l.get(0), l.get(1), l.get(2) );
			}
			
			
		}
		
		Zones.zones.put( tag , r );
		
	}
	
	
	
	public static int count() {
		return zones.size();
	}
	
	
	public static void close() {
		
		if ( !zones.isEmpty() ) {
			
			Zones.saveDataZones();
			
			for ( Zone z : zones.values() ) {
				z.close();
			}
			zones.clear();	
		}
		
		zones = null;
	}
	
}
