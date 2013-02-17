package com.github.mineGeek.ZoneReset.Spawners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;


public class ItemSpawn extends SpawnBase implements ItemInterface {
	
	private Material material;
	private short durability;
	private Map<Integer, Integer> enchantments = new HashMap<Integer, Integer>();
	private ItemMeta meta;
	
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
	
	public Map<Integer, Integer> getEnchantments() {
		return this.enchantments;
	}
	
	public void setEnchantments( Map<Integer, Integer> enchants ) {
		this.enchantments = enchants;
	}
	
	public void clearEnchantments() {
		this.enchantments.clear();
	}
	
	public void addEnchantment( Integer e, Integer i ) {
		this.getEnchantments().put(e, i);
	}
	
	public ItemSpawn( ItemStack item ) {
		
		this.setType( ZRSPAWNTYPE.ITEM );
		this.setMaterial( item.getType() );
		this.setQty( item.getAmount() );
		this.setData( item.getData().getData() );
		this.setDurability( item.getDurability() );
		//this.setEnchantments( item.getEnchantments() );
		if ( !item.getEnchantments().isEmpty() ) {
			for ( Enchantment e : item.getEnchantments().keySet() ) {
				this.addEnchantment( e.getId(), item.getEnchantmentLevel( e ) );
			}
		}
				
		if ( item.hasItemMeta() ) {
			this.meta = item.getItemMeta();
		}
		
	}

	public ItemSpawn() {
		// but. why?
	}

	@Override
	public Map<String, Object> getList() {
		
		Map<String, Object> r = (Map<String, Object>) super.getList( this );
		
		r.put("item", this.getMaterial().getId() );
		if ( this.getDurability() != 0 ) r.put("durability", this.getDurability() );
		/*
		if ( !this.getEnchantments().isEmpty() ) {
			Map<Integer, Integer> emap = new HashMap<Integer, Integer>();
			for ( Integer e : this.getEnchantments().keySet() ) {
				emap.put( e, this.getEnchantments().get(e) );
			}
			r.put("enchantments", emap );
		}
		*/
		if ( this.meta != null ) {
			r.put( "meta", this.meta );
		}
		
		return r;
	}
	
	@Override
	public void setList( Map<String, Object> list ) {
		
		
		super.setList( list );
		this.setType( ZRSPAWNTYPE.ITEM );
		if ( list.containsKey("item") ) this.setMaterial( Material.getMaterial( ( Integer) list.get("item") ) );
		if ( list.containsKey("durability") )  this.setDurability( (Short.parseShort( list.get("durability").toString() ) ) );
		/*
		if ( list.containsKey( "enchantments") ) {
			
			@SuppressWarnings("unchecked")
			Map<Integer, Integer> emap = (Map<Integer, Integer>) list.get("enchantments");
			
			if ( !emap.isEmpty() ) {
				
				for ( Integer x : emap.keySet() ) {
					this.addEnchantment( x, emap.get(x) );
				}
				
			}
			
		}
		*/
		if ( list.containsKey( "meta" ) ) {
			this.meta = (ItemMeta) list.get("meta");
		}
		
		
	}

	public ItemStack getAsItemStack() {
		
		ItemStack i = new ItemStack( this.getMaterial() );
		i.setAmount( this.getQty() );
		i.setData( new MaterialData( this.getData() ) );
		i.setDurability( this.getDurability() );
		/*
		if ( !this.getEnchantments().isEmpty() ) {
			
			for ( Integer e : this.getEnchantments().keySet() ) {
				try {
					i.addEnchantment( new EnchantmentWrapper( e ), this.getEnchantments().get(e) );
				} catch ( Exception ex ) {}
				
			}
			
		}
		*/
		if ( this.meta != null ) {
			i.setItemMeta( this.meta );
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
