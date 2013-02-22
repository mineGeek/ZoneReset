package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionRemoveSpawnPoints extends Action {

	public ActionRemoveSpawnPoints(String tag) {
		super(tag);
	}

	public ZRScope scope = ZRScope.REGION;
	public boolean online = true;
	public Location location = null;
	
	@Override
	public void run() {
		
		List<Player> players = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> ps = Utilities.getPlayersNearZone( Zones.getZone( this.tag ) );
			for ( String x : ps ) {
				if (Bukkit.getPlayer(x).isOnline() || online ) players.add( Bukkit.getPlayer(x) );
			}
			
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] ps = (Player[]) ( online ? Bukkit.getServer().getOnlinePlayers() : Bukkit.getServer().getOfflinePlayers() );
			players.addAll( Arrays.asList( ps ) );
		}
		
		if ( !players.isEmpty() ) {
			
			for ( Player p : players ) {
				
				p.setBedSpawnLocation( location ); 
				
			}
			
			
		}
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !enabled ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".remove.spawnpoints.scope", scope.toString().toLowerCase() );
		
		if ( location != null ) {
			c.set( root + ".remove.spawnpoints.world", location.getWorld().getName() );
			c.set( root + ".remove.spawnpoints.xyz", new ArrayList<Integer>( Arrays.asList( location.getBlockX(), location.getBlockY(), location.getBlockZ() ) ) );			
		}
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".remove.spawnpoints.scope", "region").toUpperCase() );
		String worldName = Zones.getZone( this.tag ).getWorldName();
		
		worldName = c.getString( root + ".remove.spawnpoints.world", worldName );
		
		if ( c.isSet( root + ".remove.spawnpoints.xyz" ) ) {
			List<Integer> xyz = c.getIntegerList( root + ".remove.spawnpoints.xyz" );
			location = new Location( Bukkit.getWorld( worldName ), xyz.get(0), xyz.get(1), xyz.get(2) );
		}		
		enabled = ( !scope.equals( ZRScope.REGION ) || location != null );
		
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}	

}
