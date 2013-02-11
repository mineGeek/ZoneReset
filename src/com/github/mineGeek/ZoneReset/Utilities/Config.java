package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;


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
	
	@SuppressWarnings("unchecked")
	public static void saveZoneConfig( Zone z ) {
		
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
		
		
		//post
		if ( z.getSpawnEntities() != null ) {
			
			List<SpawnInterface> l = z.getSpawnEntities();
			List<Object> o = new ArrayList<Object>();
			
			
			for( SpawnInterface e : l ) {
				//Bukkit.getLogger().info( e.entityType.name() );
				o.add( e.getList() );
				//o.add( new ArrayList<Object>(Arrays.asList( e.entityType.name(), e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ()) ) );
				
			}
			
			c.set(path + "post.spawnEntities", o );
			
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
	
	
}
