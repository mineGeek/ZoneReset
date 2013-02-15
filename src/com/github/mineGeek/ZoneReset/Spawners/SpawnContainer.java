package com.github.mineGeek.ZoneReset.Spawners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


/**
 * Special item spawner that containes other items
 * Will not spawn base item, but will fill container
 * at Location with its contents
 * 
 * @author Moí
 *
 */
public class SpawnContainer extends SpawnBase {

	/**
	 * The material ID of the container
	 */
	private Material material;
	
	/**
	 * List of items to fill container with
	 */
	private List<ItemSpawn> items = new ArrayList<ItemSpawn>();
	
	/**
	 * Returns container material
	 * @return
	 */
	public Material getMaterial() {
		return this.material;
	}
	
	/**
	 * Sets container material
	 * @param m
	 */
	public void setMaterial( Material m ) {
		this.material = m;
	}
	
	/**
	 * Returns list of items to spawn in container
	 * @return
	 */
	public List<ItemSpawn> getItems() {
		return this.items;
	}
	
	/**
	 * Sets the items to fill the container with
	 * @param items
	 */
	public void setItems( List<ItemSpawn> items ) {
		this.items = items;
	}
	
	/**
	 * Clear any items in the spawn queue
	 */
	public void clearItems() {
		this.items.clear();
	}
	
	/**
	 * Add an item to add to the spawn queue
	 * @param item
	 */
	public void addItem( ItemSpawn item ) {
		this.items.add( item );
	}
	
	
	/**
	 * Standard constructor
	 */
	public SpawnContainer() {}
	
	/**
	 * Constructor that take the container Block and 
	 * calculates the inventory from there
	 * @param block
	 */
	public SpawnContainer( Block block ) {
		
		this.setType( ZRSPAWNTYPE.CONTAINER );
		this.setMaterial( block.getState().getType() );
		this.setData( block.getState().getRawData() );
		this.setLocation( block.getLocation() );
		
		if ( block.getState() instanceof InventoryHolder ) {
			
			Inventory inv = ((InventoryHolder)block.getState()).getInventory();
			this.clearItems();
			for ( ItemStack i : inv.getContents() ) {
				
				if ( i != null ) this.addItem( new ItemSpawn( i ) );
			}
			
		}
		
	}

	/**
	 * Returns a Map representation of this object
	 * for easy saving to config file.
	 */
	@Override
	public Map<String, Object> getList() {
		
		
		Map<String, Object> r = (Map<String, Object>) super.getList( this );
		if ( this.getMaterial() != null ) r.put("item", this.getMaterial().getId() );

		
		if ( !this.getItems().isEmpty() ) {
			List<Object> items = new ArrayList<Object>();
			
			for ( ItemSpawn x : this.getItems() ) {
				//remove redundant tags
				Map<String, Object> m = x.getList();
				m.remove("type");
				
				items.add( m );
			}
			
			r.put("contains", items );
			
		}
		
		return r;
	}
	
	/**
	 * Sets this object from a config list object
	 */
	public void setList( Map<String, Object> list ) {
		
		super.setList( list );
		if ( list.containsKey("item") ) this.setMaterial( Material.getMaterial( (Integer) list.get("item")));
		if ( list.containsKey("contains") ) {
			
			@SuppressWarnings("unchecked")
			List< Map<String, Object>> items = (List<Map<String, Object>>) list.get("items");
			
			if ( items != null ) {
				for ( Map<String, Object> i : items ) {
					
					ItemSpawn spawn = new ItemSpawn();
					spawn.setList( i );
					this.addItem( spawn );
					
				}
			}
			
		}
	}

	/**
	 * Handles spawning of item and its contents
	 */
	@Override
	public void spawn() {
		
		
		if ( this.isHasLocation() ) {
			
			Location l = this.getLocation();
			Block b = l.getWorld().getBlockAt(l);
			b.setType( Material.AIR );
			b.setType( this.getMaterial() );
			BlockState bs = b.getState();
			bs.setData( new MaterialData( this.getMaterial(), this.getData() ) );
			Inventory inv = ((InventoryHolder)bs).getInventory();
			
			if ( !this.getItems().isEmpty() ) {
				
				for ( ItemSpawn i : this.getItems() ) {
					inv.addItem( i.getAsItemStack() );
				}
				
			}
			
			
		}
		
	}

}
