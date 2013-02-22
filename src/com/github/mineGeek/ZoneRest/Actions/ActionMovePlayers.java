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

public class ActionMovePlayers extends Action {

	public ActionMovePlayers(String tag) {
		super(tag);
	}

	public ZRScope scope = ZRScope.REGION;
	public Location location = null;

	public void run() {
		
		if ( location == null || !enabled ) return;
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> players = Utilities.getPlayersNearZone( Zones.getZone( this.tag ) );
			for ( String x : players ) {
				if ( Bukkit.getPlayer(x).isOnline() ) Bukkit.getPlayer(x).teleport(location);
			}
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			List<Player> players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			for ( Player p : players ) {
				if ( p.isOnline() ) p.teleport(location);
			}
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			for ( Player player : players ) {
				player.teleport(location);
			}
		}
		
	}
	
	public void close() {
		this.location =  null;
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !enabled ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".moveplayers.scope", scope.toString().toLowerCase() );
		
		if ( location != null ) {
			c.set( root + ".moveplayers.world", location.getWorld().getName() );
			c.set( root + ".moveplayers.xyz", new ArrayList<Integer>( Arrays.asList( location.getBlockX(), location.getBlockY(), location.getBlockZ() ) ) );			
		}
			
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {

		scope = ZRScope.valueOf( c.getString( root + ".moveplayers.scope", "region").toUpperCase() );
		String worldName = Zones.getZone( this.tag ).getWorldName();
		
		worldName = c.getString( root + ".moveplayers.world", worldName );
		
		if ( c.isSet( root + ".moveplayers.xyz" ) ) {
			List<Integer> xyz = c.getIntegerList( root + ".moveplayers.xyz" );
			location = new Location( Bukkit.getWorld( worldName ), xyz.get(0), xyz.get(1), xyz.get(2) );
		}		
		enabled = ( !scope.equals( ZRScope.REGION ) || location != null );
		
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}	
	
	
}
