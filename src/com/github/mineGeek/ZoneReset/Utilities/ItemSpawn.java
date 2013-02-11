package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ItemSpawn implements SpawnInterface {

	private Material material;
	private Integer qty;
	private Byte data;
	private Short durability;
	private String worldName;
	private Integer x;
	private Integer y;
	private Integer z;
	public List<ItemSpawn> contains = new ArrayList<ItemSpawn>();
	
	Map<Enchantment, Integer> enhants = new HashMap<Enchantment, Integer>();
	
	public ItemSpawn() {}
	
	public ItemSpawn( Material m, byte data, short durability, int qty ) {
		this.material = m;
		this.data = data;
		this.durability = durability;
		this.qty = qty;		
		
	}
	
	public ItemSpawn( Material m, byte data, short durability, int qty, String worldName, int x, int y, int z) {
	
		this(m, data, durability, qty);
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		
		
	}
	
	public ItemSpawn( ItemStack i, Location l ) {
		
		this(i.getType(), i.getData().getData(), i.getDurability(), i.getAmount(), l.getWorld().getName(), l.getBlockX(), l.getBlockY(), l.getBlockY() );
		this.setEnchantments( i.getEnchantments() );
	
	}
	
	public ItemSpawn( int materialId, String worldName, ArrayList<Integer> location, byte data, short durability, int qty ) {
		
	}
	
	public ItemSpawn( int materialId ) {
		this.material = Material.getMaterial( materialId );
	}
	
	
	
	public ItemSpawn( Map<?,?> item ) {
		
		this.setList( item );
				
	}
	
	@SuppressWarnings("unchecked")
	public void setList( Map<?,?> item ) {
		
		if ( item.containsKey("item") ) this.material = Material.getMaterial( (Integer) item.get("item") );		
		if ( item.containsKey("data") ) this.data = (Byte) item.get("data");
		if ( item.containsKey("durability" ) ) this.durability = (Short) item.get("durability");
		if ( item.containsKey("qty") ) this.qty = (Integer) item.get("qty");
		if ( item.containsKey("world") ) this.worldName = (String) item.get("world");
		if ( item.containsKey("location") ) {
			List<Integer> loc = (List<Integer>) item.get("location");
			this.x = loc.get(0);
			this.y = loc.get(1);
			this.z = loc.get(2);
		}
		if ( item.containsKey("enchants") ) {
			this.enhants.clear();
			Map<String, Integer> e = (Map<String, Integer>) item.get("enchants");
			
			if ( !e.isEmpty() ) {
				for ( String x : e.keySet() ) {
					this.addEnchantment(x, e.get(x));
				}
			}
		}
		if ( item.containsKey("contains") ) {
			this.contains.clear();
			List<Map<?, ?>> items = (List<Map<?, ?>>) item.get("contains");
			
			for ( Map<?, ?> inner : items ) {
				this.contains.add( new ItemSpawn( inner ) );
			}
		}		
		
	}
	
	public Map<?, ?> getList( ItemSpawn i ) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		if ( i.material != null ) r.put( "item", this.material.getId() );
		if ( i.data != 0) r.put( "data", this.data );
		if ( i.durability != 0 ) r.put( "durability", this.durability );
		if ( i.qty != 0 ) r.put("qty", this.qty );
		if ( i.worldName != null ) r.put("world", this.worldName );
		r.put("location", new ArrayList<Integer>( Arrays.asList( this.x, this.y, this.z  )));
		if ( !this.enhants.isEmpty() ) {
			r.put("enchants", this.enhants );
		}
		
		if ( !this.contains.isEmpty() ) {
			List<Map<?, ?>> inners = new ArrayList<Map<?, ?>>();
			for( ItemSpawn inner : this.contains ) {
				inners.add( this.getList( inner) );
			}
			
		}
		
		return r;
		
	}
	
	public Map<?, ?> getList() {
		return this.getList( this );		
	}
	
	
	public void addEnchantment( String e, Integer i ) {
		this.enhants.put( Enchantment.getByName(e), i );
	}
	
	public void setEnchantments( Map<Enchantment, Integer> e ) {
		this.enhants = e;
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ZR_SPAWN_TYPE getType() {
		return ZR_SPAWN_TYPE.ITEM;
	}
	
	
}
