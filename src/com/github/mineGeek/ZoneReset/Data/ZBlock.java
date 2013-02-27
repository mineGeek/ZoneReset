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
 * @author moí
 *
 */
public class ZBlock implements Serializable {

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int z;
	public int materialId;
	public byte data;
	public boolean hasInventory = false;
	public boolean deferred = false;
	public boolean isContainer = false;
	
	public ArrayList<String> lines = null;
	public ArrayList<ZItem> items = null;
	
	public ZBlock( Block block, boolean deferred ) {
		
		this(block);
		this.deferred = deferred;
		
	}
	
	public ZBlock ( Block block ) {
		
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
			isContainer = true;
			Inventory inv = null;
			if ( block.getState() instanceof Chest ) {
				inv = ((Chest)block.getState()).getBlockInventory();				
			} else  {
				inv = ((InventoryHolder)block.getState()).getInventory();
			}
			
			if ( inv.getContents().length > 0 ) {
				this.hasInventory = true;
				this.items = new ArrayList< ZItem >();
				for ( int i = 0; i < inv.getContents().length; i++ ) {
					if ( inv.getItem(i) != null ) items.add( new ZItem( inv.getItem(i)));
				}
			
			}
			
		}

		
	}
	
}
