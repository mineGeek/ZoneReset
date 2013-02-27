package com.github.mineGeek.ZoneReset.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Area;

public class Track {

	public String entranceMessage = null;
	public String exitMessage = null;
	public Area area = null;
	
	public Map<String, Boolean> in = new HashMap<String, Boolean>();
	
	public Track( Location ne, Location sw ) {
		
		area = new Area( ne, sw );
		Tracking.add( this );
		
	}
	
	public void run( Player p ) {
		
		if ( area == null ) return;
		
		boolean intersect = area.intersectsWith( p.getLocation() );
		
		if ( intersect && !in.containsKey( p.getName() ) ) {
			p.sendMessage("in");
			in.put( p.getName() , true );
			onEnter( p );
		} else if ( !intersect && in.containsKey( p.getName() ) ) {
			p.sendMessage("out");
			in.remove( p.getName() );
			onExit( p );
		}
		
	}
	
	public void onEnter( Player p ) {
		
		if ( entranceMessage != null ) p.sendMessage( entranceMessage );
	}
	
	public void onExit( Player p ) {

		if ( exitMessage != null ) p.sendMessage( exitMessage );
	}
	
	
	public void close() {
		this.area.close();
		this.in.clear();
		this.area = null;
		this.in = null;
		
	}
}
