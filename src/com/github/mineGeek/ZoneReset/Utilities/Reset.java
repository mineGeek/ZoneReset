package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class Reset {

	public String tag;
	public String worldName;
	public Area area;
	public String key;
	public boolean requireNoPlayers = false;
	public boolean removeSpawnPoints = false;
	public Location resetSpawnPoints;
	public Location transportPlayers;
	public boolean killEntities = false;
	public List<EntityType> killEntityExceptions = new ArrayList<EntityType>(); 
	public List<EntityLocation> spawnEntities = new ArrayList<EntityLocation>();
	public List<String> lastErrors = new ArrayList<String>();
	public String snapShotName = null;
	public Map< String, String > schedule = new HashMap<String, String>();
	public boolean onPlayerJoin = false;
	public List<String> onPlayerJoinList = new ArrayList<String>();
	public boolean onPlayerQuit = false;
	public List<String> onPlayerQuitList = new ArrayList<String>();
	public int onMinutes = 0;
	public Location onInteractLocation = null;
	
	public boolean restore() {
		
		lastErrors.clear();
		
		if ( this.removeSpawnPoints ) {

			this.resetSpawnPoints( this.area, this.resetSpawnPoints );
			
		}
		
		
		if ( this.transportPlayers != null ) {
			
			this.movePlayersToLocation( this.area, this.transportPlayers );
			
		}
		
		if ( requireNoPlayers && this.areaContainsPlayers( this.area ) ) {
			lastErrors.add("Area contains players. Cannot proceed with restore.");
			return false;
		}
		
		if ( killEntities ) this.clearLocationOfEntities( this.area, this.killEntityExceptions );
		this.spawnEntities();
		
		
		return true;
		
	}
	
	
	public boolean isInteracting( Location l ) {
		
		return this.onInteractLocation.getBlock().equals( l.getBlock());
		
	}
	
	public boolean isPlayerJoin( String playerName ) {
		
		if ( this.onPlayerJoin ) {
			
			if ( this.onPlayerJoinList.isEmpty() ) return true;			
			return this.onPlayerJoinList.contains( playerName );
			
		}
		
		return false;
		
	}
	
	public boolean isPlayerQuit( String playerName ) {
		
		if ( this.onPlayerQuit ) {
			
			if ( this.onPlayerQuitList.isEmpty() ) return true;			
			return this.onPlayerQuitList.contains( playerName );
			
		}
		
		return false;		
		
	}
	
	public boolean 
	
	
	public void setKillEntityExceptions( List<String> list ) {
		
		this.killEntityExceptions.clear();
		
		if ( list != null && list.size() > 0 ) {
			
			for ( String x : list ) {
				this.killEntityExceptions.add( EntityType.fromName( x ) );
			}
			
		}
		
	}
	
	public void spawnEntities() {
		
		if ( this.spawnEntities.size() > 0  ) {
			
			Server server = Bukkit.getServer();
			
			for ( EntityLocation e : this.spawnEntities ) {
				
				server.getWorld( this.worldName ).spawnEntity( e.location, e.entityType );
				
			}
			
		}
		
		
	}
	
	public void clearLocationOfEntities( Area area, List<EntityType> exclusions ) {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
	    
		int fromX = ( (int)area.ne.getX()/16) -1 ;
		int toX = ( (int)area.sw.getX()/16) + 1;
		
		int fromZ = ( (int)area.ne.getZ()/16) - 1;
		int toZ = ( (int)area.sw.getZ()/16) + 1;
		
		for( int x = fromX; x <= toX; x++ ) {

			chunks.add( area.ne.getWorld().getChunkAt( x, fromZ ) );
			
			for ( int z = fromZ; z <= toZ; z++) {
				chunks.add( area.ne.getWorld().getChunkAt(x, z) );
			}
			
		}		
		
		if ( chunks.size() > 0 ) {

			for ( Chunk chunk : chunks ) {

				for( Entity e : chunk.getEntities()) {

					if ( !exclusions.contains( e.getType() ) ) { 
						if ( area.intersectsWith( e.getLocation() ) ) {
							e.remove();
						}
					}
				}

			}
			
		}
		
		
	}
	
	
	private void resetSpawnPoints( Area area, Location location ) {
		
		Server server = Bukkit.getServer();		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getBedSpawnLocation() ) )  {
				p.setBedSpawnLocation( location, true );
			}
			
		}		
		
		
	}

	
	private void movePlayersToLocation( Area area, Location location ) {
		
		Server server = Bukkit.getServer();
		
		Player[] ps = server.getOnlinePlayers();
		
		if ( ps.length == 0 ) return;
		
		for ( Player p : ps ) {
			
			if ( area.intersectsWith( p.getLocation() ) )  {
				p.teleport( location );
			}
			
		}
		
	}
	
	private boolean areaContainsPlayers( Area area ) {
		
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
	
	
}
