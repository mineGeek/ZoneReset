package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;


/**
 * Utility wrapper for configuration
 *
 */
public class Config {


	
	/**
	 * The actual config file from getConfig()
	 */
	public static FileConfiguration c;
	
	/**
	 * Text to display to player when there are players still in the zone
	 */
	public static String txtPlayersStillInArea = "There are still players in the area";
	

	public static String snapShotFolder;
	
	/**
	 * Timeout of restriction messages to prevent flooding player
	 */
	public static int spamPlayerMessageTimeout = 1500;
	
	public static int wandId = 294;
	/**
	 * Debugging parameters
	 */
	public static boolean debug_area_chunkEntrance = false;
	public static boolean debug_area_chunkExit = false;
	public static boolean debug_area_chunkChange = false;
	
	
	
	
	/**
	 * Load all rules from the config
	 * @param c
	 */
	public static void loadZonesFromConfig( MemorySection c) {
		
		/**
		 * Load Zones
		 */
		if ( c.contains("zones") ) {
			
			for ( String x : c.getConfigurationSection("zones").getKeys( false ) ) {
				
				Zones.addZone( x, c.getConfigurationSection("zones." + x) );
				
			}
			
		}
		
		Zones.loadInteractKeys();
		Bukkit.getServer().getLogger().info("ZoneRest loaded " + Zones.count() + " rules total.");
		
	}
	
	
	
	
	/**
	 * Load other variables from config
	 */
	public static void loadConfig() {
		
		Config.spamPlayerMessageTimeout		= c.getInt( "playerMessageTimeout", 1500 );
		
		Config.debug_area_chunkEntrance		= c.getBoolean("debug.area.chunkEntrance", false);
		Config.debug_area_chunkExit			= c.getBoolean("debug.area.chunkExit", false);
		Config.debug_area_chunkChange		= c.getBoolean("debug.area.chunkChange", false);
		
		Config.wandId						= c.getInt("wand", 294 );
		
		Config.loadZonesFromConfig( c );
	}
	
	/**
	 * Good guy brings closure
	 */
	public static void close() {
		Config.c = null;
		
	}

	public static void saveZoneConfig( Zone z ) {
		
		if ( z == null ) return;
		
		String path = "zones." + z.getTag() + ".";
		
		if ( z.getArea().ne() != null ) {		
			List<Integer> ne = new ArrayList<Integer>(Arrays.asList(z.getArea().ne().getBlockX(), z.getArea().ne().getBlockY(), z.getArea().ne().getBlockZ()));
			c.set(path + "ne", ne );
			c.set(path + "world", z.getArea().ne().getWorld().getName());
		}
		
		if ( z.getArea().sw() != null ) {
			List<Integer> sw = new ArrayList<Integer>(Arrays.asList(z.getArea().sw().getBlockX(), z.getArea().sw().getBlockY(), z.getArea().sw().getBlockZ()));
			c.set(path + "sw", sw );
			c.set(path + "world", z.getArea().sw().getWorld().getName());
		}
		
		//Requirements
		c.set( path + "requirements.noPlayers", z.isRequireNoPlayers() );
		
		
		//Pre
		String ppath = path + "pre.";
		c.set(ppath + "removeEntities", z.isKillEntities() );
		c.set(ppath + "keepEntities", z.getKillEntityExceptions() );
		
		c.set( ppath + "removeSpawnPoints", z.isRemoveSpawnPoints() );
		
		if ( z.getResetSpawnLocation() != null ) {
			c.set( ppath + "setSpawn.world", z.getResetSpawnLocation().getWorld().getName() );
			c.set( ppath + "setSpawn.location", new ArrayList<Integer>(Arrays.asList( z.getResetSpawnLocation().getBlockX(), z.getResetSpawnLocation().getBlockY(), z.getResetSpawnLocation().getBlockZ() ) ) );
		}
		
		if ( z.getTransportPlayers() != null ) {
			c.set( ppath + "movePlayers.world", z.getTransportPlayers().getWorld().getName() );
			c.set( ppath + "movePlayers.location", new ArrayList<Integer>(Arrays.asList( z.getTransportPlayers().getBlockX(), z.getTransportPlayers().getBlockY(), z.getTransportPlayers().getBlockZ() ) ) );		
		}
		
		
		//Post
		if ( !z.getSpawns().isEmpty() ) {
			
			if ( z.getSpawns().containsKey( ZRSPAWNTYPE.INVENTORY ) ) {
				
				//set players inventory
				
				c.set( path + "post.setInventory", formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.INVENTORY) ) );
			}	
			
			if ( z.getSpawns().containsKey( ZRSPAWNTYPE.ITEM ) || z.getSpawns().containsKey( ZRSPAWNTYPE.CONTAINER) ) {
			
				//set items
				List< Map<String, Object>> items = new ArrayList< Map<String, Object>>();
				if ( z.getSpawns().containsKey( ZRSPAWNTYPE.ITEM ) ) {
					items.addAll( formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.ITEM ) ) );
				}
				
				if ( z.getSpawns().containsKey( ZRSPAWNTYPE.CONTAINER ) ) {
					items.addAll( formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.CONTAINER ) ) );
				}
				
				if ( !items.isEmpty() ) {
					c.set( path + "post.spawnItems", items );
				}
			}
					
			
			if ( z.getSpawns().containsKey( ZRSPAWNTYPE.MOB ) ) {
				
				c.set( "post.spawnMobs", formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.MOB ) ) );
				
				
			}
				
			
		}
		
		//Triggers
		
		ppath = path + "trigger.";
		
		if ( z.isOnPlayerJoin() ) c.set(ppath + "onPlayerJoin", z.isOnPlayerJoin() );
		if ( z.getOnPlayerJoinList().size() > 0 ) c.set(ppath + "whenPlayersJoin", z.getOnPlayerJoinList() ); 
		if ( z.isOnPlayerQuit() ) c.set(ppath + "onPlayerQuit", z.isOnPlayerQuit() );
		if ( z.getOnPlayerQuitList().size() > 0 ) c.set(ppath + "whenPlayersQuit", z.getOnPlayerQuitList() );
		if ( z.getOnMinutes() > 0 ) c.set( ppath + "onTimer", z.getOnMinutesFormat() );
		if ( z.getOnInteractLocation() != null ) {
			c.set(ppath + "onInteract.item", z.getOnInteractMaterialId() );
			c.set( ppath + "onInteract.world", z.getOnInteractLocation().getWorld().getName() );
			c.set( ppath + "onInteract.location", new ArrayList<Integer>(Arrays.asList( z.getOnInteractLocation().getBlockX(), z.getOnInteractLocation().getBlockY(), z.getOnInteractLocation().getBlockZ() ) ) );
		}
		
		
		
		Zones.loadInteractKeys();
		Bukkit.getPluginManager().getPlugin("ZoneReset").saveConfig();
		
		
		
	}

	public static List< Map<String, Object>> formatSpawnForConfig( List<SpawnInterface> spawn ) {
		
		List< Map<String, Object>> result = new ArrayList< Map<String, Object>>();
		
		if ( !spawn.isEmpty() ) {
			
			for( SpawnInterface s : spawn ) {
				result.add( s.getList() );
			}
			
		}
		
		return result;
		
	}
	
	
}
