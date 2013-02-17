package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Messaging.Message;
import com.github.mineGeek.ZoneReset.Spawners.BookSpawn;
import com.github.mineGeek.ZoneReset.Spawners.ItemSpawn;
import com.github.mineGeek.ZoneReset.Spawners.MobSpawner;
import com.github.mineGeek.ZoneReset.Spawners.SpawnContainer;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;
import com.github.mineGeek.ZoneReset.Spawners.SignSpawn;
import com.github.mineGeek.ZoneReset.Utilities.Zone.ZRMethod;



public class Utilities {


	public static Map<String, List<String> > playersByChunkSig = new HashMap<String, List<String>>();
	public static Map<String, String> playersLastChunkSig = new HashMap<String, String>();
	
	public static ZoneReset plugin;
	
	public static String getChunkSig( Location l ) {
		return l.getWorld().getName() + "|" + l.getChunk().getX() + "|" + l.getChunk().getZ();
	}
	
	public static String getChunkSig( Chunk c ) {
		return c.getWorld().getName() + "|" + c.getX() + "|" + c.getZ();
	}	
	
	public static void checkAllPlayerChunks() {
		
		playersByChunkSig.clear();
		playersLastChunkSig.clear();
		
		for ( Player p : Bukkit.getOnlinePlayers() ) {
			
			p.removeMetadata("ZRChunk", plugin );
			checkPlayerChunk(p);
			
		}
		
	}
	
	public static void checkPlayerChunk( Player p ) {
		checkPlayerChunk(p, p.getLocation() );
	}
	
	public static void checkPlayerChunk( Player p, Location l ) {
		
    	String chunkSig = getChunkSig( l.getChunk() );
    	
    	if ( p.hasMetadata("ZRChunk") ) {
    		
    		if ( !p.getMetadata("ZRChunk").get(0).asString().equals( chunkSig ) ) {
    			Utilities.addPlayerByChunk( chunkSig, p.getName() );
    			//p.sendMessage("new chunk " + chunkSig );
    		}
    		
    	} else {
    		Utilities.addPlayerByChunk(chunkSig, p.getName() );
    	}
    	
		p.setMetadata("ZRChunk",  new FixedMetadataValue( Bukkit.getPluginManager().getPlugin("ZoneReset"), chunkSig ) );		
		
	}
	
	public static void addPlayerByChunk( String chunkSig, String playerName ) {
		
		if ( playersLastChunkSig.containsKey( playerName ) ) {
			if ( !chunkSig.equals( playersLastChunkSig.get(playerName) ) ) {
				playersByChunkSig.get( playersLastChunkSig.get(playerName ) ).remove( playerName );
			}
		}
		
		playersLastChunkSig.put( playerName, chunkSig );
		
		if ( playersByChunkSig.containsKey( chunkSig ) ) {
			playersByChunkSig.get( chunkSig ).add( playerName );
		} else {
			playersByChunkSig.put( chunkSig, new ArrayList<String>( Arrays.asList( playerName ) ) );
			
		}
		
	}
	
	public static void removePlayerFromChunks( Player p ) {
		
		if ( playersLastChunkSig.containsKey( p.getName() ) ) {
			playersByChunkSig.get( playersLastChunkSig.get( p.getName() ) ).remove( p.getName() );			
			playersLastChunkSig.remove( p.getName() );
		}
		
		p.removeMetadata("ZRChunk", plugin );
		
		
		
	}
	
	public static List<String> getPlayersNearZone( Zone zone ) {
		
		List<String> list = new ArrayList<String>();
		
		Area a = zone.getArea();
		if ( a == null || a.ne() == null || a.sw() == null ) {
			return list;
		}
		
		List<String> chunks = getChunkSigsFromArea( a );
		
		if ( !chunks.isEmpty() && !playersByChunkSig.isEmpty() ) {
			
			for ( String chunk : chunks ) {
				
				if ( playersByChunkSig.containsKey( chunk ) ) {
					list.addAll(playersByChunkSig.get( chunk ) );
				}
				
			}
			
			
		}
		
		return list;
		
	}
	
	public static List<String> getChunkSigsFromArea( Area a ) {
		
		List<Chunk> chunks = getChunksFromArea( a );
		List<String> list = new ArrayList<String>();
		
		if ( !chunks.isEmpty() ) {
			for ( Chunk c : chunks ) {
				list.add( Utilities.getChunkSig(c) );
			}
		}
		
		return list;
		
	}
	
	public static List<Chunk> getChunksFromArea( Area a ) {
		
		List<Chunk> list = new ArrayList<Chunk>();
		
		Location ne = a.ne();
		Location sw = a.sw();
		
		int fromX = ( (int)ne.getX()/16) -1 ;
		int toX = ( (int)sw.getX()/16) + 1;
		
		int fromZ = ( (int)ne.getZ()/16) - 1;
		int toZ = ( (int)sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {
			
			for ( int z = fromZ; z <= toZ; z++) {
				list.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		return list;
		
	}
	
	
	public static void queue() {
		
		plugin.clearAllMessages();
		plugin.clearAllResets();
		
		for ( Zone z : Zones.getZones().values() ) {
			queueResets( z );
			queueMessages( z );
		}
		
	}
	
	public static void queue( Zone z ) {
		
		plugin.clearMessages( z.getTag() );
		plugin.clearResets( z.getTag() );
		queueResets( z );
		queueMessages( z );
		
	}
	
	public static void queueMessages() {
		
		plugin.clearAllMessages();
		
		for ( Zone z : Zones.getZones().values() ) {
			queueMessages( z );
		}
		
	}
	
	public static void queueMessages( Zone z ) {
		
		List<Message> messages = z.getTimedMessages();
		
		if ( !messages.isEmpty() ) {
			
			for ( Message m : messages ) {
				
				Long next = z.getNextTimedReset();
				
				if ( next != null && next != 0 ) {
					
					if ( next < System.currentTimeMillis() ) {
						//overdue. Run now.
						next = 0L;
					} else {
						next = next - System.currentTimeMillis();
					}
					
				}				
				
				Long interval = z.getTrigTimer();
				Long time = m.getTime();
				
				
				if ( time != null ) {
					
					if ( time < 0 ) {
						next = interval + time;
					} else {
						next = next + time;
					}
					
	    			BukkitTask task = plugin.getServer().getScheduler().runTaskTimer( plugin, m , next * 20 , interval * 20 );
	    			
	    			if ( plugin.messages.containsKey( z.getTag() ) ) {
	    				plugin.messages.get( z.getTag() ).add( task );
	    			} else {	    			
	    				plugin.messages.put( z.getTag(), new ArrayList<BukkitTask>( Arrays.asList( task ) ) );
	    			}
					
				}
				
			}
			
		}		
		
	}
	
	public static void queueResets() {
		
		plugin.clearAllResets();
		for ( Zone z : Zones.getZones().values() ) {
			queueResets( z );
		}
		
	}
	
	public static void queueResets( Zone z ) {
		
		if ( z.getTrigTimer() > 0 ) {
			
			Long next = z.getNextTimedReset();
			
			if ( next != null && next != 0 ) {
				
				if ( next < System.currentTimeMillis() ) {
					//overdue. Run now.
					next = 0L;
				} else {
					next = next - System.currentTimeMillis();
				}
				
			}
			
			final Long nextRun = next * 20 + 1;
			final Long repeatRun = z.getTrigTimer() * 20;
			final String tag = z.getTag();
			
			BukkitTask task = plugin.getServer().getScheduler().runTaskTimer( plugin, new Runnable() {
	    	    @Override  
	    	    public void run() {
	    	    	try {
	    	    		Zones.getZone(tag).reset( ZRMethod.TIMED );
	    	    		Zones.getZone(tag).setNextTimedRest( Zones.getZone(tag).getTrigTimer() + System.currentTimeMillis() );
	    	    	} catch (Exception e ) {}
	    	    }
	    	}, nextRun , repeatRun );
			
			if ( plugin.tasks.containsKey( z.getTag() ) ) {
				plugin.tasks.get( z.getTag() ).add( task );
			} else {	    			
				plugin.tasks.put( z.getTag(), new ArrayList<BukkitTask>( Arrays.asList( task ) ) );
			}
			
		}		
		
	}
	
	public static boolean zoneHasPlayers( Zone zone ) {
		return zoneHasPlayers( zone.getArea() );
	}
	
	public static boolean zoneHasPlayers( Area area ) {
		
		Server server = Bukkit.getServer();
		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return false;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getLocation() ) )  {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	
	public static Long getSecondsFromText( String value ) {
		
		
		if ( value == null ) return 0L;
		
		try {
			Matcher match = Pattern.compile("(?:(-?))?(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?").matcher( value );
			Long secs = (long) 0;	
			boolean negative = false;
			
			if ( match.find() ) {

				if ( match.group(1).equals( "-")  ) negative = true;
				if ( match.group(2) != null ) secs = 60*60*24 * Long.parseLong( match.group(2) );
				if ( match.group(3) != null ) secs += 60*60* Long.parseLong( match.group(3) );
				if ( match.group(4) != null ) secs += 60* Long.parseLong( match.group(4) );
				if ( match.group(5) != null ) secs += Long.parseLong( match.group(5) );
				
			}
		
			return ( negative ? secs * -1 : secs );
			
		} catch ( Exception e ) {}
		
		return null;
		
	}	
	
	public static void clearPlayerMetaData( Player p ) {
		
		Plugin plug = Bukkit.getPluginManager().getPlugin("ZoneReset");
		String[] keys = {"ZREditMode", "ZR_1", "ZR_2", "zr", "zra", "zrinteract" };
		
		for ( String x : keys ) {
			p.removeMetadata( x , plug );
		}
		
		
		
	}
	
	public static void resetZoneSpawnPoints( Zone zone ) {
		resetZoneSpawnPoints( zone.getArea(), zone.getPreSpawnLocation() );
	}
	
	public static void resetZoneSpawnPoints( Area area, Location location ) {
		
		Server server = Bukkit.getServer();		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getBedSpawnLocation() ) )  {
				p.setBedSpawnLocation( location, true );
			}
			
		}		
		
		
	}
	
	public static void movePlayersInZone( Zone zone ) {
		movePlayersInZoneTo( zone.getArea(), zone.getPreNewLocation() );
	}
	
	public static void movePlayersInZoneTo( Area area, Location destination ) {
		
		Server server = Bukkit.getServer();
		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getLocation() ) )  {
				p.teleport( destination );
			}
			
		}
		
	}
	
	
	public static void clearZoneOfEntities( Zone zone ) {
		clearLocationOfEntities( zone.getArea(), zone.getPreNoMobsExceptionList() );
	}
	
	public static Map<ZRSPAWNTYPE, List<SpawnInterface>> getEntitiesInZone( Zone zone ) {
		List<ZRSPAWNTYPE> l = new ArrayList<ZRSPAWNTYPE>();
		l.add( ZRSPAWNTYPE.CONTAINER );
		l.add( ZRSPAWNTYPE.ITEM );
		l.add( ZRSPAWNTYPE.MOB );
		l.add( ZRSPAWNTYPE.SIGN );
		return getEntitiesInZone( zone, l );
	}
	
	public static Map<ZRSPAWNTYPE, List<SpawnInterface>> getEntitiesInZone( Zone zone, List<ZRSPAWNTYPE> types  ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    Area area = zone.getArea();
	    List<SpawnInterface> l = new ArrayList<SpawnInterface>();
	    
	    List<SpawnInterface> mobs = new ArrayList<SpawnInterface>();
	    List<SpawnInterface> items = new ArrayList<SpawnInterface>();
	    List<SpawnInterface> containers = new ArrayList<SpawnInterface>();
	    List<SpawnInterface> signs 	= new ArrayList<SpawnInterface>();
	    
		Location ne = area.ne();
		Location sw = area.sw();
		
		int fromX = Math.min( ne.getChunk().getX(), sw.getChunk().getX() );
		int toX = Math.max( ne.getChunk().getX(), sw.getChunk().getX() );
		
		int fromZ = Math.min( ne.getChunk().getZ(), sw.getChunk().getZ() );
		int toZ = Math.max( ne.getChunk().getZ(), sw.getChunk().getZ() );
				
		
		
		for( int x = fromX; x <= toX; x++ ) {
			
			for ( int z = fromZ; z <= toZ; z++) {
				chunks.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		if ( chunks.size() > 0 ) {

			for ( Chunk chunk : chunks ) {
				
				for( Entity e : chunk.getEntities()) {
 
					if ( area.intersectsWith( e.getLocation() ) ) {
						
						SpawnInterface spawn = null;
						
						if ( e instanceof Player ) {
							
							//Do nothing!
							
						} else if ( e instanceof Creature && types.contains( ZRSPAWNTYPE.MOB ) ) {
							
							spawn = new MobSpawner( e );
							mobs.add( spawn );
							
						} else if ( e instanceof Monster  && types.contains( ZRSPAWNTYPE.MOB ) ) {
							
							spawn = new MobSpawner( e );
							mobs.add( spawn );
							
						} else if ( e instanceof Item  && types.contains( ZRSPAWNTYPE.ITEM ) ) {
							
							ItemStack i = ((Item)e).getItemStack();
							
							if ( i.getType() == Material.WRITTEN_BOOK || i.getType() == Material.BOOK || i.getType() == Material.BOOK_AND_QUILL ) {
								//spawn = new BookSpawn( i );
								spawn = new ItemSpawn( i ); 
							} else {
								spawn = new ItemSpawn( i );
							}
							items.add( spawn );
							
						}
						
						if ( spawn != null ) {
							
							
							l.add( spawn );
								//l.add( new EntityLocation( e.getType().name(), e.getType().getTypeId(), e.getWorld().getName(), e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ()) ); 
						}
					}
				}
				
				for ( BlockState bs : chunk.getTileEntities() ) {
					
					if ( area.intersectsWith( bs.getLocation() )  && types.contains( ZRSPAWNTYPE.CONTAINER ) ) {
						
						
						if ( bs instanceof Sign && types.contains( ZRSPAWNTYPE.SIGN )) {
							
							SignSpawn s = new SignSpawn( (Sign)bs );
							signs.add( s );
							l.add( s );
						} else {
						
							 SpawnContainer s = new SpawnContainer( bs.getBlock() );
							
							 Inventory ih = null;
							 if ( bs instanceof Chest ) {
								 ih = ((Chest) bs).getBlockInventory();
							 } else {
								 ih = ((InventoryHolder) bs).getInventory();
							 }
							 s.clearItems();
							 for( ItemStack it : ih ) {
								 
								 if ( it != null ) {
									 if ( it.getType() == Material.WRITTEN_BOOK || it.getType() == Material.BOOK || it.getType() == Material.BOOK_AND_QUILL ) {
										//s.addItem( new BookSpawn( it ) );
										 s.addItem( new ItemSpawn( it ) );
									 } else {
										 s.addItem( new ItemSpawn( it ) );
									 }
									
								 }
								 
								 
							 }						
												
							containers.add( s );
							l.add( s );
						}
						

						/*
						 ItemSpawnOld item = new ItemSpawnOld( bs.getBlock().getType(), bs.getBlock().getData(), (short)0, 1, bs.getLocation().getWorld().getName(), bs.getLocation().getBlockX(), bs.getLocation().getBlockY(), bs.getLocation().getBlockZ());

						 Inventory ih = null;
								 
						 if ( bs instanceof Chest ) {
							 ih = ((Chest) bs).getBlockInventory();
						 } else {
							 ih = ((InventoryHolder) bs).getInventory();
						 }
						 
						 for( ItemStack it : ih ) {
							 
							 if ( it != null ) {
					
								 
								 ItemSpawnOld invItem = new ItemSpawnOld( it.getType(), it.getData().getData(), it.getDurability(), it.getAmount() );
								 item.contains.add( invItem );
							 }
							 
							 
						 }
						 
						 l.add( item );
						 //Material m, byte data, short durability, int qty, String worldName, int x, int y, int z
						*/
						
					}
					
				}
				

			}
			
		}
		
		Map<ZRSPAWNTYPE, List<SpawnInterface>> result = new HashMap<ZRSPAWNTYPE, List<SpawnInterface>>();
		
		if ( types.contains( ZRSPAWNTYPE.MOB ) ) {
			result.put( ZRSPAWNTYPE.MOB, mobs );
		}
		
		if ( types.contains( ZRSPAWNTYPE.ITEM ) ) {
			result.put( ZRSPAWNTYPE.ITEM, items );
		}
		
		if ( types.contains( ZRSPAWNTYPE.CONTAINER ) ) {
			result.put( ZRSPAWNTYPE.CONTAINER, containers );
		}
		
		if ( types.contains( ZRSPAWNTYPE.SIGN ) ) {
			result.put( ZRSPAWNTYPE.SIGN, signs );
		}
		
		return result;
		
		
	}
	

	
	public static Material getMaterialFromEntity( Entity entity ) {
		
		Class<?>[] interfaces = entity.getClass().getInterfaces();
		if ( interfaces.length == 1 ) {
			String s = interfaces[0].getSimpleName();
			Material mat = Material.matchMaterial(s);
			if ( mat != null ) return mat;
		}
		
		return null;
		
	}	
	
	public static void clearLocationOfEntities( Area area, List<EntityType> exclusions ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    
		Location ne = area.ne();
		Location sw = area.sw();
		
		int fromX = ( (int)ne.getX()/16) -1 ;
		int toX = ( (int)sw.getX()/16) + 1;
		
		int fromZ = ( (int)ne.getZ()/16) - 1;
		int toZ = ( (int)sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {

			chunks.add( ne.getWorld().getChunkAt( x, fromZ ) );
			
			for ( int z = fromZ; z <= toZ; z++) {
				chunks.add( ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		if ( chunks.size() > 0 ) {

			for ( Chunk chunk : chunks ) {

				for ( BlockState bs : chunk.getTileEntities() ) {
					
					if ( area.intersectsWith( bs.getLocation()) ) {
					
						if ( bs instanceof InventoryHolder ) {
							
							((InventoryHolder)bs).getInventory().clear();
							
						}
						bs.getBlock().setType( Material.AIR);

						
					}					
					
					
				}
				
				for( Entity e : chunk.getEntities()) {
					if ( !( e instanceof Player ) ) {
						if ( !exclusions.contains( e.getType() ) ) { 
							if ( area.intersectsWith( e.getLocation() ) ) {
								
								if ( e instanceof DoubleChest ) {
									

								} else {
									e.remove();
								}
							}
						}
					}
				}

			}
			
		}
		
		
	}
	
	
	public static void spawnEntities( Map<ZRSPAWNTYPE, List< SpawnInterface>> spawns ) {
		
		if ( !spawns.isEmpty()  ) {
			
			if ( spawns.containsKey( ZRSPAWNTYPE.MOB ) ) {
				spawnList( spawns.get( ZRSPAWNTYPE.MOB) );
			}
			
			if ( spawns.containsKey( ZRSPAWNTYPE.ITEM ) ) {
				spawnList( spawns.get( ZRSPAWNTYPE.ITEM) );
			}
			
			if ( spawns.containsKey( ZRSPAWNTYPE.CONTAINER ) ) {
				spawnList( spawns.get( ZRSPAWNTYPE.CONTAINER) );
			}
			
			if ( spawns.containsKey( ZRSPAWNTYPE.SIGN ) ) {
				spawnList( spawns.get( ZRSPAWNTYPE.SIGN ) );
			}
			
		}
		
		
	}
	
	public static void spawnList( List< SpawnInterface> spawn ) {
		
		if ( !spawn.isEmpty() ) {
			for ( SpawnInterface s : spawn ) {
				s.spawn();
			}
		}
		
	}
	
	

	
	
	
	
}
