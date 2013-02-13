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

public class SpawnContainer extends SpawnBase {

	private Material material;
	private List<ItemSpawn> items = new ArrayList<ItemSpawn>();
	
	
	
	public Material getMaterial() {
		return this.material;
	}
	
	public void setMaterial( Material m ) {
		this.material = m;
	}
	
	public List<ItemSpawn> getItems() {
		return this.items;
	}
	
	public void setItems( List<ItemSpawn> items ) {
		this.items = items;
	}
	
	public void clearItems() {
		this.items.clear();
	}
	
	public void addItem( ItemSpawn item ) {
		this.items.add( item );
	}
	
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

	public SpawnContainer() {}

	@Override
	public Map<String, Object> getList() {
		
		Map<String, Object> r = (Map<String, Object>) super.getList( this );
		if ( this.getMaterial() != null ) r.put("item", this.getMaterial().getId() );

		
		if ( !this.getItems().isEmpty() ) {
			List<Object> items = new ArrayList<Object>();
			
			for ( ItemSpawn x : this.getItems() ) {
				
				//remove spam tags
				Map<String, Object> m = x.getList();
				m.remove("type");
				
				items.add( m );
			}
			r.put("contains", items );
		}
		
		return r;
	}
	
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
