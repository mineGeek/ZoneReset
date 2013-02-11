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
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.github.mineGeek.ZoneReset.nms.NMSAbstraction;
import com.github.mineGeek.ZoneReset.nms.NMSHelper;



public class Zone {

	public enum ZRTrigger { ZR_ONJOIN, ZR_ONQUIT, ZR_ONINTERACT };
	
	private String 	tag;
	private String 	worldName;
	
	private Area 	area = new Area();
	
	private boolean requireNoPlayers = false;
	private boolean removeSpawnPoints = false;
	
	private String resetSpawnPointsWorldName;
	private int resetSpawnPointsX;
	private int resetSpawnPointsY;
	private int resetSpawnPointsZ;
	
	private String transportPlayersWorldName;
	private int transportPlayersX;
	private int transportPlayersY;
	private int transportPlayersZ;
	
	private boolean killEntities = false;
	private List<EntityType> killEntityExceptions = new ArrayList<EntityType>(); 
	private List<SpawnInterface> spawnEntities = new ArrayList<SpawnInterface>();
	
	private String snapShotName = null;
	private Map< String, String > schedule = new HashMap<String, String>();
	private boolean onPlayerJoin = false;
	private List<String> onPlayerJoinList = new ArrayList<String>();
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
		this.killEntities = clone.killEntities;
		this.killEntityExceptions = clone.killEntityExceptions;
		this.onInteractMaterialId = clone.onInteractMaterialId;
		this.onInteractLocation = clone.onInteractLocation;
		this.onInteractLocationWorld = clone.onInteractLocationWorld;
		this.onInteractLocationX = clone.onInteractLocationX;
		this.onInteractLocationY = clone.onInteractLocationY;
		this.onInteractLocationZ = clone.onInteractLocationZ;
		this.onMinutesFormat = clone.onMinutesFormat;
		this.onMinutes = clone.onMinutes;
		this.onPlayerJoin = clone.onPlayerJoin;
		this.onPlayerJoinList = clone.onPlayerJoinList;
		this.onPlayerQuit = clone.onPlayerQuit;
		this.onPlayerQuitList = clone.onPlayerQuitList;
		this.removeSpawnPoints = clone.removeSpawnPoints;
		this.requireNoPlayers = clone.requireNoPlayers;
		this.resetSpawnPointsWorldName = clone.resetSpawnPointsWorldName;
		this.resetSpawnPointsX = clone.resetSpawnPointsX;
		this.resetSpawnPointsY = clone.resetSpawnPointsY;
		this.resetSpawnPointsZ = clone.resetSpawnPointsZ;
		this.schedule = clone.schedule;
		this.transportPlayersWorldName = clone.transportPlayersWorldName;
		this.transportPlayersX = clone.transportPlayersX;
		this.transportPlayersY = clone.transportPlayersY;
		this.transportPlayersZ = clone.transportPlayersZ;
		this.worldName = clone.worldName;
		
		
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
		return removeSpawnPoints;
	}




	/**
	 * @param removeSpawnPoints the removeSpawnPoints to set
	 */
	public void setRemoveSpawnPoints(boolean removeSpawnPoints) {
		this.removeSpawnPoints = removeSpawnPoints;
	}

	/**
	 * @return the resetSpawnPoints
	 */
	public Location getResetSpawnLocation() {
		
		Location r = null;
		
		if ( this.resetSpawnPointsWorldName != null ) {
			World world = Bukkit.getWorld( this.resetSpawnPointsWorldName );
			
			if ( world != null ) {
				r = new Location( world, this.resetSpawnPointsX, this.resetSpawnPointsY, this.resetSpawnPointsZ );
			}
		}
		
		return r;
		
	}

	/**
	 * @param resetSpawnPoints the resetSpawnPoints to set
	 */
	public void setResetSpawnPoints(String worldName, int x, int y, int z ) {

		this.resetSpawnPointsWorldName = worldName;
		this.resetSpawnPointsX = x;
		this.resetSpawnPointsY = y;
		this.resetSpawnPointsZ = z;
		
	}

	/**
	 * @return the transportPlayers
	 */
	public Location getTransportPlayers() {
		Location r = null;
		
		if ( this.transportPlayersWorldName != null ) {
			World world = Bukkit.getWorld( this.transportPlayersWorldName );
			
			if ( world != null ) {
				r = new Location( world, this.transportPlayersX, this.transportPlayersY, this.transportPlayersZ );
			}
		}
		
		return r;
	}

	/**
	 * @param transportPlayers the transportPlayers to set
	 */
	public void setTransportPlayers( String worldName, int x, int y, int z) {
		this.transportPlayersWorldName = worldName;
		this.transportPlayersX = x;
		this.transportPlayersY = y;
		this.transportPlayersZ = z;
	}

	/**
	 * @return the killEntities
	 */
	public boolean isKillEntities() {
		return killEntities;
	}

	/**
	 * @param killEntities the killEntities to set
	 */
	public void setKillEntities(boolean killEntities) {
		this.killEntities = killEntities;
	}

	/**
	 * @return the killEntityExceptions
	 */
	public List<EntityType> getKillEntityExceptions() {
		return killEntityExceptions;
	}

	/**
	 * @return the spawnEntities
	 */
	public List<SpawnInterface> getSpawnEntities() {
		return spawnEntities;
	}

	/**
	 * @param spawnEntities the spawnEntities to set
	 */
	public void setSpawnEntities(List<SpawnInterface> spawnEntities) {
		this.spawnEntities = spawnEntities;
	}


	/**
	 * @return the snapShotName
	 */
	public String getSnapShotName() {
		return snapShotName;
	}


	/**
	 * @param snapShotName the snapShotName to set
	 */
	public void setSnapShotName(String snapShotName) {
		this.snapShotName = snapShotName;
	}



	/**
	 * @return the schedule
	 */
	public Map<String, String> getSchedule() {
		return schedule;
	}




	/**
	 * @param schedule the schedule to set
	 */
	public void setSchedule(Map<String, String> schedule) {
		this.schedule = schedule;
	}




	/**
	 * @return the onPlayerJoin
	 */
	public boolean isOnPlayerJoin() {
		return onPlayerJoin;
	}




	/**
	 * @param onPlayerJoin the onPlayerJoin to set
	 */
	public void setOnPlayerJoin(boolean onPlayerJoin) {
		this.onPlayerJoin = onPlayerJoin;
	}




	/**
	 * @return the onPlayerJoinList
	 */
	public List<String> getOnPlayerJoinList() {
		return onPlayerJoinList;
	}




	/**
	 * @param onPlayerJoinList the onPlayerJoinList to set
	 */
	public void setOnPlayerJoinList(List<String> onPlayerJoinList) {
		this.onPlayerJoinList = onPlayerJoinList;
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
		
		if ( killEntities ) Utilities.clearZoneOfEntities( this );
		
		
		this.loadBlocks();
		
		Utilities.spawnEntitiesInZone( this );
		
		
		return true;
		
	}
	
	
	public boolean isInteracting( Location l ) {
		
		return this.onInteractLocation.getBlock().equals( l.getBlock() );
		
	}
	
	public boolean isPlayerJoin( String playerName ) {
		
		if ( this.onPlayerJoin ) {
			
			if ( this.onPlayerJoinList.isEmpty() ) return true;			
			return this.onPlayerJoinList.contains( playerName );
			
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
		
		this.killEntityExceptions.clear();
		
		if ( list != null && list.size() > 0 ) {
			
			for ( String x : list ) {
				this.killEntityExceptions.add( EntityType.fromName( x ) );
			}
			
		}
		
	}
	
	
	public void restoreBlocks( ZoneBlocks z ) {
		
		try {
			NMSAbstraction nms = NMSHelper.init( Bukkit.getPluginManager().getPlugin("ZoneReset") );
			
			if ( nms == null ) {
				//bah./ use bukkit
			} else {
				
				List<ZoneBlock> b = z.getBlocks();
				World w = Bukkit.getWorld( this.worldName );
				MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater( w );
				if ( !b.isEmpty() ) {
					
					for ( ZoneBlock zb : b ) {
						mbu.setBlock(zb.x, zb.y, zb.z, zb.materialId, zb.data);						
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
			FileInputStream fileIn = new FileInputStream( Config.snapShotFolder + File.separator + this.getTag() + ".ser" );
			ObjectInputStream in = new ObjectInputStream( fileIn );
			ZoneBlocks z = ( ZoneBlocks ) in.readObject();
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
        ZoneBlocks z = this.area.getBlocks();
        
		try {

			fileOut = new FileOutputStream( Config.snapShotFolder + File.separator + this.tag + ".ser");
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
		this.killEntityExceptions.clear();
		this.spawnEntities.clear();
		this.schedule.clear();
		this.onPlayerJoinList.clear();
		this.onPlayerQuitList.clear();
		this.onInteractLocation = null;
	}	
	
}
