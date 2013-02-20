package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionEmptyPlayerInventory extends Action {

	public ZRScope scope = ZRScope.REGION;
	public List<ItemStack> exceptions = new ArrayList<ItemStack>();
	public boolean isWhitelist = false;
	
	public void run() {
		
		List<Player> players = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> ps = Utilities.getPlayersNearZone( Zones.getZone( this.tag ) );
			for ( String x : ps ) {
				if (Bukkit.getPlayer(x).isOnline() ) players.add( Bukkit.getPlayer(x) );
			}
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] ps = Bukkit.getServer().getOnlinePlayers();
			players.addAll( Arrays.asList( ps ) );
		}
		
		if ( !players.isEmpty() ) {
			
			for ( Player p : players ) {
				
				if ( exceptions.isEmpty() ) { //remove all
					p.getInventory().clear();
				} else if ( isWhitelist ) { //if not in list, remove
					for (ItemStack i : p.getInventory().getContents()) {
						if ( !exceptions.contains(i) ) {
							p.getInventory().remove( i );
						}
					}
				} else if ( !isWhitelist ) {
					for (ItemStack i : p.getInventory().getContents()) {
						if ( exceptions.contains(i) ) {
							p.getInventory().remove( i );
						}
					}					
				}
				
			}
			
			
		}
		
	}
	
}
