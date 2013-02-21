package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.ZItem;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionFillTileEntities extends Action {
	
	public List<ItemStack> items = new ArrayList<ItemStack>();
	public List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
	public ZRScope scope = ZRScope.REGION;

	
	public void run() {
		
		if ( !entities.isEmpty() ) {
			
			for ( Map<String, Object> ent : entities ) {
				BlockState bs = getBlockState( ent );
				if ( ent != null ) {
					
					Inventory inv = null;
					
					if ( bs instanceof Chest ) {
						inv = ((Chest)bs).getBlockInventory();				
					} else  {
						inv = ((InventoryHolder)bs).getInventory();
					}
					
					for ( ItemStack i : items ) {
						inv.addItem(i);
					}					
					
				}
			}
			
		}
		
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !enabled ) return;
		
		//if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".containers.add.scope", scope.toString().toLowerCase() );
		
		List<Map<String, Object>> a = new ArrayList<Map<String, Object>>();
		
		for ( ItemStack i : items ) {
			
			a.add( new ZItem(i).getMap() );				
			
		}
		
		if ( !a.isEmpty() ) c.set( root + ".containers.add.items", a );
		
		List<Map<String, Object>> b = new ArrayList<Map<String, Object>>();
		
		for ( Map<String, Object> map : entities ) {
				
				BlockState bs = this.getBlockState( map );
				if ( bs != null) b.add( map );			
		}
		
		if ( !b.isEmpty() ) c.set( root + ".containers.add.to", b );
		
	}

	
	public List<BlockState> getList() {
		
		List<BlockState> bs = new ArrayList<BlockState>();
		List<Chunk> chunks = null;
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			chunks = Utilities.getChunksFromArea( Zones.getZone( this.tag ).getArea() );

		}
		
		Area a = Zones.getZone( this.tag ).getArea();
		
		for ( Chunk c : chunks ) {
			
			for ( BlockState b : c.getTileEntities() ) {
				if ( a.intersectsWith( b.getLocation() ) ) {
					bs.add( b );
				}
			}
			
		}
		
		return bs;
		
	}
	

	@SuppressWarnings("unchecked")
	private BlockState getBlockState( Map<String, Object> map ) {
		
		World w = null;
		Location l = null;
		List<Integer> xyz = null;
		Block block = null;
		BlockState bs = null;
		
		if ( map.containsKey( "world") && map.containsKey("xyz") ) {
			w = Bukkit.getWorld( (String) map.get("world") );
			if ( w != null ) xyz = (List<Integer>) map.get("xyz");
			if ( xyz != null ) l = new Location( w, xyz.get(0), xyz.get(1), xyz.get(2));
			if ( l != null )block = w.getBlockAt( l );
			if ( block != null ) bs = block.getState();				
		}
		return bs;
		
	}
	
	@SuppressWarnings({ "unchecked" })
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
					entities.add( (Map<String, Object>) map );
				}
			}
			
		}
		
		if ( !entities.isEmpty() ) c.set( root + ".containers.add.to", entities );		
		
	}

}
