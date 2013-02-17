package com.github.mineGeek.ZoneReset.Data;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Block data object
 * @author mo�
 *
 */
public class ZRBlock implements Serializable {

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int z;
	public int materialId;
	public byte data;
	public boolean hasInventory = false;
	public boolean deferred = false;
	
	public ArrayList<String> lines = null;
	public ArrayList<ItemSerializable> items = null;
	
	public ZRBlock( Block block, boolean deferred ) {
		
		this(block);
		this.deferred = deferred;
		
	}
	
	public ZRBlock ( Block block ) {
		
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.materialId = block.getTypeId();
		this.data = block.getState().getRawData();
		
		
		if ( block.getState() instanceof Sign ) {
			this.lines = new ArrayList<String>();
			String[]lines = ((Sign)block.getState()).getLines();
			for ( int i = 0; i < lines.length; i++ ) {
				this.lines.add( lines[i] );
			}
		}
		
		if ( block.getState() instanceof InventoryHolder ) {
		
			Inventory inv = null;
			if ( block.getState() instanceof Chest ) {
				inv = ((Chest)block.getState()).getBlockInventory();				
			} else  {
				inv = ((InventoryHolder)block.getState()).getInventory();
			}
			
			if ( inv.getContents().length > 0 ) {
				this.hasInventory = true;
				this.items = new ArrayList< ItemSerializable >();
				for ( int i = 0; i < inv.getContents().length; i++ ) {
					if ( inv.getItem(i) != null ) items.add( new ItemSerializable( inv.getItem(i)));
				}
			
			}
			
		}

		
	}
	
}
