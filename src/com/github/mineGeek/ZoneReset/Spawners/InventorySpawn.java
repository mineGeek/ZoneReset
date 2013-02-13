package com.github.mineGeek.ZoneReset.Spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class InventorySpawn extends SpawnBase {

	List<ItemSpawn> items = new ArrayList<ItemSpawn>();
	
	public void clearItems() {
		this.items.clear();
	}
	
	public void setItems( List<ItemSpawn> items ) {
		this.items =items;
	}
	
	public List<ItemSpawn> getItems() {
		return this.items;
	}

	@Override
	public Map<String, Object> getList() {
		
		
		if ( !this.getItems().isEmpty() ) {
			Map<String, Object> r = (Map<String, Object>) super.getList( this );
			List<Object> items = new ArrayList<Object>();
			for ( ItemSpawn x : this.getItems() ) {
				items.add( x.getList() );
			}
			r.put( "items", items );
			return r;
		} else {
			return null;
		}
		
		
	}
	
	@Override
	public void setList( Map<String, Object> list ) {
		
		super.setList( list );
		if ( list.containsKey( "items") ) {
			
			@SuppressWarnings("unchecked")
			List< Map<String, Object>> items = (List<Map<String, Object>>) list.get("items");
			this.clearItems();
			for ( Map<String, Object> item : items ) {
				ItemSpawn i = new ItemSpawn();
				i.setList(item);
				this.getItems().add( i );
			}
			
		}
		
	}

	public void reverseApply( Player p ) {
		
		if ( !this.getItems().isEmpty() ) {
			
			for ( ItemSpawn i : this.getItems() ) {
				
				if ( p.getInventory().contains( i.getAsItemStack() ) ) {
					p.getInventory().remove( i.getAsItemStack() );
				}
				
			}
			
		}
		
	}
	
	public void apply( Player p, boolean emptyFirst ) {
		
		if ( emptyFirst ) {
			p.getInventory().clear();
		}
		
		if ( !this.getItems().isEmpty() ) {
			
			for ( ItemSpawn i : this.getItems() ) {
				
				p.getInventory().addItem( i.getAsItemStack() );
				
			}
			
		}
		
		
	}
	
	public void apply( Player p ) {
		
		this.apply(p, true );
		
	}
	
	@Override
	public void spawn() {
		// ha.ha. You can't spawn this shit.
		
	}
	

}
