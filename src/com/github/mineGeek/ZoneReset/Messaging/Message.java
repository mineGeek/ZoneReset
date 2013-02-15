package com.github.mineGeek.ZoneReset.Messaging;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Utilities.Zone;

public class Message {
	
	public enum ZRMessageType {PLAYER, LIST, REGION, WORLD, SERVER};
	public String text;
	public ZRMessageType type;
	public List<String> players = new ArrayList<String>();
	
	public Message( ZRMessageType type, String text ) {
		this.text = text;
		this.type = type;
	}
	
	public void send() {
		
		for( Player p : Bukkit.getServer().getOnlinePlayers() ) {
			p.sendMessage( this.text );
		}
			
		
	}
	
	public void send( List<String> players ) {
		
		if ( !players.isEmpty() ) {
			for( String x : this.players ) {
				if ( Bukkit.getPlayer( x ).isOnline() ) {
					Bukkit.getPlayer( x ).sendMessage( this.text );
				}
			}
		}
		
	}
	
	public void send( Zone zone ) {
		
		Area area = zone.getArea();
		
		if ( area.ne() != null && area.sw() != null ) {
			
			for( Player p : Bukkit.getOnlinePlayers() ){
				
				if ( area.intersectsWith( p.getLocation() ) ) {
					p.sendMessage( this.text );
				}
				
			}
			
		}
		
	}
	
	public void send( String worldName ) {
		
		for ( Player p : Bukkit.getWorld( worldName ).getPlayers() ) {
			
			p.sendMessage( this.text );
			
		}
		
	}
	
	
	
	
}
