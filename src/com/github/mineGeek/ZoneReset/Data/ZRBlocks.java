package com.github.mineGeek.ZoneReset.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


/**
 * Container for all zones in a cuboid
 * @author moí
 *
 */
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
	private ArrayList<MobSerializable> mobs = new ArrayList<MobSerializable>();
	
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
	
	public ArrayList<MobSerializable> getMobs() {
		return this.mobs;
	}
	
	public void copyBlocks() {
		
		
		int fromX = Math.min( this.neX, this.swX );
		int fromY = Math.min( this.neY, this.swY );
		int fromZ = Math.min( this.neZ, this.swZ );
		
		int toX = Math.max( this.neX, this.swX );
		int toY = Math.max( this.neY, this.swY );
		int toZ = Math.max( this.neZ, this.swZ );		
		
		World world = Bukkit.getWorld( this.worldName );
		List<ZRBlock> deferred = new ArrayList<ZRBlock>();
		for ( int x = fromX; x <= toX; x++ ) {
			for ( int z=fromZ; z <= toZ; z++ ) {
				for (int y=fromY; y<=toY; y++ ) {
					
					Block b = world.getBlockAt( x, y, z );
					
					if ( b.getState() instanceof Sign ) {
						deferred.add( new ZRBlock( b, true ) );
					} else {
						blocks.add( new ZRBlock( b ));
					}
					
				}
			}
		}

		blocks.addAll( deferred );
		
	}
	
	public void copyEntities() {
		
		
		World world = Bukkit.getWorld( this.worldName );
		Area area = new Area( new Location( world, this.neX, this.neY, this.neZ), new Location( world, this.swX, this.swY, this.swZ) );
		
		int fromX = Math.min( area.ne().getChunk().getX(), area.sw().getChunk().getX() );
		int fromZ = Math.min( area.ne().getChunk().getZ(), area.sw().getChunk().getZ() );
		
		int toX = Math.max(  area.ne().getChunk().getX(), area.sw().getChunk().getX() );
		int toZ = Math.max( area.ne().getChunk().getZ(), area.sw().getChunk().getZ() );		
		
				
		List<Chunk> chunks = new ArrayList<Chunk>();
		
		for ( int x = fromX; x <= toX; x++ ) {
			for ( int z=fromZ; z <= toZ; z++ ) {
				chunks.add( world.getChunkAt(x, z) );
			}
		}
		
		if ( !chunks.isEmpty() ) {
			
			for ( Chunk chunk : chunks ) {
				
				for ( Entity e : chunk.getEntities() ) {
					
					if ( area.intersectsWith( e.getLocation() ) ) {
						
						if ( e instanceof LivingEntity && !(e instanceof Player ) ) {
							
							mobs.add( new MobSerializable( e ) );
							
						} else {
							Bukkit.getLogger().info("Skipping entity: " + e.getType().getName() );
						}
						
					}
					
				}
				
			}
			
		}
		
		
	}
	
}
