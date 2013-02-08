package com.github.mineGeek.ZoneReset.Player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class Marker {

	int[] x = {0,0,0};
	int[] y = {0,0,0};
	int[] z = {0,0,0};
	int[] typeId = {0,0,0};
	byte[] data = {0,0,0};
	String worldName;
	
	
	public Marker( Block block, Block flag1, Block flag2 ) {
	
		worldName = block.getWorld().getName();
		
		x[0] = block.getX();
		y[0] = block.getY();
		z[0] = block.getZ();
		typeId[0] = block.getTypeId();
		data[0] = block.getData();

		x[1] = flag1.getX();
		y[1] = flag1.getY();
		z[1] = flag1.getZ();
		typeId[1] = flag1.getTypeId();
		data[1] = flag1.getData();
		
		x[2] = flag2.getX();
		y[2] = flag2.getY();
		z[2] = flag2.getZ();
		typeId[2] = flag2.getTypeId();
		data[2] = flag2.getData();
		
	}
	
	public void highlight( Player player ) {
		
		Location l1 = new Location( Bukkit.getWorld( worldName ), x[0], y[0], z[0] );
		Location l2 = new Location( Bukkit.getWorld( worldName ), x[1], y[1], z[1] );
		Location l3 = new Location( Bukkit.getWorld( worldName ), x[2], y[2], z[2] );
		player.sendBlockChange( l1,  89, (byte)0 );
		player.sendBlockChange( l2,  41, (byte)0 );
		player.sendBlockChange( l3,  41, (byte)0 );
		
		
	}
	
	public void unhighlight( Player player ) {
		
		for ( int i=0; i<3 ; i++ ) {
			Location l = new Location( Bukkit.getWorld( worldName ), x[i], y[i], z[i] );
			player.sendBlockChange( l,  typeId[i], data[i] );
		}		
	}	
	
}
