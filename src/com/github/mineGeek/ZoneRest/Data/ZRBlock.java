package com.github.mineGeek.ZoneRest.Data;

import java.io.Serializable;

import org.bukkit.block.Block;

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
