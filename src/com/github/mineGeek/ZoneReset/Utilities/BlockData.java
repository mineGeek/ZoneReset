package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockData {

	private int x;
	private int y;
	private int z;
	private int materialId;
	private byte data;
	private String worldName;
	
	public BlockData( Block block ) {
		
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.materialId = block.getTypeId();
		this.data = block.getData();
		this.worldName = block.getWorld().getName();
		
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getMaterialId() {
		return this.materialId;
	}
	
	public byte getData() {
		return this.data;
	}
	
	public Location getLocation() {
		return new Location( Bukkit.getWorld( this.worldName ), this.x, this.y, this.z );
	}
	
}
