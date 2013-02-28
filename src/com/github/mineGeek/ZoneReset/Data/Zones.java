package com.github.mineGeek.ZoneReset.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;
import com.github.mineGeek.ZoneReset.Actions.ActionEmptyPlayerInventory;
import com.github.mineGeek.ZoneReset.Actions.ActionFillPlayerInventory;
import com.github.mineGeek.ZoneReset.Actions.ActionMovePlayers;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveEntities;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.ActionSetSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.IAction;
import com.github.mineGeek.ZoneReset.Actions.ResetAction;
import com.github.mineGeek.ZoneReset.Messaging.Message;

import com.github.mineGeek.ZoneReset.Tasks.MessageTask;
import com.github.mineGeek.ZoneReset.Tasks.ResetTask;
import com.github.mineGeek.ZoneReset.Triggers.Trigger;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnEnter;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnExit;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnInteract;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnJoin;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnQuit;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Tracking;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class Zones {

	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	private static Map<String, List<String>> interactKeys = new HashMap<String, List<String>>();
	private static Map<String, List<String>> chunkKeys = new HashMap<String, List<String>>();
	

	
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
		zone.tasks.setResume( o.getAsInteger("resume", null ) );
		
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
		o.set("resume", zone.tasks.getResume() );
		o.save();
		
	}
	
	
	public static Map<String, Zone> getZones() {
		return zones;
	}
	
	
	public static void loadChunkKeys() {
		
		chunkKeys.clear();
		
		for ( Zone z : zones.values() ) {
			
			Area a = z.getArea();
			
			if ( a.ne() != null && a.sw() != null ) {
				
				List<Chunk> chunks = Utilities.getChunksInArea( a );
				
				if ( !chunks.isEmpty() ) {
					
					for ( Chunk chunk : chunks ) {
						String i = chunk.getWorld().getName() + "|" + chunk.getX() + "|" + chunk.getZ();
						if ( chunkKeys.containsKey(i) ) {
							if ( !chunkKeys.get(i).contains(z.getTag() ) ) {
								chunkKeys.get(i).add( z.getTag() );
							}
						} else {
							chunkKeys.put(i, new ArrayList<String>( Arrays.asList( z.getTag() ) ) );
						}
					}
					
				}
				
			}			
		}
	}
	
	
	public static void loadInteractKeys() {
		
		interactKeys.clear();
		
		for ( Zone z : zones.values() ) {
			if ( z.triggers.onInteract != null ) {
				Location l = z.triggers.onInteract.location;
				String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + z.triggers.onInteract.materialId;
				if ( interactKeys.containsKey(i) ) {
					interactKeys.get(i).add(i);
				} else {
					interactKeys.put(i, new ArrayList<String>( Arrays.asList( z.getTag() ) ) );
				}
			}
		}
		
		loadChunkKeys();
		
	}
	
	public static Map<String, List<String>> getChunkKeys() {
		return chunkKeys;
	}
	
	public static List<String> getChunkKeys( String key ) {
	
		if ( chunkKeys.containsKey( key ) ) return chunkKeys.get(key);
		return null;
	}
	
	public static void triggerPlayerMove( Player p ) {
		
		Tracking.updatePlayerChunkMap( p );		
		if ( !Config.trackMovement ) return;		
		Tracking.playerMove( p );
		
	}
	
	public static void triggerPlayerJoin( Player p ) {
		
		for ( Zone z : zones.values() ) {
			z.triggers.onJoin( p );			
		}
		
	}
	
	public static void triggerPlayerQuit( Player p ) {
		
		for ( Zone z : zones.values() ) {
			z.triggers.onQuit( p );		
		}
		
	}
	
	public static void triggerInteract( Material m, Location l ) {

		
		String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + m.getId();
		
		if ( interactKeys.containsKey(i) ) {
		
			for ( String x : interactKeys.get(i) ) {
				Zone z = zones.get( x );
				if ( z != null ) z.triggers.onInteract(m, l);
			}
			
		}
		
	}	
	
	public static void addZone( String tag, ConfigurationSection c ) {
		
		Zone r = new Zone();
		Zones.zones.put( tag , r );
		
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
		
		
			
		if ( c.isSet( "messages.onReset" ) ) {
			
			ZRScope messageType = ZRScope.valueOf( c.getString("messages.onreset.scope", "region").toUpperCase() );		
			Message post = new Message( messageType, worldName);
			post.zoneTag = r.getTag();
			r.resetMessage = post;
			
		}
			
		
		List<Map<?, ?>> messages = c.getMapList("messages.timed");
		
		for ( Map<?, ?> message : messages ) {
			
			MessageTask messageTimed = new MessageTask();
			
			messageTimed.tag = r.getTag();
			messageTimed.scope = ZRScope.valueOf( message.containsKey("scope") ? message.get("scope").toString().toUpperCase() : "REGION" );
			messageTimed.secStart = (int) (Utilities.getSecondsFromText( message.get("start").toString() ) * 1);
			
			if ( message.containsKey("interval") ) messageTimed.secInterval = (int) (Utilities.getSecondsFromText( message.get("interval").toString() ) * 1);
			if ( message.containsKey("end") ) messageTimed.secEnd = (int) (Utilities.getSecondsFromText( message.get("end").toString() ) * 1);
			
			messageTimed.setMessage( message.get("messages.timer.message").toString() );
			r.tasks.add( messageTimed );
			
		}
			
		
		

		/**
		 * Remove all entities before reset?
		 */
		IAction pre;
		
		pre = new ActionRemoveEntities( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.preActions.add( pre );
		
		pre = new ActionRemoveSpawnPoints( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.preActions.add( pre );		
		
		pre = new ActionSetSpawnPoints( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.preActions.add( pre );			
		
		pre = new ActionMovePlayers( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.preActions.add( pre );			

		
		pre = new ResetAction( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.resetActions.add( pre );			
		
		pre = new ActionEmptyPlayerInventory( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.postActions.add( pre );				

		pre = new ActionFillPlayerInventory( r.tag );
		pre.loadFromConfig("", c);
		if ( pre.isEnabled() ) r.postActions.add( pre );		
		
		boolean trackMovements = false;

		if ( c.isSet("trigger.onjoin.reset" ) ) {
			
			TriggerOnJoin onJoin = new TriggerOnJoin();
			onJoin.tag = r.getTag();
			String mode = c.getString("trigger.onjoin.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				onJoin.restartTimer = true;
			} else if ( mode.equals("zone") ) {
				onJoin.resetSeconds = 0;
			} else if ( mode.length() > 0 ) {
				onJoin.resetSeconds = (int)(Utilities.getSecondsFromText( mode )*1);
			}
			
			r.triggers.onJoin = onJoin;
			
		}
		
		if ( c.isSet("trigger.onquit.reset" ) ) {
			
			TriggerOnQuit onQuit = new TriggerOnQuit();
			onQuit.tag = r.getTag();
			String mode = c.getString("trigger.onquit.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				onQuit.restartTimer = true;
			} else if ( mode.equals("zone") ) {
				onQuit.resetSeconds = 0;
			} else if ( mode.length() > 0 ) {
				onQuit.resetSeconds = (int)(Utilities.getSecondsFromText( mode )*1);
			}
			
			r.triggers.onQuit = onQuit;
			
		}
		
		if ( c.isSet("trigger.oninteract.xyz") ) {
			
			TriggerOnInteract interact = new TriggerOnInteract();
			interact.tag = r.getTag();
			List<Integer> l = c.getIntegerList("trigger.oninteract.xyz");
			String w = c.getString("trigger.oninteract.world", worldName);			
			Location loc = new Location( Bukkit.getWorld(w), l.get(0), l.get(1), l.get(2) );
			interact.location = loc;
			interact.materialId = c.getInt("trigger.oninteract.item");
			r.triggers.onInteract = interact;
		}
		
	
		
		if ( c.isSet("trigger.onenter.reset" ) ) {
			
			TriggerOnEnter enter = new TriggerOnEnter();
			enter.tag = r.getTag();
			String mode = c.getString("trigger.onenter.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				enter.restartTimer = true;
			} else if ( mode.equals("zone") ) {
				enter.resetSeconds = 0;
			} else if ( mode.length() > 0 ) {
				enter.resetSeconds = (int)(Utilities.getSecondsFromText( mode )*1);
			}
			
			r.triggers.onEnter = enter;
			trackMovements = true;
			
		}
		
		if ( c.isSet("trigger.onexit.reset" ) ) {
			
			TriggerOnExit exit = new TriggerOnExit();
			exit.tag = r.getTag();
			String mode = c.getString("trigger.onexit.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				exit.restartTimer = true;
			} else if ( mode.equals("zone") ) {
				exit.resetSeconds = 0;
			} else if ( mode.length() > 0 ) {
				exit.resetSeconds = (int)(Utilities.getSecondsFromText( mode )*1);
			}
			
			r.triggers.onExit = exit;
			trackMovements = true;
			
		}
		
		if ( c.isSet("trigger.ontime.reset" ) ) {
			
			Trigger time = new Trigger();
			time.tag = r.getTag();
			String mode = c.getString("trigger.ontime.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				time.restartTimer = true;
			} else if ( mode.equals("zone") ) {
				time.resetSeconds = 0;
			} else if ( mode.length() > 0 ) {
				time.resetSeconds = (int)(Utilities.getSecondsFromText( mode )*1);
			}
			
			r.triggers.onTimed = time;

			ResetTask t = new ResetTask();
			t.secInterval = time.resetSeconds;
			r.tasks.add( t );
			
			
		}
		
		if ( trackMovements ) Config.trackMovement = true;
		
		
		r.start();
		
	}
	
	public static void saveZone( Zone zone ) {
		
        FileOutputStream fileOut;
        ZArea z = zone.getArea().getBlocks();
        
        if ( z.getBlocks().size() == 0 ) {
        	z = null;
        }
        
		try {

			fileOut = new FileOutputStream( Config.folderSnapshots + File.separator + zone.tag + ".ser");
	        ObjectOutputStream out =  new ObjectOutputStream(fileOut);
	        out.writeObject( z );
	        out.close();
	        fileOut.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
