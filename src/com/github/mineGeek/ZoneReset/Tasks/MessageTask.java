package com.github.mineGeek.ZoneReset.Tasks;



import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;
import com.github.mineGeek.ZoneReset.Data.Zones;



public class MessageTask extends Task implements ITask {

	public ZRScope scope = ZRScope.REGION;
	public String rawMessage;
	private boolean hasToken = false;

	public void setMessage( String message ) {
		
		this.hasToken = message.contains("%");
		this.rawMessage = message;
		
	}
	
	public String getMessage() {
		
		if ( !this.hasToken ) return rawMessage;
		
		Object[] args = { this.getTextToEnd(), this.getTextSinceStart(), this.getTextLastStart() };		
		return String.format( this.rawMessage, args );
	}
	
	@Override
	public void run() {
		
		String message = this.getMessage();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> players = Zones.getZone( this.tag ).getPlayers();
			for ( String x : players ) {
				if ( Bukkit.getPlayer(x).isOnline() ) Bukkit.getPlayer(x).sendMessage( message );
			}
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			List<Player> players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			for ( Player p : players ) {
				if ( p.isOnline() ) p.sendMessage( message );
			}
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			for ( Player player : players ) {
				player.sendMessage( message );
			}
		}
		
	}

}
