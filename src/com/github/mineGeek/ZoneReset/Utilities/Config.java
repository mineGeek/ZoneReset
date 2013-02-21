package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Messaging.Message;
import com.github.mineGeek.ZoneReset.Tasks.ITask;
import com.github.mineGeek.ZoneReset.Tasks.MessageTask;
import com.github.mineGeek.ZoneRest.Actions.ActionEmptyPlayerInventory;
import com.github.mineGeek.ZoneRest.Actions.ActionFillPlayerInventory;
import com.github.mineGeek.ZoneRest.Actions.ActionMovePlayers;
import com.github.mineGeek.ZoneRest.Actions.ActionRemoveEntities;
import com.github.mineGeek.ZoneRest.Actions.ActionSetSpawnPoints;
import com.github.mineGeek.ZoneRest.Actions.IAction;
import com.github.mineGeek.ZoneRest.Actions.ResetAction;



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
	public static String folderZones;
	public static String folderPlugin;
	public static String folderSnapshots;
	
	public static boolean noNMS = false;
	
	public static boolean trackMovement = false;
	
	/**
	 * Main load from config
	 */
	public static void loadConfig() {
		
		Config.spamPlayerMessageTimeout		= c.getInt( "playerMessageTimeout", 1500 );		
		Config.debug_area_chunkEntrance		= c.getBoolean("debug.area.chunkEntrance", false);
		Config.debug_area_chunkExit			= c.getBoolean("debug.area.chunkExit", false);
		Config.debug_area_chunkChange		= c.getBoolean("debug.area.chunkChange", false);
		Config.noNMS						= c.getBoolean("no-nms", false);
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
		//TODO: Reimpliment c.set( path + "requirements.noPlayers", z.isRequireNoPlayers() );
		
		

		saveActionToConfig( "zones." + z.getTag() + ".reset.", z.preActions.reset );
		saveActionToConfig( "zones." + z.getTag() + ".timer.", z.resetActions.resetTasks );
		
		saveActionToConfig( "zones." + z.getTag() + ".inventory.remove.", z.preActions.inventoryEmpty );
		saveActionToConfig( "zones." + z.getTag() + ".inventory.give", z.postActions.inventoryFill );
		
		saveActionToConfig( "zones." + z.getTag() + ".spawnpoints.remove.", z.preActions.removeSpawnPoints );
		saveActionToConfig( "zones." + z.getTag() + ".spawnpoints.set", z.preActions.setSpawnPoints );
		saveActionToConfig( "zones." + z.getTag() + ".moveplayers.", z.preActions.movePlayers );
		
		saveActionToConfig( "zones." + z.getTag() + ".remove.", z.preActions.removeEntities );
		
		
		
		saveActionToConfig( "zones." + z.getTag() + ".containers.fill", z.postActions.containerFill );

		
		
		if ( z.triggers.onJoin != null ) {
			
			String value = "timer";
			if ( z.triggers.onJoin.resetSeconds == 0 ) {
				value = "zones";
			} else if ( z.triggers.onJoin.resetSeconds > 0 ) {
				value = Utilities.getTimeStampAsString( z.triggers.onJoin.resetSeconds );
			}
			c.set( path + "trigger.onjoin.reset", value );
			
		}
		
		if ( z.triggers.onQuit != null ) {
			
			String value = "timer";
			if ( z.triggers.onQuit.resetSeconds == 0 ) {
				value = "zones";
			} else if ( z.triggers.onQuit.resetSeconds > 0 ) {
				value = Utilities.getTimeStampAsString( z.triggers.onQuit.resetSeconds );
			}
			c.set( path + "trigger.onquit.reset", value );
			
		}		
		
		if ( z.triggers.onInteract != null ) {
			
			Location l = z.triggers.onInteract.location;
			List<Integer> xyz = new ArrayList<Integer>();
			xyz.add( l.getBlockX() ); xyz.add( l.getBlockY() ); xyz.add( l.getBlockZ() );
			c.set( path + "trigger.oninteract.xyz", xyz );
			c.set( path + "trigger.oninteract.world", l.getWorld().getName() );
			c.set( path + "trigger.oninteract.item", z.triggers.onInteract.materialId );
			
		}
		
		if ( z.triggers.onEnter != null ) {
			
			String value = "timer";
			if ( z.triggers.onEnter.resetSeconds == 0 ) {
				value = "zones";
			} else if ( z.triggers.onEnter.resetSeconds > 0 ) {
				value = Utilities.getTimeStampAsString( z.triggers.onEnter.resetSeconds );
			}
			c.set( path + "trigger.onenter.reset", value );
			
		}
		
		if ( z.triggers.onExit != null ) {
			
			String value = "timer";
			if ( z.triggers.onExit.resetSeconds == 0 ) {
				value = "zones";
			} else if ( z.triggers.onExit.resetSeconds > 0 ) {
				value = Utilities.getTimeStampAsString( z.triggers.onExit.resetSeconds );
			}
			c.set( path + "trigger.onexit.reset", value );
			
		}
		
		if ( z.triggers.onTimed != null ) {
			
			String value = "timer";
			if ( z.triggers.onTimed.resetSeconds == 0 ) {
				value = "zones";
			} else if ( z.triggers.onTimed.resetSeconds > 0 ) {
				value = Utilities.getTimeStampAsString( z.triggers.onTimed.resetSeconds );
			}
			c.set( path + "trigger.ontime.reset", value );
			
		}		
		
		if ( z.resetMessage != null ) {
			
			c.set( path + "messages.onreset.scope", z.resetMessage.scope.toString().toLowerCase() );
			c.set( path + "messages.onreset.message", z.resetMessage.text );
						
		}
		
		saveMessagesToConfig( path + "messages.timed", z.tasks.tasks );		
		
		/**
		 * Save it.
		 */
		Bukkit.getPluginManager().getPlugin("ZoneReset").saveConfig();
		
		
		
	}

	public static void saveMessagesToConfig( String path, List<ITask> tasks ) {
		
		List<Map<String, Object>> timer = new ArrayList<Map<String, Object>>();
		
		for ( ITask i : tasks ) {
			if ( i instanceof MessageTask ) {
				MessageTask m = (MessageTask)i;
				Map<String, Object> o = new HashMap<String, Object>();
				o.put( "scope",  m.scope.toString().toLowerCase() );
				if ( m.secStart != null ) o.put( "start", Utilities.getTimeStampAsString( m.secStart ) );
				if ( m.secInterval != null ) o.put( "interval", Utilities.getTimeStampAsString( m.secInterval ) );
				if ( m.secEnd != null ) o.put( "end", Utilities.getTimeStampAsString( m.secEnd ) );
				o.put( "message", m.rawMessage );
				timer.add( o );
			}
		}
		
		c.set( path, timer );
		
		
	}
	
	public static void saveActionToConfig( String path, IAction act ) {
		
		
		if ( act instanceof ActionRemoveEntities ) {
			
			ActionRemoveEntities a = (ActionRemoveEntities) act;
			if ( !a.enabled ) return;
			
			if ( !a.scope.equals( ZRScope.REGION ) ) c.set(path + "remove.scope", a.scope.toString().toLowerCase() );
			c.set(path + "remove.animals", a.removeAnimals);
			c.set(path + "remove.drops", a.removeDrops );
			c.set(path + "remove.mobs", a.removeMobs );
			c.set(path + "remove.containers", a.removeTiles );
						
		} else if ( act instanceof ActionSetSpawnPoints ) {
						
			ActionSetSpawnPoints a = (ActionSetSpawnPoints) act;
			if ( !a.enabled ) return;
			if ( a.location == null ) {
				c.set( path + "remove.spawnpoints.scope", a.scope.toString().toLowerCase() );
				
			} else  {
				if ( !a.scope.equals( ZRScope.REGION ) ) c.set( path + "setspawn.scope", a.scope.toString().toLowerCase() );
				List<Integer> xyz = new ArrayList<Integer>();
				xyz.add( a.location.getBlockX() ); xyz.add( a.location.getBlockY() ); xyz.add( a.location.getBlockZ() );
				c.set( path + "setspawn.xyz", xyz );
				c.set( path + "setspawn.world", a.location.getWorld().getName() );	
			}
			
		} else if ( act instanceof ActionMovePlayers ) {
			
			ActionMovePlayers a = (ActionMovePlayers) act;
			if ( !a.enabled ) return;
			if ( !a.scope.equals( ZRScope.REGION ) ) c.set( path + "moveplayers.scope", a.scope.toString().toLowerCase() );			
			List<Integer> xyz = new ArrayList<Integer>();
			xyz.add( a.toX ); xyz.add( a.toY ); xyz.add( a.toZ );
			c.set( path + "moveplayers.xyz", xyz );
			c.set( path + "moveplayers.world", a.worldName );
			
		} else if ( act instanceof ResetAction ) {
			
			ResetAction a = ( ResetAction ) act;
			if ( !a.enabled ) return;
			c.set( path + "blocks" , a.resetBlocks );
			c.set( path + "containers", a.resetContainers );
			c.set( path + "mobs", a.resetMobs );
			
			
		} else if ( act instanceof ActionEmptyPlayerInventory ) {
			
			ActionEmptyPlayerInventory a = ( ActionEmptyPlayerInventory ) act;
			if ( !a.enabled ) return;
			if ( !a.scope.equals( ZRScope.REGION ) ) c.set( path + "removeinventory.scope", a.scope.toString().toLowerCase() );
			c.set( path + "removeinventory.whitelist", a.isWhitelist );
			
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			for ( ItemStack i : a.exceptions ) {
				Map<String, Object> add = new HashMap<String, Object>();
				add.put("item", i.getTypeId() );
				add.put("amount", i.getAmount() );
				add.put("damage", i.getAmount() );
				add.put("meta", i.getItemMeta().serialize() );
				items.add( add );
			}
			
			c.set( path + "removeinventory.exceptions", items );
			
		} else if ( act instanceof ActionFillPlayerInventory ) {
			
			ActionFillPlayerInventory a = ( ActionFillPlayerInventory ) act;
			if ( !a.enabled ) return;
			if ( !a.scope.equals( ZRScope.REGION ) ) c.set( path + "addinventory.scope", a.scope.toString().toLowerCase() );
			
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			for ( ItemStack i : a.items ) {
				Map<String, Object> add = new HashMap<String, Object>();
				add.put("item", i.getTypeId() );
				add.put("amount", i.getAmount() );
				add.put("damage", i.getAmount() );
				add.put("meta", i.getItemMeta().serialize() );
				items.add( add );
			}
			
			c.set( path + "addinventory.items", items );
			
		}
		
		
	}
	
	public static List< Map<String, Object>> formatMessageForConfig( List<Message> message ) {
		
		List< Map<String, Object>> result = new ArrayList< Map<String, Object>>();
		
		if ( message != null && !message.isEmpty() ) {
			
			for( Message m : message ) {
				result.add( m.getList() );
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
