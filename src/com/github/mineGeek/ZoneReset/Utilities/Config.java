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
	
	/**
	 * Timeout of messages to prevent flooding player
	 * TODO: I don't think this is used here anymore.
	 */
	public static int spamPlayerMessageTimeout = 1500;
	
	/**
	 * Debugging parameters
	 */
	public static boolean debug_area_chunkEntrance = false;
	public static boolean debug_area_chunkExit = false;
	public static boolean debug_area_chunkChange = false;
	
	public static String folderPlayers;
	public static String folderPlugin;
	public static String folderSnapshots;
	
	
	/**
	 * Main load from config
	 */
	public static void loadConfig() {
		
		Config.spamPlayerMessageTimeout		= c.getInt( "playerMessageTimeout", 1500 );		
		Config.debug_area_chunkEntrance		= c.getBoolean("debug.area.chunkEntrance", false);
		Config.debug_area_chunkExit			= c.getBoolean("debug.area.chunkExit", false);
		Config.debug_area_chunkChange		= c.getBoolean("debug.area.chunkChange", false);

		Config.loadZonesFromConfig( c );
	}	
	
	/**
	 * Load all zones from the config
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
		
		/**
		 * Pre-Load any interaction triggers for zones.
		 */
		Zones.loadInteractKeys();
		
		/**
		 * Let 'em know we are loaded and good to go.
		 */
		Bukkit.getServer().getLogger().info("ZoneRest loaded " + Zones.count() + " zones total.");
		
	}
	
	/**
	 * Save a zone to the config file. Note that this will overwrite any values
	 * that have been changed in the config.yml since it was last loaded.
	 * 
	 * @param Zone z
	 */
	public static void saveZoneConfig( Zone z ) {
		
		/**
		 * Who you trying to fool?
		 */
		if ( z == null ) return;
		
		/**
		 * Root path
		 */
		String path = "zones." + z.getTag() + ".";
		
		/**
		 * Set world
		 */
		if ( z.getWorldName() != null ) c.set( path + "world", z.getWorldName() );
		
		/**
		 * If NE is set, record it
		 */
		if ( z.getArea().ne() != null ) {		
			List<Integer> ne = new ArrayList<Integer>(Arrays.asList(z.getArea().ne().getBlockX(), z.getArea().ne().getBlockY(), z.getArea().ne().getBlockZ()));
			c.set(path + "ne", ne );
			if ( z.getWorldName() == null ) {
				c.set(path + "world", z.getArea().ne().getWorld().getName());
			}
		}
		
		/**
		 * If SW is set, record it
		 */
		if ( z.getArea().sw() != null ) {
			List<Integer> sw = new ArrayList<Integer>(Arrays.asList(z.getArea().sw().getBlockX(), z.getArea().sw().getBlockY(), z.getArea().sw().getBlockZ()));
			c.set(path + "sw", sw );
			if ( z.getWorldName() == null ) {
				c.set(path + "world", z.getArea().sw().getWorld().getName());
			}
		}
		
		/**
		 * Any Requirements for reset?
		 */
		c.set( path + "requirements.noPlayers", z.isRequireNoPlayers() );
		
		
		/**
		 * Pre-Reset processing.
		 */
		String ppath = path + "pre.";
		c.set(ppath + "removeEntities", z.isPreNoMobs() );
		c.set(ppath + "keepEntities", z.getPreNoMobsExceptionList() );
		
		c.set( ppath + "removeSpawnPoints", z.isPreNoSpawns() );
		
		if ( z.getPreSpawnLocation() != null ) {
			c.set( ppath + "setSpawn.world", z.getPreSpawnLocation().getWorld().getName() );
			c.set( ppath + "setSpawn.location", new ArrayList<Integer>(Arrays.asList( z.getPreSpawnLocation().getBlockX(), z.getPreSpawnLocation().getBlockY(), z.getPreSpawnLocation().getBlockZ() ) ) );
		}
		
		if ( z.getPreNewLocation() != null ) {
			c.set( ppath + "movePlayers.world", z.getPreNewLocation().getWorld().getName() );
			c.set( ppath + "movePlayers.location", new ArrayList<Integer>(Arrays.asList( z.getPreNewLocation().getBlockX(), z.getPreNewLocation().getBlockY(), z.getPreNewLocation().getBlockZ() ) ) );		
		}
		
		
		/**
		 * Post-Reset processing
		 */
		ppath = path + "post.";
		if ( !z.getSpawns().isEmpty() ) {
			
			if ( z.getSpawns().containsKey( ZRSPAWNTYPE.INVENTORY ) ) {
				
				//set players inventory
				
				c.set( ppath + "setInventory", formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.INVENTORY) ) );
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
					c.set( ppath + "spawnItems", items );
				}
			}
					
			
			if ( z.getSpawns().containsKey( ZRSPAWNTYPE.MOB ) ) {
				
				c.set( ppath + "spawnMobs", formatSpawnForConfig( z.getSpawns().get( ZRSPAWNTYPE.MOB ) ) );
				
				
			}
				
			
		}
		
		
		/**
		 * Triggers that may cause resetting
		 */
		ppath = path + "trigger.";
		
		if ( z.isTrigOnPlayerJoin() ) c.set(ppath + "onPlayerJoin", z.isTrigOnPlayerJoin() );
		if ( z.getTrigOnPlayerJoinList().size() > 0 ) c.set(ppath + "whenPlayersJoin", z.getTrigOnPlayerJoinList() ); 
		if ( z.isTrigOnPlayerQuit() ) c.set(ppath + "onPlayerQuit", z.isTrigOnPlayerQuit() );
		if ( z.getTrigOnPlayerQuitList().size() > 0 ) c.set(ppath + "whenPlayersQuit", z.getTrigOnPlayerQuitList() );
		if ( z.getTrigTimer() > 0 ) c.set( ppath + "onTimer", z.getTrigTimerText() );
		if ( z.getOnInteractLocation() != null ) {
			c.set(ppath + "onInteract.item", z.getOnInteractMaterialId() );
			c.set( ppath + "onInteract.world", z.getOnInteractLocation().getWorld().getName() );
			c.set( ppath + "onInteract.location", new ArrayList<Integer>(Arrays.asList( z.getOnInteractLocation().getBlockX(), z.getOnInteractLocation().getBlockY(), z.getOnInteractLocation().getBlockZ() ) ) );
		}
		
		
		/**
		 * Save it.
		 */
		Bukkit.getPluginManager().getPlugin("ZoneReset").saveConfig();
		
		
		
	}

	/**
	 * Transform a zone spawn type to a basic list for storage in config.
	 * @param spawn
	 * @return
	 */
	public static List< Map<String, Object>> formatSpawnForConfig( List<SpawnInterface> spawn ) {
		
		List< Map<String, Object>> result = new ArrayList< Map<String, Object>>();
		
		if ( !spawn.isEmpty() ) {
			
			for( SpawnInterface s : spawn ) {
				result.add( s.getList() );
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * Good guy brings closure
	 */
	public static void close() {
		Config.c = null;
		
	}	
	
}
