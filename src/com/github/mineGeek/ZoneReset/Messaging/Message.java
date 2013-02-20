package com.github.mineGeek.ZoneReset.Messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class Message implements Runnable {
	
	public String 	zoneTag;
	public String 	timeText;
	public Long 	showTime;
	public Long 	start;
	public Long		end;
	public Long		endStamp;
	public Long		interval;
	public int 		task;
	public String 	text;
	public ZRScope	scope;
	public List<String> players = new ArrayList<String>();

	
	public Message( ZRScope type, String text ) {
		this.text = text;
		this.scope = type;
	}
	
	public Message() {}

	public void send() {
		
		Bukkit.getServer().broadcastMessage( this.getMessage( this.text ) );
			
		
	}
	
	public void send( List<String> players ) {
		
		if ( !players.isEmpty() ) {
			for( String x : players ) {
				if ( Bukkit.getPlayer( x ).isOnline() ) {
					Bukkit.getPlayer( x ).sendMessage( this.getMessage( this.text ) );
				}
			}
		}
		
	}
	
	public void send( Zone zone ) {
		
		List<String> names = Utilities.getPlayersNearZone( zone );
		
		if ( names != null && !names.isEmpty() ) {
			this.send( names );
		}
		
	}
	
	public void send( String worldName ) {
		
		for ( Player p : Bukkit.getWorld( worldName ).getPlayers() ) {
			
			p.sendMessage( this.getMessage( this.text ) );
			
		}
		
	}

	public String getMessage( String value ) {
		
		if ( endStamp != null && endStamp != 0 ) {
			Long diff = ( endStamp - (System.currentTimeMillis()/1000));
			Object[] args = {Utilities.getTimeStampAsString( diff )};
			return String.format( value, args );
		}
		
		return text;
		
	}
	
	public Long getTime() {
		if ( this.timeText != null ) return Utilities.getSecondsFromText( this.timeText );
		return null;
	}
	
	@Override
	public void run() {

		if ( this.scope.equals( ZRScope.LIST ) ) {
			this.send( this.players );
		} else if ( this.scope.equals( ZRScope.REGION ) ) {
			
			Zone z = Zones.getZone( this.zoneTag );
			if ( z.getArea().ne() != null && z.getArea().sw() != null ) {
				this.send( z );
			}
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			this.send();
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			this.send( Zones.getZone( this.zoneTag ).getWorldName() );
		}
		
	}
	
	
	public Map<String, Object> getList() {
		
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("scope", this.scope.toString().toLowerCase() );
		if ( this.timeText != null ) r.put("time", this.timeText );
		if ( this.text != null ) r.put("text", this.text ) ;
		
		return r;
		
	}	
	
	
	
	
}
