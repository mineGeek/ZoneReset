package com.github.mineGeek.ZoneReset.Utilities;

import java.io.Serializable;

import org.bukkit.block.Block;

public class ZoneBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int z;
	public int materialId;
	public byte data;
	
	public ZoneBlock ( Block block ) {
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.materialId = block.getTypeId();
		this.data = block.getData();
	}
}
