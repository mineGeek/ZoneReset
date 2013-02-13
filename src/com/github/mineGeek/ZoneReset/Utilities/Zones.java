package com.github.mineGeek.ZoneReset.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Spawners.ItemSpawn;
import com.github.mineGeek.ZoneReset.Spawners.MobSpawner;
import com.github.mineGeek.ZoneReset.Spawners.SpawnContainer;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;
import com.github.mineGeek.ZoneReset.Utilities.Zone.ZRTrigger;

public class Zones {

	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	private static Map<String, String> interactKeys = new HashMap<String, String>();
	
	public static Zone getZone( String tag ) {
		return zones.get( tag );
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
	
	public static void trigger( ZRTrigger type, Material m, Location l ) {
		
		if ( type == ZRTrigger.ZR_ONINTERACT ) {
			
		}
		
	}
	
	
	public static void triggerPlayerJoin( Player p ) {
		
		for ( Zone z : zones.values() ) {
			
			if ( z.isOnPlayerJoin() ) {
				z.restore();
			} else if ( z.getOnPlayerJoinList().contains( p.getName() ) ) {
				z.restore();
			}
			
		}
		
	}
	
	public static void triggerPlayerQuit( Player p ) {
		
		for ( Zone z : zones.values() ) {
			
			if ( z.isOnPlayerQuit() ) {
				z.restore();
			} else if ( z.getOnPlayerQuitList().contains( p.getName() ) ) {
				z.restore();
			}
			
		}
		
	}
	
	public static void triggerInteract( Material m, Location l ) {

		
		String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + m.getId();
		Bukkit.getLogger().info( i );
		
		for ( String x : interactKeys.keySet() ) {
			Bukkit.getLogger().info( x );
		}
		
		if ( interactKeys.containsKey(i) ) {
		
			Zone z = zones.get( interactKeys.get(i) );
			if ( z != null ) z.restore();
			
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
		

		/**
		 * Remove all entities before reset?
		 */
		r.setKillEntities( c.getBoolean("pre.removeEntities", false ) );
		r.setKillEntityExceptions( c.getStringList( "pre.keepEntities") ) ;
		
		/**
		 * Remove any spawnPoints?
		 */
		r.setRemoveSpawnPoints( c.getBoolean("pre.removeSpawnPoints", false ) );
		
		
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
			r.setResetSpawnPoints( spawnWorldName, l.get(0), l.get(1), l.get(2) );				
			
		}
		
		
		/**
		 * Set players inventory
		 */
		r.clearPlayerInventory();
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
			r.setTransportPlayers( c.getString("pre.movePlayers.world", worldName ), l.get(0), l.get(1), l.get(2) );
		}
		
		
		/**
		 * Set schedule for reset
		 */
		if ( c.isSet( "trigger") ) {
			
			if ( c.isSet( "trigger.onPlayerJoin") ) {
				
				r.setOnPlayerJoin( c.getBoolean("trigger.onPlayerJoin", false ) );
				
			}
			
			if ( c.isSet( "trigger.whenPlayersJoin" ) ) {
				r.setOnPlayerJoinList( c.getStringList("trigger.whenPlayersJoin") );
			}
			
			if ( c.isSet( "trigger.onPlayerQuit") ) {
				
				r.setOnPlayerQuit( c.getBoolean("trigger.onPlayerQuit", false ) );
				
			}
			
			if ( c.isSet( "trigger.whenPlayersQuit" ) ) {
				r.setOnPlayerQuitList( c.getStringList("trigger.whenPlayersQuit") );
			}			
			
			if ( c.isSet( "trigger.onTimer") ) {
				r.setOnMinutesFormat( c.getString( "trigger.onTimer") );
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
			
			for ( Zone z : zones.values() ) {
				z.close();
			}
			zones.clear();	
		}
		
		zones = null;
	}
	
}
