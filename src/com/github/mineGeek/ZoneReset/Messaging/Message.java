package com.github.mineGeek.ZoneReset.Messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class Message implements Runnable {
	
	public enum ZRMessageType {LIST, REGION, WORLD, SERVER};
	
	public String 	zoneTag;
	public String 	timeText;
	public Long 	showTime;
	public int 		task;
	public String 	text;
	public ZRMessageType type;
	public List<String> players = new ArrayList<String>();

	
	public Message( ZRMessageType type, String text ) {
		this.text = text;
		this.type = type;
	}
	
	public void send() {
		
		Bukkit.getServer().broadcastMessage( this.text );
			
		
	}
	
	public void send( List<String> players ) {
		
		if ( !players.isEmpty() ) {
			for( String x : players ) {
				if ( Bukkit.getPlayer( x ).isOnline() ) {
					Bukkit.getPlayer( x ).sendMessage( this.text );
				}
			}
		}
		
	}
	
	public void send( Zone zone ) {
		
		this.send( Utilities.getPlayersNearZone( zone ) );
		
	}
	
	public void send( String worldName ) {
		
		for ( Player p : Bukkit.getWorld( worldName ).getPlayers() ) {
			
			p.sendMessage( this.text );
			
		}
		
	}

	public Long getTime() {
		if ( this.timeText != null ) return Utilities.getSecondsFromText( this.timeText );
		return null;
	}
	
	@Override
	public void run() {

		if ( this.type.equals( ZRMessageType.LIST ) ) {
			this.send( this.players );
		} else if ( this.type.equals( ZRMessageType.REGION ) ) {
			
			Zone z = Zones.getZone( this.zoneTag );
			if ( z.getArea().ne() != null && z.getArea().sw() != null ) {
				this.send( z );
			}
			
		} else if ( this.type.equals( ZRMessageType.SERVER ) ) {
			this.send();
		} else if ( this.type.equals( ZRMessageType.WORLD ) ) {
			this.send( Zones.getZone( this.zoneTag ).getWorldName() );
		}
		
	}
	
	
	public Map<String, Object> getList() {
		
		Map<String, Object> r = new HashMap<String, Object>();
		r.put("scope", this.type.toString().toLowerCase() );
		if ( this.timeText != null ) r.put("time", this.timeText );
		if ( this.text != null ) r.put("text", this.text ) ;
		
		return r;
		
	}	
	
	
	
	
	
}
