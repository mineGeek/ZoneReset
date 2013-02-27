package com.github.mineGeek.ZoneReset.Data;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;


/**
 * Utility class for staking out areas as a cuboid
 *
 */
public class Area {

	/**
	 * corner 1 of area
	 */
	private Location cachedNe;
	private int neX;
	private int neY;
	private int neZ;
		
	/**
	 * opposite corner of cuboid
	 */
	public Location cachedSw;
	private int swX;
	private int swY;
	private int swZ;
	
	public String worldName = null;
	
	
	
	/**
	 * Return the NE corner
	 * @return
	 */
	public Location ne() {
		
		if ( cachedNe == null ) {
			
			World world = Bukkit.getWorld( this.worldName );
			if ( world != null ) {
				cachedNe = new Location( world, neX, neY, neZ );
			}
			
		}
		
		return cachedNe;
	}
	
	/**
	 * Set the NE corner with a Location object
	 * @param ne
	 */
	public void setNe( Location ne ) {
		this.worldName = ne.getWorld().getName();
		this.cachedNe = ne;
	}
	
	/**
	 * Return the SW corner of the area
	 * @return
	 */
	public Location sw() {
		
		if ( cachedSw == null ) {
			
			World world = Bukkit.getWorld( this.worldName );
			if ( world != null ) {
				cachedSw = new Location( world, swX, swY, swZ );
			}
			
		}
		
		return cachedSw;
	}
	
	/**
	 * Set the SW corner of region with a Location object
	 * @param sw
	 */
	public void setSw( Location sw ) {
		this.worldName = sw.getWorld().getName();
		this.cachedSw = sw;
	}
	
	
	/**
	 * Just look purdy.
	 */
	public Area() {}
	
	/**
	 * Constructor taking the 2 points of cuboid
	 * @param ne
	 * @param sw
	 */
	public Area( Location ne, Location sw ) {
		
		this.setNe( ne );		
		this.setSw( sw );
		
	}
	
	/**
	 * Constructor that sets all points at once
	 * @param world
	 * @param neX
	 * @param neY
	 * @param neZ
	 * @param swX
	 * @param swY
	 * @param swZ
	 */
	public Area( World world, Integer neX, Integer neY, Integer neZ, Integer swX, Integer swY, Integer swZ ) {
		
		this( new Location( world, neX, neY, neZ) , new Location( world, swX, swY, swZ ) );
		
	}
	
	/**
	 * Another way of constructing
	 * @param worldName
	 * @param neX
	 * @param neY
	 * @param neZ
	 * @param swX
	 * @param swY
	 * @param swZ
	 */
	public Area( String worldName, Integer neX, Integer neY, Integer neZ, Integer swX, Integer swY, Integer swZ ) {
		
		this.neX = neX;
		this.neY = neY;
		this.neZ = neZ;
		this.swX = swX;
		this.swY = swY;
		this.swZ = swZ;
		this.worldName = worldName;
		
		this.cachedNe = null;
		this.cachedSw = null;
		
	}	
	
	/**
	 * Constructor that sets all variables from lists
	 * @param world
	 * @param ne
	 * @param sw
	 */
	public Area( World world, List<Integer> ne, List<Integer> sw ) {
		this( world, ne.get(0), ne.get(1), ne.get(2), sw.get(0), sw.get(1), sw.get(2));
	}
	
	/**
	 * Look. Another constructor
	 * @param worldName
	 * @param ne
	 * @param sw
	 */
	public Area( String worldName, List<Integer> ne, List<Integer> sw ) {

		this.cachedNe = null;
		this.cachedSw = null;
		this.neX = ne.get(0);
		this.neY = ne.get(1);
		this.neZ = ne.get(2);
		this.swX = sw.get(0);
		this.swY = sw.get(1);
		this.swZ = sw.get(2);
		this.worldName = worldName;

	}	
	
	/**
	 * Constructor that sets all vars from a single list
	 * @param world
	 * @param loc
	 */
	public Area( World world, List<Integer> loc ) {
		this( world, loc.get(0), loc.get(1), loc.get(2), loc.get(3), loc.get(4), loc.get(5));
	}

	/**
	 * Will return true if location passed is within the cuboid
	 * @param l
	 * @return
	 */
	public Boolean intersectsWith( Location l ) {	

		Location ne = ne();
		Location sw = sw();

		
		if ( l.getWorld().getName().equals( ne.getWorld().getName() ) ) {
			if ( ( Math.max( ne.getBlockX(), sw.getBlockX() ) < l.getBlockX() ) || ( Math.min( ne.getBlockX(), sw.getBlockX() ) > l.getBlockX() ) ) return false;
			if ( ( Math.max( ne.getBlockZ(), sw.getBlockZ() ) < l.getBlockZ() ) || ( Math.min( ne.getBlockZ(), sw.getBlockZ() ) > l.getBlockZ() ) ) return false;
			if ( ( Math.max( ne.getBlockY(), sw.getBlockY() ) < l.getBlockY() ) || ( Math.min( ne.getBlockY(), sw.getBlockY() ) > l.getBlockY() ) ) return false;	
			return true;
		} else {
			return false;
		}
		
		
	}
	
	/**
	 * Get all the ZRBlocks in this area.
	 * TODO: Won't this fart out if only 1 location object is set?
	 * @return
	 */
	public ZArea getBlocks() {
		
		ZArea z = new ZArea( this.ne(), this.sw() );
		z.copyBlocks();
		z.copyEntities();
		return z;
		
	}
	
	
	/**
	 * Good guy closure
	 */
	public void close() {
		this.cachedNe = null;
		this.cachedSw = null;
	}

	
	
}
