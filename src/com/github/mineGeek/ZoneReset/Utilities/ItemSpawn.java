package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

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
		if ( item.containsKey("data") && item.get("data") != null ) this.data = Byte.valueOf( String.valueOf( item.get("data") ) );
		if ( item.containsKey("durability" ) && item.get( "durability") != null ) this.durability = Short.valueOf( String.valueOf( item.get("durability") ));
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
			boolean a = true;
			a = false;
		}		
		
	}
	
	public Map<?, ?> getList( ItemSpawn i ) {
		return this.getList( i, false );
	}
	
	public Map<?, ?> getList( ItemSpawn i, boolean noContainer ) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		if ( i.material != null ) r.put( "item", this.material.getId() );
		if ( i.data != 0) r.put( "data", this.data );
		if ( i.durability != 0 ) r.put( "durability", this.durability );
		if ( i.qty != 0 ) r.put("qty", this.qty );
		if ( i.worldName != null ) r.put("world", this.worldName );
		if ( !noContainer) r.put("location", new ArrayList<Integer>( Arrays.asList( this.x, this.y, this.z  )));
		if ( !this.enhants.isEmpty() ) {
			r.put("enchants", this.enhants );
		}
		
		if ( !noContainer) {
			r.put("type", ZR_SPAWN_TYPE.ITEM.toString() );
			if ( !this.contains.isEmpty() ) {
				List<Map<?, ?>> inners = new ArrayList<Map<?, ?>>();
				for( ItemSpawn inner : this.contains ) {
					inners.add( inner.getList( inner, true) );
				}
				r.put("contains", inners);
				
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

	public ItemStack getItemStack() {
		ItemStack i = new ItemStack( this.material );
		if ( this.qty != null  ) i.setAmount( this.qty );
		if ( this.data != null ) i.setData( new MaterialData( this.data) );
		if ( this.durability != null) i.setDurability( this.durability );
		
	
		return i;
	}
	
	@Override
	public void spawn() {
	
		
		World world = Bukkit.getWorld( this.worldName );
		Location l = new Location( world, this.x, this.y, this.z );
		ItemStack i = this.getItemStack();
		
		
		if ( !i.getType().equals( Material.CHEST ) && !i.getType().equals( Material.FURNACE )) {			
			//assume a dropped item?
			world.dropItem( l, i);
			
		} else {
		
			//a container?
			Block b = l.getBlock();
			//b.setType( i.getType() );
			BlockState bs = b.getState();
			bs.update( true );
			Inventory inv = ((InventoryHolder)bs).getInventory();
						
			for ( ItemSpawn sp : this.contains ) {
				
				inv.addItem( sp.getItemStack() );
				
			}
		}
		
	}

	@Override
	public ZR_SPAWN_TYPE getType() {
		return ZR_SPAWN_TYPE.ITEM;
	}
	
	
}
