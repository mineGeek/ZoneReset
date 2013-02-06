package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;



public class Zone {

	
	private String 	tag;
	private String 	worldName;
	
	private Area 	area;
	
	private boolean requireNoPlayers = false;
	private boolean removeSpawnPoints = false;
	
	private Location resetSpawnPoints;
	private Location transportPlayers;
	
	private boolean killEntities = false;
	private List<EntityType> killEntityExceptions = new ArrayList<EntityType>(); 
	private List<EntityLocation> spawnEntities = new ArrayList<EntityLocation>();
	
	private String snapShotName = null;
	private Map< String, String > schedule = new HashMap<String, String>();
	private boolean onPlayerJoin = false;
	private List<String> onPlayerJoinList = new ArrayList<String>();
	private boolean onPlayerQuit = false;
	private List<String> onPlayerQuitList = new ArrayList<String>();
	private int onMinutes = 0;
	private Location onInteractLocation = null;
	
	

	
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
	public Location getResetSpawnPoints() {
		return resetSpawnPoints;
	}

	/**
	 * @param resetSpawnPoints the resetSpawnPoints to set
	 */
	public void setResetSpawnPoints(Location resetSpawnPoints) {
		this.resetSpawnPoints = resetSpawnPoints;
	}

	/**
	 * @return the transportPlayers
	 */
	public Location getTransportPlayers() {
		return transportPlayers;
	}

	/**
	 * @param transportPlayers the transportPlayers to set
	 */
	public void setTransportPlayers(Location transportPlayers) {
		this.transportPlayers = transportPlayers;
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
	public List<EntityLocation> getSpawnEntities() {
		return spawnEntities;
	}

	/**
	 * @param spawnEntities the spawnEntities to set
	 */
	public void setSpawnEntities(List<EntityLocation> spawnEntities) {
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




	/**
	 * @return the onMinutes
	 */
	public int getOnMinutes() {
		return onMinutes;
	}




	/**
	 * @param onMinutes the onMinutes to set
	 */
	public void setOnMinutes(int onMinutes) {
		this.onMinutes = onMinutes;
	}




	/**
	 * @return the onInteractLocation
	 */
	public Location getOnInteractLocation() {
		return onInteractLocation;
	}




	/**
	 * @param onInteractLocation the onInteractLocation to set
	 */
	public void setOnInteractLocation(Location onInteractLocation) {
		this.onInteractLocation = onInteractLocation;
	}


	
	public boolean restore() {
		
		/**
		 * Reset spawn points
		 */
		if ( this.getResetSpawnPoints() != null ) {

			Utilities.resetZoneSpawnPoints( this );
			
		}
		
		/**
		 * Move players out of zone
		 */
		if ( this.getTransportPlayers() != null ) {
			
			Utilities.movePlayersInZone( this );
			
		}
		
		
		
		if ( requireNoPlayers && Utilities.zoneHasPlayers( this )  ) {

			return false;
			
		}
		
		if ( killEntities ) Utilities.clearZoneOfEntities( this );
		
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
	
	public void close() {
		this.killEntityExceptions.clear();
		this.spawnEntities.clear();
		this.schedule.clear();
		this.onPlayerJoinList.clear();
		this.onPlayerQuitList.clear();
		this.onInteractLocation = null;
	}	
	
}
