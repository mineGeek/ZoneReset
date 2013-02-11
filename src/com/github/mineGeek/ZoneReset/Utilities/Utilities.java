package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;



public class Utilities {


	
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
	
	
	public static void clearPlayerMetaData( Player p ) {
		
		Plugin plug = Bukkit.getPluginManager().getPlugin("ZoneReset");
		String[] keys = {"ZREditMode", "ZR_1", "ZR_2", "zr", "zra", "zrinteract" };
		
		for ( String x : keys ) {
			p.removeMetadata( x , plug );
		}
		
		
		
	}
	
	public static void resetZoneSpawnPoints( Zone zone ) {
		resetZoneSpawnPoints( zone.getArea(), zone.getResetSpawnLocation() );
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
		movePlayersInZoneTo( zone.getArea(), zone.getTransportPlayers() );
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
		clearLocationOfEntities( zone.getArea(), zone.getKillEntityExceptions() );
	}
	
	public static List<SpawnInterface> getEntitiesInZone( Zone zone ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    Area area = zone.getArea();
	    List<SpawnInterface> l = new ArrayList<SpawnInterface>();
	    
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
							
							
							
						} else if ( e instanceof Creature ) {
							//mob
							
						} else if ( e instanceof Monster ) {
							

							
						} else if ( e instanceof Item ) {
							
							ItemStack i = ((Item)e).getItemStack();
							spawn = new ItemSpawn(i, e.getLocation() );
							
							
							
						}
						
						if ( spawn != null ) {
							
							
							l.add( spawn );
								//l.add( new EntityLocation( e.getType().name(), e.getType().getTypeId(), e.getWorld().getName(), e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ()) ); 
						}
					}
				}
				
				for ( BlockState bs : chunk.getTileEntities() ) {
					
					if ( area.intersectsWith( bs.getLocation() ) ) {
						
						 ItemSpawn item = new ItemSpawn( bs.getBlock().getType(), bs.getBlock().getData(), (short)0, 1, bs.getLocation().getWorld().getName(), bs.getLocation().getBlockX(), bs.getLocation().getBlockY(), bs.getLocation().getBlockZ());

						 Inventory ih = ((InventoryHolder) bs).getInventory();
						 
						 for( ItemStack it : ih ) {
							 
							 if ( it != null ) {
					
								 
								 ItemSpawn invItem = new ItemSpawn( it.getType(), it.getData().getData(), it.getDurability(), it.getAmount() );
								 item.contains.add( invItem );
							 }
							 
							 
						 }
						 
						 l.add( item );
						 //Material m, byte data, short durability, int qty, String worldName, int x, int y, int z
						
						
					}
					
				}
				

			}
			
		}
		
		return l;
		
		
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

				for( Entity e : chunk.getEntities()) {
					if ( !( e instanceof Player ) ) {
						if ( !exclusions.contains( e.getType() ) ) { 
							if ( area.intersectsWith( e.getLocation() ) ) {
								e.remove();
							}
						}
					}
				}

			}
			
		}
		
		
	}
	
	
	public static void spawnEntitiesInZone( Zone zone ) {
		spawnEntities( zone.getWorldName(), zone.getSpawnEntities() );
	}
	
	public static void spawnEntities( String worldName, List< SpawnInterface> list ) {
		
		if ( list.size() > 0  ) {
			
			
			for ( SpawnInterface e : list ) {
				e.spawn();
				
			}
			
		}
		
		
	}
	
	

	
	
	
	
}
