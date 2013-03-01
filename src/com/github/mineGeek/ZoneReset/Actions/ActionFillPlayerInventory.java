package com.github.mineGeek.ZoneReset.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.ZItem;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionFillPlayerInventory extends Action {

	public ActionFillPlayerInventory(String tag) {
		super(tag);
	}
	public ZRScope scope = ZRScope.REGION;
	public List<ItemStack> items = new ArrayList<ItemStack>();
	
	public void run() {
		
		List<Player> p = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> players = Zones.getZone( this.tag ).getPlayers();
			for ( String x : players ) {
				if (Bukkit.getPlayer(x) != null ) p.add( Bukkit.getPlayer(x) );
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
				if ( player.isOnline() ) player.getInventory().addItem((ItemStack[]) items.toArray());
			}
			
		}
		
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {

		if ( !isEnabled() ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".inventory.add.scope", scope.toString().toLowerCase() );
		
		List<Map<String, Object>> a = new ArrayList<Map<String, Object>>();
		
		for ( ItemStack i : items ) {
			
			a.add( new ZItem(i).getMap() );				
			
		}
		
		if ( !a.isEmpty() ) c.set( root + ".inventory.add.items", a );
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".inventory.add.scope", "region").toUpperCase() );
		
		if ( c.isSet( root + ".inventory.add.items") ) {
			List<Map<?, ?>> a = c.getMapList( root + ".inventory.add.items");
			items.clear();
			
			if ( !a.isEmpty() ) {
				for ( Map<?,?> map : a ) {
					items.add( new ZItem( (Map<String, Object>)map ).getItemStack() );
				}
			}
			
		}
		
		enabled = isEnabled();
		
		
	}
	@Override
	public boolean isEnabled() {
		return ( ( !scope.equals( ZRScope.REGION ) ) || items.isEmpty() );
	}	
	
}
