package com.github.mineGeek.ZoneReset.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;


public class ZRItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public int materialId;
	public short durability;
	public int amount;
	public byte data;
	
	public Map<String, Object> meta = null;
	
	public Map<String, Object > serialized = null;
	
	public ZRItem( ItemStack item ) {
		this.serialized = item.serialize();
		this.materialId = item.getTypeId();
		this.durability = item.getDurability();
		this.amount = item.getAmount();
		this.data = item.getData().getData();
		
		if ( item.hasItemMeta() ) {
			this.meta = new HashMap<String, Object>();
			Map<String, Object> m = item.getItemMeta().serialize();
			
			for ( String x : m.keySet() ) {
				this.meta.put( x , m.get(x) );
			}
			
		}
		boolean b = true;
	}
	
	
	public ItemStack getItemStack() {
		
		return ItemStack.deserialize( this.serialized );
		
	}
	
	
}
