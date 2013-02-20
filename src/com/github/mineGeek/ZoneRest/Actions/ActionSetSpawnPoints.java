package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionSetSpawnPoints extends Action {

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

}
