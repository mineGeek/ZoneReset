package com.github.mineGeek.ZoneReset.Tasks;

import java.util.List;

import org.bukkit.Bukkit;

import com.github.mineGeek.ZoneReset.Data.Zone.ZRPVPMode;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Tracking;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class PVPTask extends Task implements ITask {

	public PVPTask( String tag ) { super(tag); }
	public ZRPVPMode mode = ZRPVPMode.DEFAULT;
	@Override
	public void run() {
		
		Zones.getZone( this.tag ).pvpMode = mode;
		List<String> players = Tracking.getPlayersInZone( this.tag );
		
		if ( !players.isEmpty() ) {
			for( String x : players ) { Utilities.pvpAddPlayer( x, mode ); Bukkit.getPlayer(x).sendMessage( "pvp: " + mode.toString() + " -- " + Bukkit.getPlayer(x).getWorld().getPVP()); }
		}
		
	}	
	
}
