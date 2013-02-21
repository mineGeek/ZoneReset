package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.ZItem;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionFillTileEntities extends Action {
	
	public List<ItemStack> items = new ArrayList<ItemStack>();
	public List<BlockState>entities = new ArrayList<BlockState>();
	public ZRScope scope = ZRScope.REGION;

	
	public void run() {
		
		
		
		
	}


	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !enabled ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".containers.add.scope", scope.toString().toLowerCase() );
		
		List<Map<String, Object>> a = new ArrayList<Map<String, Object>>();
		
		for ( ItemStack i : items ) {
			
			a.add( new ZItem(i).getMap() );				
			
		}
		
		if ( !a.isEmpty() ) c.set( root + ".containers.add.items", a );
		
		List<Map<String, Object>> b = new ArrayList<Map<String, Object>>();
		
		for ( BlockState bs : entities ) {
			
			Map<String, Object> e = new HashMap<String, Object>();
			List<Integer> xyz = new ArrayList<Integer>();
			xyz.addAll( Arrays.asList( bs.getLocation().getBlockX(), bs.getLocation().getBlockY(), bs.getLocation().getBlockZ()));
			e.put("world",  bs.getLocation().getWorld().getName() );
			e.put("xyz", xyz );
			b.add( e );
		}
		
		if ( !b.isEmpty() ) c.set( root + ".containers.add.to", b );
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".containers.add.scope", "region").toUpperCase() );
		
		if ( c.isSet( root + ".containers.add.items") ) {
			List<Map<?, ?>> a = c.getMapList( root + ".containers.add.items");
			items.clear();
			
			if ( !a.isEmpty() ) {
				for ( Map<?,?> map : a ) {
					items.add( new ZItem( (Map<String, Object>)map ).getItemStack() );
				}
			}
			
		}
		
		if ( c.isSet( root + ".containers.add.to") ) {
			List<Map<?, ?>> a = c.getMapList( root + ".containers.add.to");
			entities.clear();
			
			if ( !items.isEmpty() ) {
				for ( Map<?,?> map : a ) {
					items.add(  );
				}
			}
			
		}
		
		if ( !b.isEmpty() ) c.set( root + ".containers.add.to", b );		
		
	}

}
