package com.github.mineGeek.RestoreIt.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class RestoreSection {

	public String tag;
	public String worldName;
	public Area area;
	public String key;
	public boolean requireNoPlayers = false;
	public boolean transportPlayers = false;
	public boolean removeSpawnPoints = false;
	public Location resetSpawnPoints;
	public Location transportPlayersTo;
	public boolean killEntities = false;
	public List<EntityType> killEntityExceptions = new ArrayList<EntityType>(); 
	public Map<String, Location> spawnEntities = new HashMap<String, Location>();
	public List<String> lastErrors = new ArrayList<String>();
	
	public boolean restore() {
		
		lastErrors.clear();
		
		if ( this.removeSpawnPoints ) {

			this.resetSpawnPoints( this.area, this.resetSpawnPoints );
			
		}
		
		
		if ( this.transportPlayers ) {
			
			this.movePlayersToLocation( this.area, this.transportPlayersTo );
			
		}
		
		if ( requireNoPlayers && this.areaContainsPlayers( this.area ) ) {
			lastErrors.add("Area contains players. Cannot proceed with restore.");
			return false;
		}
		
		if ( killEntities ) this.clearLocationOfEntities( this.area, this.killEntityExceptions );
		this.spawnEntities();
		
		
		return true;
		
	}
	
	
	public void spawnEntities() {
		
		if ( this.spawnEntities.size() > 0  ) {
			
			Server server = Bukkit.getServer();
			
			for ( String x : this.spawnEntities.keySet() ) {
				
				server.getWorld( this.worldName ).spawnEntity( this.spawnEntities.get( x ), EntityType.fromName(x) );
				
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
