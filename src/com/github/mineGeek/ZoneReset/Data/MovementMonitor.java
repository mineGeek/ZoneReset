package com.github.mineGeek.ZoneReset.Data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementMonitor {

	public String lastChunkKey = null;
	public String currentChunkKey;
	

	
	public Location lastLocation = null;
	public List<String> inZones = new ArrayList<String>();
	
	public boolean update( Player p ) {
		
		
		List<String> last = null;
		
		currentChunkKey = this.getCurrentChunkSig(p);
	
		if ( !currentChunkKey.equals( lastChunkKey ) ) {
			if ( lastChunkKey != null) last = Zones.getChunkKeys( lastChunkKey );
			p.sendMessage("new chunk: " + currentChunkKey );
			lastChunkKey = new String(currentChunkKey);
		}
		
		List<String> now = Zones.getChunkKeys( currentChunkKey );

		inZones.clear();
		if ( last != null ) {
			
			for ( String tag : last ) {
				inZones.add( tag );
				if ( Zones.getZone( tag ).getArea().intersectsWith( p.getLocation() ) && !Zones.getZone( tag ).getArea().intersectsWith( this.lastLocation ) ) {
					Zones.getZone( tag ).triggers.onEnter.run( p );
				} else if ( !Zones.getZone( tag ).getArea().intersectsWith( p.getLocation() ) && Zones.getZone( tag ).getArea().intersectsWith( this.lastLocation ) ) {
					Zones.getZone( tag ).triggers.onExit.run ( p );
				}
			}			
		}
		
		if ( now != null ) {
			
			for ( String tag : now ) {
				if ( !inZones.contains( tag ) ) {
					inZones.add( tag );
					if ( Zones.getZone( tag ).getArea().intersectsWith( p.getLocation() ) && ( this.lastLocation == null || !Zones.getZone( tag ).getArea().intersectsWith( this.lastLocation ) ) ) {
						Zones.getZone( tag ).triggers.onEnter.run( p );
					} else if ( this.lastLocation != null && ( !Zones.getZone( tag ).getArea().intersectsWith( p.getLocation() ) && Zones.getZone( tag ).getArea().intersectsWith( this.lastLocation ) ) ){
						Zones.getZone( tag ).triggers.onExit.run( p );
					}
				}
			}				
			
		}
		
		this.lastLocation = p.getLocation().clone();
		
		return true;
		
	}

	public String getCurrentChunkSig( Player p ) {
		
		return p.getLocation().getWorld().getName() + "|" + p.getLocation().getChunk().getX() + "|" + p.getLocation().getChunk().getZ();
		
	}
	
	public void close() {
		this.lastLocation = null;
	}
	
	
	
}
