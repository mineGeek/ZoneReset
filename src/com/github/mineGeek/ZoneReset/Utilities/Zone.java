package com.github.mineGeek.ZoneReset.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.ZRBlock;
import com.github.mineGeek.ZoneReset.Data.ZRBlocks;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;
import com.github.mineGeek.ZoneReset.nms.NMSAbstraction;
import com.github.mineGeek.ZoneReset.nms.NMSHelper;

public class Zone {

	/**
	 * Methods for triggers
	 */
	public enum 	ZRMethod { NONE, MANUAL, TIMED, ONJOIN, ONQUIT, ONINTERACT}
	public enum		ZRAREA { NONE, CUBOID, WORLD, SERVER }
	
	/**
	 * The case sensitive string rep for the zone
	 */
	private String 	tag;
	
	/**
	 * The world this zone exists in
	 */
	private String 	worldName;
	
	/**
	 * Last time a reset was done... what method was used
	 */
	public ZRMethod lastRestMethod = ZRMethod.NONE;
	
	/**
	 * Time stamps
	 */
	public Long 	lastReset;
	public Long 	lastTimedReset;
	public Long 	nextTimedReset;
	
	/**
	 * Area that this zone covers
	 */
	private Area 	area = new Area();
	
	/**
	 * Scope for rule. Only reset blocks if
	 * scope is cuboid?
	 */
	private ZRAREA	scope = ZRAREA.NONE;
	
	/**
	 * List of shit to spawn on reset, categorised by type
	 */
	private Map< ZRSPAWNTYPE, List<SpawnInterface>> spawns = new HashMap< ZRSPAWNTYPE, List<SpawnInterface>>();
	
	/**
	 * Require: zone to not have players
	 */
	private boolean requireNoPlayers = false;
	
	/**
	 * Pre: noSpawns = remove spawn points before reset
	 */
	private boolean preNoSpawns = false;
	
	/**
	 * Pre: setSpawns = set new spawn point
	 */
	private String 	preNewSpawnWorldName;
	private int 	preNewSpawnX;
	private int 	preNewSpawnY;
	private int 	preNewSpawnZ;
	
	/**
	 * Pre: newLocation = move players to position
	 */
	private String 	preNewLocation;
	private int 	preNewLocationX;
	private int 	preNewLocationY;
	private int 	preNewLocationZ;
	
	/**
	 * Pre: noMobs = remove entities ( can't remove peeps)
	 */
	private boolean preNoMobs = false;
	private List<EntityType> preNoMobsExceptionList = new ArrayList<EntityType>(); 
	

	/**
	 * Triggers when player joins. trigOnPlayerJoin (true ) = any player joining
	 * otherwise it sees if the player is in the list
	 * trigOnFirstPlayerJoin = won't trigger if others are on
	 */
	private boolean trigOnPlayerJoin = false;
	private boolean trigOnFirstPlayerJoin = false;
	private List<String> trigOnPlayerJoinList = new ArrayList<String>();
	
	/**
	 * Trigger when a player leaves
	 */
	private boolean onPlayerQuit = false;
	private List<String> onPlayerQuitList = new ArrayList<String>();
	private Long onMinutes = (long) 0;
	private String onMinutesFormat = null;
	private int onInteractMaterialId = 0;
	private Location onInteractLocation = null;
	private String onInteractLocationWorld;
	private int onInteractLocationX;
	private int onInteractLocationY;
	private int onInteractLocationZ;
	
	
	public Zone() {}
	
	public Zone( Zone clone, String newTag ) {
		
		this.tag = newTag;
		this.area = clone.area;
		this.preNoMobs = clone.preNoMobs;
		this.preNoMobsExceptionList = clone.preNoMobsExceptionList;
		this.onInteractMaterialId = clone.onInteractMaterialId;
		this.onInteractLocation = clone.onInteractLocation;
		this.onInteractLocationWorld = clone.onInteractLocationWorld;
		this.onInteractLocationX = clone.onInteractLocationX;
		this.onInteractLocationY = clone.onInteractLocationY;
		this.onInteractLocationZ = clone.onInteractLocationZ;
		this.onMinutesFormat = clone.onMinutesFormat;
		this.onMinutes = clone.onMinutes;
		this.trigOnPlayerJoin = clone.trigOnPlayerJoin;
		this.trigOnPlayerJoinList = clone.trigOnPlayerJoinList;
		this.onPlayerQuit = clone.onPlayerQuit;
		this.onPlayerQuitList = clone.onPlayerQuitList;
		this.preNoSpawns = clone.preNoSpawns;
		this.requireNoPlayers = clone.requireNoPlayers;
		this.preNewSpawnWorldName = clone.preNewSpawnWorldName;
		this.preNewSpawnX = clone.preNewSpawnX;
		this.preNewSpawnY = clone.preNewSpawnY;
		this.preNewSpawnZ = clone.preNewSpawnZ;
		this.preNewLocation = clone.preNewLocation;
		this.preNewLocationX = clone.preNewLocationX;
		this.preNewLocationY = clone.preNewLocationY;
		this.preNewLocationZ = clone.preNewLocationZ;
		this.worldName = clone.worldName;
		this.lastRestMethod = clone.lastRestMethod;
		this.lastReset = clone.lastReset;
		this.lastTimedReset = clone.lastTimedReset;
		this.nextTimedReset = clone.nextTimedReset;
		
		
	}
	
	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}


	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}


	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}


	/**
	 * @param worldName the worldName to set
	 */
	public void setWorldName(String worldName) {
		this.worldName = worldName;
		this.getArea().worldName = worldName;
	}


	/**
	 * @return the area
	 */
	public Area getArea() {
		
		if ( area.worldName == null )  area.worldName = this.worldName;
		
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}


	/**
	 * @return the requireNoPlayers
	 */
	public boolean isRequireNoPlayers() {
		return requireNoPlayers;
	}




	/**
	 * @param requireNoPlayers the requireNoPlayers to set
	 */
	public void setRequireNoPlayers(boolean requireNoPlayers) {
		this.requireNoPlayers = requireNoPlayers;
	}




	/**
	 * @return the removeSpawnPoints
	 */
	public boolean isRemoveSpawnPoints() {
		return preNoSpawns;
	}


	public ZRMethod getLastResetMethod() {
		return this.lastRestMethod;
	}
	
	public void setLastResetMethod( ZRMethod method ) {
		this.lastRestMethod = method;
	}

	public Long getLastReset() {
		return this.lastReset;
	}
	
	public void setLastReset( Long value ) {
		this.lastReset = value;
	}
	
	public Long getLastTimedReset() {
		return this.lastTimedReset;
	}
	
	public void setLastTimedReset( Long value ) {
		this.lastTimedReset = value;
	}
	
	public Long getNextTimedReset() {
		return this.nextTimedReset;
	}
	
	public void setNextTimedRest( Long value ) {
		this.nextTimedReset = value;
	}
	
	/**
	 * @param removeSpawnPoints the removeSpawnPoints to set
	 */
	public void setRemoveSpawnPoints(boolean removeSpawnPoints) {
		this.preNoSpawns = removeSpawnPoints;
	}

	/**
	 * @return the resetSpawnPoints
	 */
	public Location getResetSpawnLocation() {
		
		Location r = null;
		
		if ( this.preNewSpawnWorldName != null ) {
			World world = Bukkit.getWorld( this.preNewSpawnWorldName );
			
			if ( world != null ) {
				r = new Location( world, this.preNewSpawnX, this.preNewSpawnY, this.preNewSpawnZ );
			}
		}
		
		return r;
		
	}
	

	/**
	 * @param resetSpawnPoints the resetSpawnPoints to set
	 */
	public void setResetSpawnPoints(String worldName, int x, int y, int z ) {

		this.preNewSpawnWorldName = worldName;
		this.preNewSpawnX = x;
		this.preNewSpawnY = y;
		this.preNewSpawnZ = z;
		
	}

	/**
	 * @return the transportPlayers
	 */
	public Location getTransportPlayers() {
		Location r = null;
		
		if ( this.preNewLocation != null ) {
			World world = Bukkit.getWorld( this.preNewLocation );
			
			if ( world != null ) {
				r = new Location( world, this.preNewLocationX, this.preNewLocationY, this.preNewLocationZ );
			}
		}
		
		return r;
	}

	/**
	 * @param transportPlayers the transportPlayers to set
	 */
	public void setTransportPlayers( String worldName, int x, int y, int z) {
		this.preNewLocation = worldName;
		this.preNewLocationX = x;
		this.preNewLocationY = y;
		this.preNewLocationZ = z;
	}
	
	public Map<ZRSPAWNTYPE, List<SpawnInterface>> getSpawns() {
		return this.spawns;
	}
	
	public void setSpawns( Map<ZRSPAWNTYPE, List<SpawnInterface>> list ) {
		this.spawns = list;
	}
	
	public void clearSpawnList() {
		this.spawns.clear();
	}
	
	public void setSpawns( ZRSPAWNTYPE z, List<SpawnInterface> list ) {
		
		this.spawns.put( z, list );

	}
	
	public void addSpawn( ZRSPAWNTYPE z, SpawnInterface s ) {
		
		if ( this.spawns.containsKey( z ) ) {
			this.spawns.get(z).add( s );
		} else {
			
			List<SpawnInterface> list = new ArrayList<SpawnInterface>();
			list.add( s );
			this.spawns.put( z, list );
			
		}
	}
	
	/**
	 * @return the killEntities
	 */
	public boolean isKillEntities() {
		return preNoMobs;
	}

	/**
	 * @param killEntities the killEntities to set
	 */
	public void setKillEntities(boolean killEntities) {
		this.preNoMobs = killEntities;
	}

	/**
	 * @return the killEntityExceptions
	 */
	public List<EntityType> getKillEntityExceptions() {
		return preNoMobsExceptionList;
	}


	/**
	 * @return the onPlayerJoin
	 */
	public boolean isOnPlayerJoin() {
		return trigOnPlayerJoin;
	}




	/**
	 * @param onPlayerJoin the onPlayerJoin to set
	 */
	public void setOnPlayerJoin(boolean onPlayerJoin) {
		this.trigOnPlayerJoin = onPlayerJoin;
	}




	/**
	 * @return the onPlayerJoinList
	 */
	public List<String> getOnPlayerJoinList() {
		return trigOnPlayerJoinList;
	}




	/**
	 * @param onPlayerJoinList the onPlayerJoinList to set
	 */
	public void setOnPlayerJoinList(List<String> onPlayerJoinList) {
		this.trigOnPlayerJoinList = onPlayerJoinList;
	}




	/**
	 * @return the onPlayerQuit
	 */
	public boolean isOnPlayerQuit() {
		return onPlayerQuit;
	}




	/**
	 * @param onPlayerQuit the onPlayerQuit to set
	 */
	public void setOnPlayerQuit(boolean onPlayerQuit) {
		this.onPlayerQuit = onPlayerQuit;
	}




	/**
	 * @return the onPlayerQuitList
	 */
	public List<String> getOnPlayerQuitList() {
		return onPlayerQuitList;
	}




	/**
	 * @param onPlayerQuitList the onPlayerQuitList to set
	 */
	public void setOnPlayerQuitList(List<String> onPlayerQuitList) {
		this.onPlayerQuitList = onPlayerQuitList;
	}



	public String getOnMinutesFormat() {
		return this.onMinutesFormat;
	}
	
	public void setOnMinutesFormat( String value ) {
		
		this.onMinutesFormat = value;
		
		if ( value == null ) return;
		
		try {
			Matcher match = Pattern.compile("(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?").matcher( value );
			Long secs = (long) 0;	
			
			if ( match.find() ) {
			
				if ( match.group(1) != null ) secs = 60*60*24 * Long.parseLong( match.group(1) );
				if ( match.group(2) != null ) secs += 60*60* Long.parseLong( match.group(2) );
				if ( match.group(3) != null ) secs += 60* Long.parseLong( match.group(3) );
				if ( match.group(4) != null ) secs += Long.parseLong( match.group(4) );
				
			}
			
			this.setOnMinutes( secs );
		} catch ( Exception e ) {}
		
	}
	

	/**
	 * @return the onMinutes
	 */
	public Long getOnMinutes() {
		return onMinutes;
	}




	/**
	 * @param onMinutes the onMinutes to set
	 */
	public void setOnMinutes(Long onMinutes) {
		this.onMinutes = onMinutes;
	}


	public int getOnInteractMaterialId() {
		return this.onInteractMaterialId;
	}

	public void setOnInteractMaterialId( int value ) {
		this.onInteractMaterialId = value;
	}

	/**
	 * @return the onInteractLocation
	 */
	public Location getOnInteractLocation() {
		
		if ( this.onInteractLocation == null && this.onInteractLocationWorld != null ) {
			
			World world = Bukkit.getWorld( this.onInteractLocationWorld );
			
			if ( world != null ) {
				this.onInteractLocation = new Location( world, this.onInteractLocationX, this.onInteractLocationY, this.onInteractLocationZ );
			}
		}
		
		return onInteractLocation;
	}




	/**
	 * @param onInteractLocation the onInteractLocation to set
	 */
	public void setOnInteractLocation( String worldName, int x, int y, int z ) {

		this.onInteractLocationWorld = worldName;
		this.onInteractLocationX = x;
		this.onInteractLocationY = y;
		this.onInteractLocationZ = z;
		
	}


	
	public boolean restore() {
		
		/**
		 * Reset spawn points
		 */
		Location l = this.getResetSpawnLocation();
		if ( l != null ) {

			Utilities.resetZoneSpawnPoints( this );
			
		}
		
		/**
		 * Move players out of zone
		 */
		l = this.getTransportPlayers();
		if ( l != null ) {
			
			Utilities.movePlayersInZone( this );
			
		}
		
		
		
		if ( requireNoPlayers && Utilities.zoneHasPlayers( this )  ) {

			return false;
			
		}
		
		if ( preNoMobs ) Utilities.clearZoneOfEntities( this );
		
		
		this.loadBlocks();
		
		Utilities.spawnEntities( this.getSpawns() );
		
		
		return true;
		
	}
	
	
	public boolean isInteracting( Location l ) {
		
		return this.onInteractLocation.getBlock().equals( l.getBlock() );
		
	}
	
	public boolean isPlayerJoin( String playerName ) {
		
		if ( this.trigOnPlayerJoin ) {
			
			if ( this.trigOnPlayerJoinList.isEmpty() ) return true;			
			return this.trigOnPlayerJoinList.contains( playerName );
			
		}
		
		return false;
		
	}
	
	public boolean isPlayerQuit( String playerName ) {
		
		if ( this.onPlayerQuit ) {
			
			if ( this.onPlayerQuitList.isEmpty() ) return true;			
			return this.onPlayerQuitList.contains( playerName );
			
		}
		
		return false;		
		
	}
	

	
	public void setKillEntityExceptions( List<String> list ) {
		
		this.preNoMobsExceptionList.clear();
		
		if ( list != null && list.size() > 0 ) {
			
			for ( String x : list ) {
				this.preNoMobsExceptionList.add( EntityType.fromName( x ) );
			}
			
		}
		
	}
	
	
	public void restoreBlocks( ZRBlocks z ) {
		
		try {
			NMSAbstraction nms = NMSHelper.init( Bukkit.getPluginManager().getPlugin("ZoneReset") );
			
			if ( nms == null ) {
				//bah./ use bukkit
			} else {
				
				List<ZRBlock> b = z.getBlocks();
				World w = Bukkit.getWorld( this.worldName );
				MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater( w );
				if ( !b.isEmpty() ) {
					
					for ( ZRBlock zb : b ) {
						mbu.setBlock(zb.x, zb.y, zb.z, zb.materialId, zb.data);
						if ( zb.materialId == Material.CHEST.getId() ) {
							w.getBlockAt( zb.x, zb.y, zb.z).getState().update( true );
						}
					}
					
					mbu.notifyClients();
					
				}
				
			}			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		

		
	}
	
	public boolean loadBlocks() {
				
		try {
			FileInputStream fileIn = new FileInputStream( Config.folderSnapshots + File.separator + this.getTag() + ".ser" );
			ObjectInputStream in = new ObjectInputStream( fileIn );
			ZRBlocks z = ( ZRBlocks ) in.readObject();
			in.close();
			fileIn.close();
			this.restoreBlocks(z);
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		return true;
		
	}
	
	public boolean saveBlocks() {
		
        FileOutputStream fileOut;
        ZRBlocks z = this.area.getBlocks();
        
		try {

			fileOut = new FileOutputStream( Config.folderSnapshots + File.separator + this.tag + ".ser");
	        ObjectOutputStream out =  new ObjectOutputStream(fileOut);
	        out.writeObject( z );
	        out.close();
	        fileOut.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	public void close() {
		this.preNoMobsExceptionList.clear();
		this.trigOnPlayerJoinList.clear();
		this.onPlayerQuitList.clear();
		this.onInteractLocation = null;
	}	
	
}
