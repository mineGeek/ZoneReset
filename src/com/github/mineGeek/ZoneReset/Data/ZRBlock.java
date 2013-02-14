package com.github.mineGeek.ZoneReset.Data;

import java.io.Serializable;

import org.bukkit.block.Block;

/**
 * Block data object
 * @author moí
 *
 */
public class ZRBlock implements Serializable {

	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int z;
	public int materialId;
	public byte data;
	
	public ZRBlock ( Block block ) {
		
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.materialId = block.getTypeId();
		this.data = block.getState().getRawData();		
				
	}
	
}
