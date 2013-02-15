package com.github.mineGeek.ZoneReset.Spawners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


public class ItemSpawn extends SpawnBase {
	
	private Material material;
	private short durability;
	private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
	
	public Material getMaterial() {
		return this.material;
	}
	
	public void setMaterial( Material m ) {
		this.material = m;
	}
	
	public short getDurability() {
		return this.durability;
	}
	
	public void setDurability( short value ) {
		this.durability = value;
	}
	
	public Map<Enchantment, Integer> getEnchantments() {
		return this.enchantments;
	}
	
	public void setEnchantments( Map<Enchantment, Integer> enchants ) {
		this.enchantments = enchants;
	}
	
	public void clearEnchantments() {
		this.enchantments.clear();
	}
	
	public void addEnchantment( Enchantment e, Integer i ) {
		this.getEnchantments().put(e, i);
	}
	
	public ItemSpawn( ItemStack item ) {
		
		this.setType( ZRSPAWNTYPE.ITEM );
		this.setMaterial( item.getType() );
		this.setQty( item.getAmount() );
		this.setData( item.getData().getData() );
		this.setDurability( item.getDurability() );
		this.setEnchantments( item.getEnchantments() );
		
	}

	public ItemSpawn() {
		// but. why?
	}

	@Override
	public Map<String, Object> getList() {
		
		Map<String, Object> r = (Map<String, Object>) super.getList( this );
		
		r.put("item", this.getMaterial().getId() );
		if ( this.getDurability() != 0 ) r.put("durability", this.getDurability() );
		
		if ( !this.getEnchantments().isEmpty() ) {
			Map<String, Integer> emap = new HashMap<String, Integer>();
			for ( Enchantment e : this.getEnchantments().keySet() ) {
				emap.put( e.toString().toLowerCase(), this.getEnchantments().get(e) );
			}
			r.put("enchantments", emap );
		}
		
		return r;
	}
	
	@Override
	public void setList( Map<String, Object> list ) {
		
		
		super.setList( list );
		this.setType( ZRSPAWNTYPE.ITEM );
		if ( list.containsKey("item") ) this.setMaterial( Material.getMaterial( ( Integer) list.get("item") ) );
		if ( list.containsKey("durability") )  this.setDurability( (Short) list.get("durability") );
		
		if ( list.containsKey( "enchantments") ) {
			
			@SuppressWarnings("unchecked")
			Map<String, Integer> emap = (Map<String, Integer>) list.get("enchantments");
			
			if ( !emap.isEmpty() ) {
				
				for ( String x : emap.keySet() ) {
					this.addEnchantment( Enchantment.getByName( x.toUpperCase() ), emap.get(x) );
				}
				
			}
			
		}
		
		
	}

	public ItemStack getAsItemStack() {
		
		ItemStack i = new ItemStack( this.getMaterial() );
		i.setAmount( this.getQty() );
		i.setData( new MaterialData( this.getData() ) );
		i.setDurability( this.getDurability() );
		
		if ( !this.getEnchantments().isEmpty() ) {
			
			for ( Enchantment e : this.getEnchantments().keySet() ) {
				
				i.addEnchantment( e, this.getEnchantments().get(e) );
				
			}
			
		}
		
		return i;
		
	}
	
	@Override
	public void spawn() {

		if ( this.isHasLocation() ) {
			//Drop it!
			Location l = this.getLocation();
			if ( l != null ) {
				l.getWorld().dropItem( l, this.getAsItemStack() );
			}
		}
		
		
		
	}

}
