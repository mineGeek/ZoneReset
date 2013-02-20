package com.github.mineGeek.ZoneRest.Actions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionMovePlayers extends Action {

	public ZRScope scope = ZRScope.REGION;
	public String worldName;
	public int toX;
	public int toY;
	public int toZ;
	
	public void run() {
		
		Location l = new Location( Bukkit.getWorld( this.worldName ), toX, toY, toZ );
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> players = Utilities.getPlayersNearZone( Zones.getZone( this.tag ) );
			for ( String x : players ) {
				if ( Bukkit.getPlayer(x).isOnline() ) Bukkit.getPlayer(x).teleport(l);
			}
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			List<Player> players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			for ( Player p : players ) {
				if ( p.isOnline() ) p.teleport(l);
			}
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			for ( Player player : players ) {
				player.teleport(l);
			}
		}
		
	}
	
	public void close() {
		
	}
	
	
}
