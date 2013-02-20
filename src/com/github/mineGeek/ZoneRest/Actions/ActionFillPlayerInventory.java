package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionFillPlayerInventory extends Action {

	public ZRScope scope = ZRScope.REGION;
	public List<ItemStack> items = new ArrayList<ItemStack>();
	
	public void run() {
		
		List<Player> p = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> players = Utilities.getPlayersNearZone( Zones.getZone( this.tag ) );
			for ( String x : players ) {
				if (Bukkit.getPlayer(x).isOnline() ) p.add( Bukkit.getPlayer(x) );
			}
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			List<Player> players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			for ( Player ps : players ) {
				if ( ps.isOnline() ) p.add(ps);
			}
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] players = Bukkit.getServer().getOnlinePlayers();
			for ( Player player : players ) {
				p.add(player);
			}
		}
		
		if ( !p.isEmpty() && !this.items.isEmpty() ) {
			
			for ( Player player : p ) {
				player.getInventory().addItem((ItemStack[]) items.toArray());
			}
			
		}
		
		
	}	
	
}
