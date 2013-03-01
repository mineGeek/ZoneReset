package com.github.mineGeek.ZoneReset.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.Zones;

public class Track {

	public String entranceMessage = null;
	public String exitMessage = null;
	public Area area = null;
	public String tag;
	
	public Map<String, Boolean> in = new HashMap<String, Boolean>();
	
	public Track( String tag, Location ne, Location sw ) {
		
		this.tag = tag;
		area = new Area( ne, sw );
		Tracking.add( this );
		
	}
	
	public void run( Player p ) {
		
		if ( area == null ) return;
		
		boolean intersect = area.intersectsWith( p.getLocation() );
		
		if ( intersect && !in.containsKey( p.getName() ) ) {
			in.put( p.getName() , true );
			onEnter( p );
		} else if ( !intersect && in.containsKey( p.getName() ) ) {
			in.remove( p.getName() );
			onExit( p );
		}
		
	}
	
	public void onEnter( Player p ) {
		
		if ( entranceMessage != null ) p.sendMessage( entranceMessage );
		if ( tag != null ) Zones.getZone( tag ).triggers.onEnter(p);
	}
	
	public void onExit( Player p ) {

		if ( exitMessage != null ) p.sendMessage( exitMessage );
		if ( tag != null ) Zones.getZone( tag ).triggers.onExit(p);
	}
	
	
	public void close() {
		if ( this.area != null ) this.area.close();
		if ( this.in != null ) this.in.clear();
		this.area = null;
		this.in = null;
		
	}
}
