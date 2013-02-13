package com.github.mineGeek.ZoneRest.Data;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;



public class ZRBlocks implements Serializable {

	private static final long serialVersionUID = 1L;
	private int neX;
	private int neY;
	private int neZ;
	private int swX;
	private int swY;
	private int swZ;
	
	private String worldName;
	
	private ArrayList<ZRBlock> blocks = new ArrayList<ZRBlock>();
	
	public ZRBlocks( Location ne, Location sw ) {
		
		this.neX = ne.getBlockX();
		this.neY = ne.getBlockY();
		this.neZ = ne.getBlockZ();
		
		this.swX = sw.getBlockX();
		this.swY = sw.getBlockY();
		this.swZ = sw.getBlockZ();
		
		this.worldName = ne.getWorld().getName();
		
	}
	
	public ArrayList<ZRBlock> getBlocks() {
		return this.blocks;
	}
	
	public void copyBlocks() {
		
		
		int fromX = Math.min( this.neX, this.swX );
		int fromY = Math.min( this.neY, this.swY );
		int fromZ = Math.min( this.neZ, this.swZ );
		
		int toX = Math.max( this.neX, this.swX );
		int toY = Math.max( this.neY, this.swY );
		int toZ = Math.max( this.neZ, this.swZ );		
		
		World world = Bukkit.getWorld( this.worldName );
		
		for ( int x = fromX; x <= toX; x++ ) {
			for ( int z=fromZ; z <= toZ; z++ ) {
				for (int y=fromY; y<=toY; y++ ) {
					blocks.add( new ZRBlock( world.getBlockAt(x, y, z)));
				}
			}
		}
		
	}
	
}
