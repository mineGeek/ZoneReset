package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.ZItem;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;


public class ActionEmptyPlayerInventory extends Action {

	public ZRScope scope = ZRScope.REGION;
	public List<ItemStack> allow = new ArrayList<ItemStack>();
	public List<ItemStack> remove = new ArrayList<ItemStack>();
	public boolean isWhitelist = false;
	
	
	public void run() {
		
		if ( !enabled ) return;
		
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
				
				while( remove.iterator().hasNext() ) {
					ItemStack i = remove.iterator().next();
					if ( p.getInventory().contains( i ) ) p.getInventory().remove( i );
				}
				
				for ( ItemStack i : p.getInventory().getContents() ) {
					if ( !allow.contains( i ) ) p.getInventory().remove(i);
				}
				
				
			}
			
			
		}
		
	}


	public void setToConfig( String root, ConfigurationSection c ) {

		if ( !enabled ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".inventory.remove.scope", scope.toString().toLowerCase() );
		
		List<Map<String, Object>> a = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
		
		for ( ItemStack i : allow ) {
			
			a.add( new ZItem(i).getMap() );				
			
		}
		
		for ( ItemStack i : remove ) {
			
			r.add( new ZItem(i).getMap() );				
			
		}		
		
		if ( !a.isEmpty() ) c.set( root + ".inventory.remove.allow", a );
		if ( !r.isEmpty() ) c.set( root + ".inventory.remove.only", r );

	}


	@SuppressWarnings("unchecked")
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".inventory.remove.scope", "region").toUpperCase() );
		
		if ( c.isSet( root + ".inventory.remove.allow") ) {
			List<Map<?, ?>> a = c.getMapList( root + ".inventory.remove.allow");
			allow.clear();
			
			if ( !allow.isEmpty() ) {
				for ( Map<?,?> map : a ) {
					allow.add( new ZItem( (Map<String, Object>)map ).getItemStack() );
				}
			}
			
		}
		
		if ( c.isSet( root + ".inventory.remove.only") ) {
			List<Map<?, ?>> a = c.getMapList( root + ".inventory.remove.only");
			remove.clear();
			
			if ( !remove.isEmpty() ) {
				for ( Map<?,?> map : a ) {
					remove.add( new ZItem( (Map<String, Object>)map ).getItemStack() );
				}
			}
			
		}
		
		enabled = ( !scope.equals( ZRScope.REGION ) || !allow.isEmpty() || !remove.isEmpty() );
		
		
	}
	
}
