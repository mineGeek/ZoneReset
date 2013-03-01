package com.github.mineGeek.ZoneReset.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionSetSpawnPoints extends Action {

	public ActionSetSpawnPoints(String tag) {
		super(tag);
	}

	public ZRScope scope = ZRScope.REGION;
	public boolean online = true;
	public Location location = null;
	
	@Override
	public void run() {
		
		List<Player> players = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> ps = Zones.getZone( this.tag ).getPlayers();
			for ( String x : ps ) {
				if ( Bukkit.getPlayer(x) != null ) players.add( Bukkit.getPlayer(x) );
			}
			
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] ps = (Player[]) ( online ? Bukkit.getServer().getOnlinePlayers() : Bukkit.getServer().getOfflinePlayers() );
			players.addAll( Arrays.asList( ps ) );
		}
		
		if ( !players.isEmpty() ) {
			
			for ( Player p : players ) {
				
				if ( p.isOnline() ) {
					p.setBedSpawnLocation( location, true );
				} else {
					//OfflinePlayer op = Bukkit.getOfflinePlayer( p.getName() );
					
				}
				
			}
			
			
		}
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !isEnabled() ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".setspawnpoint.scope", scope.toString().toLowerCase() );
		
		if ( location != null ) {
			c.set( root + ".setspawnpoint.world", location.getWorld().getName() );
			c.set( root + ".setspawnpoint.xyz", new ArrayList<Integer>( Arrays.asList( location.getBlockX(), location.getBlockY(), location.getBlockZ() ) ) );			
		}
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".setspawnpoint.scope", "region").toUpperCase() );
		String worldName = Zones.getZone( this.tag ).getWorldName();
		
		worldName = c.getString( root + ".setspawnpoint.world", worldName );
		
		if ( c.isSet( root + ".setspawnpoint.xyz" ) ) {
			List<Integer> xyz = c.getIntegerList( root + ".setspawnpoint.xyz" );
			location = new Location( Bukkit.getWorld( worldName ), xyz.get(0), xyz.get(1), xyz.get(2) );
		}		
		enabled = isEnabled();
		
	}
	
	@Override
	public boolean isEnabled() {
		return ( !scope.equals( ZRScope.REGION ) || location != null );
	}	

}
