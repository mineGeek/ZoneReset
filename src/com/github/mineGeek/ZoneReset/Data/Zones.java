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
import com.github.mineGeek.ZoneReset.Actions.ActionPVP;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveEntities;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.ActionSetSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.IAction;
import com.github.mineGeek.ZoneReset.Actions.ResetAction;
import com.github.mineGeek.ZoneReset.Messaging.Message;

import com.github.mineGeek.ZoneReset.Tasks.MessageTask;
import com.github.mineGeek.ZoneReset.Tasks.ResetTask;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnEnter;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnExit;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnInteract;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnJoin;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnQuit;
import com.github.mineGeek.ZoneReset.Triggers.TriggerOnTime;
import com.github.mineGeek.ZoneReset.Triggers.Triggers.ZRTriggerMode;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Tracking;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class Zones {

	private static Map<String, Zone> zones = new HashMap<String, Zone>();
	private static Map<String, List<String>> interactKeys = new HashMap<String, List<String>>();
	private static Map<String, List<String>> chunkKeys = new HashMap<String, List<String>>();
	
	private static Map<Integer, List<String>> chunkToZoneMap = new HashMap<Integer, List<String>>();

	
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
			
			if ( !z.triggers.getOnInteract().enabled ) continue;
			
			Location l = z.triggers.getOnInteract().location;
			int id = z.triggers.getOnInteract().materialId;
			
			if ( l != null && id > 0 ) {
				
				
				if ( l != null ) {
					String i = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ() + "|" + z.triggers.getOnInteract().materialId;
					if ( interactKeys.containsKey(i) ) {
						interactKeys.get(i).add(i);
					} else {
						interactKeys.put(i, new ArrayList<String>( Arrays.asList( z.getTag() ) ) );
					}
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
	
	public static Zone getZoneByPlayer( Player p ) {
		
		if ( chunkToZoneMap.containsKey( p.getLocation().getChunk().hashCode() ) ) {
			return zones.get( chunkToZoneMap.get( p.getLocation().getChunk().hashCode() ) );
		}
		
		return null;
		
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
		
		
			
		if ( c.isSet( "messages.onreset" ) ) {
			
			ZRScope messageType = ZRScope.valueOf( c.getString("messages.onreset.scope", "region").toUpperCase() );	
			Message post = new Message( messageType, worldName);
			post.zoneTag = r.getTag();
			post.text = c.getString("messages.onreset.message", "");
			r.resetMessage = post;
			
		}
			
		
		List<Map<?, ?>> messages = c.getMapList("messages.timed");
		
		for ( Map<?, ?> message : messages ) {
			
			MessageTask messageTimed = new MessageTask();
			
			messageTimed.tag = r.getTag();
			messageTimed.scope = ZRScope.valueOf( message.containsKey("scope") ? message.get("scope").toString().toUpperCase() : "REGION" );
			messageTimed.secStart = Utilities.getSecondsFromText( message.get("start").toString() ) * 1;
			
			if ( message.containsKey("interval") ) messageTimed.secInterval = Utilities.getSecondsFromText( message.get("interval").toString() ) * 1;
			if ( message.containsKey("end") ) messageTimed.secEnd = Utilities.getSecondsFromText( message.get("end").toString() ) * 1;
			
			messageTimed.setMessage( message.get("message").toString() );
			r.tasks.add( messageTimed );
			
		}
			
		boolean trackMovements = false;
		if ( c.contains( "pvp") ) {
			
			ActionPVP pvp = new ActionPVP( r.tag );
			pvp.loadFromConfig("", c);
			if ( pvp.isEnabled() ) {
				r.postActions.add( pvp );
				trackMovements = true;
			}
			
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
		
		

		if ( c.isSet("trigger.onjoin.reset" ) ) {
			
			TriggerOnJoin onJoin = new TriggerOnJoin( tag );
			onJoin.tag = r.getTag();
			String mode = c.getString("trigger.onjoin.reset").toLowerCase();
			
			onJoin.message = c.getString( "trigger.onjoin.message");
			
			if ( mode.equals("timer") ) {
				onJoin.method = ZRTriggerMode.TIMER;
			} else if ( mode.equals("zone") ) {
				onJoin.method = ZRTriggerMode.INSTANT;
			} else if ( mode.length() > 0 ) {
				onJoin.resetSeconds = Utilities.getSecondsFromText( mode )*1;
				onJoin.method = ZRTriggerMode.DELAYED;
			}
			
			r.triggers.onJoin = onJoin;
			
		}
		
		if ( c.isSet("trigger.onquit.reset" ) ) {
			
			TriggerOnQuit onQuit = new TriggerOnQuit( tag );
			onQuit.tag = r.getTag();
			String mode = c.getString("trigger.onquit.reset").toLowerCase();
			
			onQuit.message = c.getString( "trigger.onquit.message");
			
			if ( mode.equals("timer") ) {
				onQuit.method = ZRTriggerMode.TIMER;
			} else if ( mode.equals("zone") ) {
				onQuit.method = ZRTriggerMode.INSTANT;
			} else if ( mode.length() > 0 ) {
				onQuit.resetSeconds = Utilities.getSecondsFromText( mode )*1;
				onQuit.method = ZRTriggerMode.DELAYED;
			}
			
			r.triggers.onQuit = onQuit;
			
		}
		
		if ( c.isSet("trigger.oninteract.xyz") ) {
			
			TriggerOnInteract interact = new TriggerOnInteract( tag );
			interact.tag = r.getTag();
			List<Integer> l = c.getIntegerList("trigger.oninteract.xyz");
			String w = c.getString("trigger.oninteract.world", worldName);			
			Location loc = new Location( Bukkit.getWorld(w), l.get(0), l.get(1), l.get(2) );
			interact.location = loc;
			interact.materialId = c.getInt("trigger.oninteract.item");
			r.triggers.onInteract = interact;
		}
		
	
		
		if ( c.isSet("trigger.onenter" ) ) {
			
			TriggerOnEnter enter = new TriggerOnEnter( tag );
			enter.tag = r.getTag();
			String mode = c.getString("trigger.onenter.reset", "").toLowerCase();
			
			enter.message = c.getString( "trigger.onenter.message");
			
			if ( mode.equals("timer") ) {
				enter.method = ZRTriggerMode.TIMER;
			} else if ( mode.equals("zone") ) {
				enter.method = ZRTriggerMode.INSTANT;
			} else if ( mode.length() > 0 ) {
				enter.resetSeconds = Utilities.getSecondsFromText( mode )*1;
				enter.method = ZRTriggerMode.DELAYED;
			}
			
			r.triggers.onEnter = enter;
			trackMovements = true;
			
		}
		
		if ( c.isSet("trigger.onexit" ) ) {
			
			TriggerOnExit exit = new TriggerOnExit( tag );
			exit.tag = r.getTag();
			String mode = c.getString("trigger.onexit.reset", "").toLowerCase();
			
			exit.message = c.getString( "trigger.onexit.message");
			
			if ( mode.equals("timer") ) {
				exit.method = ZRTriggerMode.TIMER;
			} else if ( mode.equals("zone") ) {
				exit.method = ZRTriggerMode.INSTANT;
			} else if ( mode.length() > 0 ) {
				exit.resetSeconds = Utilities.getSecondsFromText( mode )*1;
				exit.method = ZRTriggerMode.DELAYED;
			}
			
			r.triggers.onExit = exit;
			trackMovements = true;
			
		}
		
		if ( c.isSet("trigger.ontime.reset" ) ) {
			
			TriggerOnTime time = new TriggerOnTime( tag );
			time.tag = r.getTag();
			String mode = c.getString("trigger.ontime.reset").toLowerCase();
			
			if ( mode.equals("timer") ) {
				time.method = ZRTriggerMode.TIMER;
			} else if ( mode.equals("zone") ) {
				time.method = ZRTriggerMode.INSTANT;
			} else if ( mode.length() > 0 ) {
				time.resetSeconds = Utilities.getSecondsFromText( mode )*1;
				time.method = ZRTriggerMode.DELAYED;
			}
			
			r.triggers.onTimed = time;

			ResetTask t = new ResetTask( tag );
			t.secStart = time.resetSeconds;
			r.tasks.add( t );
			
			
		}
		
		if ( trackMovements ) Config.trackMovement = true;
		
		
		Area a = r.getArea();
		if ( a != null && a.ne() != null && a.sw() != null ) {
			
			List<Integer> ids = a.getChunkIDs();
			
			if ( !ids.isEmpty() ) {
				
				for ( Integer i : ids ) {
					if ( chunkToZoneMap.containsKey(i) ) {
						chunkToZoneMap.get(i).remove( r.getTag() );
					} else {
						chunkToZoneMap.put(i, new ArrayList<String>());
					}
					
					chunkToZoneMap.get(i).add( r.getTag() );
				}
				
			}
			
		}
		
		//r.start();
		
	}
	
	public static void startZones() {
		
		for ( Zone z : zones.values() ) {
			z.start();
		}
		
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
