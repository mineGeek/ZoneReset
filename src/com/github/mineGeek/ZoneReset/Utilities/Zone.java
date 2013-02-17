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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.mineGeek.ZoneReset.Data.Area;
import com.github.mineGeek.ZoneReset.Data.ItemSerializable;
import com.github.mineGeek.ZoneReset.Data.MobSerializable;
import com.github.mineGeek.ZoneReset.Data.ZRBlock;
import com.github.mineGeek.ZoneReset.Data.ZRBlocks;
import com.github.mineGeek.ZoneReset.Data.ZRItem;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface;
import com.github.mineGeek.ZoneReset.Spawners.SpawnInterface.ZRSPAWNTYPE;
import com.github.mineGeek.ZoneReset.nms.NMSAbstraction;
import com.github.mineGeek.ZoneReset.nms.NMSHelper;
import com.github.mineGeek.ZoneReset.Messaging.Message;


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
	 * false if user is editing or such.
	 */
	private boolean enabled = true;
	
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
	private boolean 	trigOnPlayerJoin = false;
	private boolean 	trigOnFirstPlayerJoin = false;
	private List<String>trigOnPlayerJoinList = new ArrayList<String>();
	
	/**
	 * Trigger when a player leaves
	 */
	private boolean 	trigOnPlayerQuit = false;
	private boolean 	trigOnLastPlayerQuit = false;
	private List<String>trigOnPlayerQuitList = new ArrayList<String>();
	
	/**
	 * Trigger on a timer
	 */
	private Long 	trigTimer = (long) 0;
	private String 	trigTimerText = null;
	
	/**
	 * Trigger on interact
	 */
	private int 	onInteractMaterialId = 0;
	private Location onInteractLocation = null;
	private String 	onInteractLocationWorld;
	private int onInteractLocationX;
	private int onInteractLocationY;
	private int onInteractLocationZ;
	
	
	private String messageOnRestart = null;
	private List< Message > timedMessages = new ArrayList< Message >(); 
	
	/**
	 * Plain old constructor
	 */
	public Zone() {}
	
	
	/**
	 * Constructor clone
	 * @param clone
	 * @param newTag
	 */
	public Zone( Zone clone, String newTag ) {
		
		this.tag 					= newTag;

		this.enabled				= clone.enabled;
		this.worldName				= clone.worldName;
		
		this.scope					= clone.scope;
		
		this.area 					= clone.area;
		
		this.lastRestMethod 		= clone.lastRestMethod;
		this.lastReset 				= clone.lastReset;
		this.lastTimedReset 		= clone.lastTimedReset;
		this.nextTimedReset 		= clone.nextTimedReset;		
		
		this.spawns					= clone.spawns;
	
		this.requireNoPlayers 		= clone.requireNoPlayers;
		this.preNoSpawns 			= clone.preNoSpawns;		
	
		this.preNewSpawnWorldName 	= clone.preNewSpawnWorldName;
		this.preNewSpawnX 			= clone.preNewSpawnX;
		this.preNewSpawnY 			= clone.preNewSpawnY;
		this.preNewSpawnZ 			= clone.preNewSpawnZ;
		
		this.preNewLocation 		= clone.preNewLocation;
		this.preNewLocationX 		= clone.preNewLocationX;
		this.preNewLocationY 		= clone.preNewLocationY;
		this.preNewLocationZ 		= clone.preNewLocationZ;		
		
		this.onInteractMaterialId 	= clone.onInteractMaterialId;
		this.onInteractLocation 	= clone.onInteractLocation;
		this.onInteractLocationWorld= clone.onInteractLocationWorld;
		this.onInteractLocationX 	= clone.onInteractLocationX;
		this.onInteractLocationY 	= clone.onInteractLocationY;
		this.onInteractLocationZ 	= clone.onInteractLocationZ;		
		
		this.preNoMobs 				= clone.preNoMobs;
		this.preNoMobsExceptionList = clone.preNoMobsExceptionList;
		
		this.trigOnPlayerJoin 		= clone.trigOnPlayerJoin;
		this.trigOnFirstPlayerJoin	= clone.trigOnFirstPlayerJoin;
		this.trigOnPlayerJoinList 	= clone.trigOnPlayerJoinList;
		
		this.trigOnPlayerQuit 		= clone.trigOnPlayerQuit;
		this.trigOnLastPlayerQuit	= clone.trigOnLastPlayerQuit;
		this.trigOnPlayerQuitList 	= clone.trigOnPlayerQuitList;
		
		this.trigTimer 				= clone.trigTimer;
		this.trigTimerText 			= clone.trigTimerText;
		
		this.messageOnRestart		= clone.messageOnRestart;
		this.timedMessages			= clone.timedMessages;
		
		
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


	public boolean getEnabled() {
		return this.enabled;
	}
	
	public void setEnabled( boolean value ) {
		this.enabled = value;
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
	public boolean isPreNoSpawns() {
		return preNoSpawns;
	}

	/**
	 * Retirn last Reset method used
	 * @return
	 */
	public ZRMethod getLastResetMethod() {
		return this.lastRestMethod;
	}
	
	/**
	 * Set the last Reset method
	 * @param method
	 */
	public void setLastResetMethod( ZRMethod method ) {
		this.lastRestMethod = method;
	}

	/**
	 * @return the last reset time
	 */
	public Long getLastReset() {
		return this.lastReset;
	}
	
	/**
	 * @param Long - sets the lastReset time
	 */
	public void setLastReset( Long value ) {
		this.lastReset = value;
	}
	
	/**
	 * @return Long the last time reset occurred via a timed method
	 */
	public Long getLastTimedReset() {
		return this.lastTimedReset;
	}
	
	/**
	 * Set the last time reset occurred from a timed method 
	 * @param value
	 */
	public void setLastTimedReset( Long value ) {
		this.lastTimedReset = value;
	}
	
	public Long getNextTimedReset() {
		
		if ( this.nextTimedReset == null ) {
			this.setNextTimedRest( this.getTrigTimer() );
		}
		return this.nextTimedReset;
	}
	
	public void setNextTimedRest( Long value ) {
		this.nextTimedReset = value;
	}
	
	/**
	 * @param noSpawns the removeSpawnPoints to set
	 */
	public void setPreNoSpawns(boolean noSpawns) {
		this.preNoSpawns = noSpawns;
	}

	/**
	 * @return the resetSpawnPoints
	 */
	public Location getPreSpawnLocation() {
		
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
	public void setPreSpawnLocation(String worldName, int x, int y, int z ) {

		this.preNewSpawnWorldName = worldName;
		this.preNewSpawnX = x;
		this.preNewSpawnY = y;
		this.preNewSpawnZ = z;
		
	}

	/**
	 * @return the transportPlayers
	 */
	public Location getPreNewLocation() {
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
	public void setPreNewLocation( String worldName, int x, int y, int z) {
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
	public boolean isPreNoMobs() {
		return preNoMobs;
	}

	/**
	 * @param preNoMobs the killEntities to set
	 */
	public void setPreNoMobs(boolean preNoMobs) {
		this.preNoMobs = preNoMobs;
	}

	/**
	 * @return the killEntityExceptions
	 */
	public List<EntityType> getPreNoMobsExceptionList() {
		return preNoMobsExceptionList;
	}


	/**
	 * @return the onPlayerJoin
	 */
	public boolean isTrigOnPlayerJoin() {
		return trigOnPlayerJoin;
	}

	/**
	 * @param onPlayerJoin the onPlayerJoin to set
	 */
	public void setTrigOnPlayerJoin(boolean onPlayerJoin) {
		this.trigOnPlayerJoin = onPlayerJoin;
	}

	/**
	 * @return the onPlayerJoinList
	 */
	public List<String> getTrigOnPlayerJoinList() {
		return trigOnPlayerJoinList;
	}

	/**
	 * @param onPlayerJoinList the onPlayerJoinList to set
	 */
	public void setTrigOnPlayerJoinList(List<String> onPlayerJoinList) {
		this.trigOnPlayerJoinList = onPlayerJoinList;
	}

	/**
	 * @return the onPlayerQuit
	 */
	public boolean isTrigOnPlayerQuit() {
		return trigOnPlayerQuit;
	}

	/**
	 * @param onPlayerQuit the onPlayerQuit to set
	 */
	public void setTrigOnPlayerQuit(boolean onPlayerQuit) {
		this.trigOnPlayerQuit = onPlayerQuit;
	}

	/**
	 * @return the onPlayerQuitList
	 */
	public List<String> getTrigOnPlayerQuitList() {
		return trigOnPlayerQuitList;
	}

	/**
	 * @param onPlayerQuitList the onPlayerQuitList to set
	 */
	public void setTrigOnPlayerQuitList(List<String> onPlayerQuitList) {
		this.trigOnPlayerQuitList = onPlayerQuitList;
	}

	public String getTrigTimerText() {
		return this.trigTimerText;
	}
	
	public void setTrigTimerText( String value ) {
		
		this.trigTimerText = value;
		
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
			
			this.setTrigTimer( secs );
		} catch ( Exception e ) {}
		
	}
	
	/**
	 * @return the timer
	 */
	public Long getTrigTimer() {
		return trigTimer;
	}

	/**
	 * @param seconds the onMinutes to set
	 */
	public void setTrigTimer(Long seconds) {
		this.trigTimer = seconds;
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

	public void setMessageOnRestart( String value ) {
		this.messageOnRestart = value;
	}
	
	public String getMessageOnResetart() {
		return this.messageOnRestart;
	}
	
	public List<Message> getTimedMessages() {
		return this.timedMessages;
	}
	
	public void setTimedMessages( List<Message> list ) {
		this.timedMessages = list;
	}
	
	public void clearTimedMessages() {
		this.timedMessages.clear();
	}
	
	public void addTimedMessages( Message m ) {
		this.timedMessages.add( m );
	}
	
	

	
	public boolean reset( ZRMethod method ) {
		
		if ( ! this.enabled ) return false;
		
		/**
		 * Reset spawn points
		 */
		Location l = this.getPreSpawnLocation();
		if ( l != null ) {

			Utilities.resetZoneSpawnPoints( this );
			
		}
		
		/**
		 * Move players out of zone
		 */
		l = this.getPreNewLocation();
		if ( l != null ) {
			
			Utilities.movePlayersInZone( this );
			
		}
		
		
		if ( requireNoPlayers && Utilities.zoneHasPlayers( this )  ) {

			return false;
			
		}
		
		if ( preNoMobs ) Utilities.clearZoneOfEntities( this );
		
		if ( this.getArea().ne() != null && this.getArea().sw() != null ) {
			this.loadBlocks();
		}
		/*
		this.setLastReset( System.currentTimeMillis() );
		this.setLastResetMethod(method);
		Bukkit.getLogger().info( this.getTag() + " reset");
		Utilities.spawnEntities( this.getSpawns() );
		
		if ( !method.equals( ZRMethod.TIMED ) ) {
			//reset timer
			Utilities.queue( this );
		}
		
		//Messages.reset( this.getTag() );
		
		*/
		return true;
		
	}
	
	
	public boolean isInteracting( Location l ) {
		
		return this.onInteractLocation.getBlock().equals( l.getBlock() );
		
	}
	
	public boolean isTrigOnPlayerJoining( String playerName ) {
		
		if ( this.trigOnPlayerJoin ) {
			
			if ( this.trigOnPlayerJoinList.isEmpty() ) return true;			
			return this.trigOnPlayerJoinList.contains( playerName );
			
		}
		
		return false;
		
	}
	
	public boolean isTrigOnPlayerQuiting( String playerName ) {
		
		if ( this.trigOnPlayerQuit ) {
			
			if ( this.trigOnPlayerQuitList.isEmpty() ) return true;			
			return this.trigOnPlayerQuitList.contains( playerName );
			
		}
		
		return false;		
		
	}
	

	
	public void setPreNoMobsExceptionList( List<String> list ) {
		
		this.preNoMobsExceptionList.clear();
		
		if ( list != null && list.size() > 0 ) {
			
			for ( String x : list ) {
				this.preNoMobsExceptionList.add( EntityType.fromName( x ) );
			}
			
		}
		
	}
	
	
	public void restoreBlocks( ZRBlocks z ) {
		
		List<ZRBlock> deferred = new ArrayList<ZRBlock>();
		List<MobSerializable> mobs = z.getMobs();
		try {
			NMSAbstraction nms = NMSHelper.init( Bukkit.getPluginManager().getPlugin("ZoneReset") );
			
			List<ZRBlock> b = z.getBlocks();
			
			World w = Bukkit.getWorld( this.worldName );			
			
			if ( nms == null || Config.noNMS ) {
				
				if ( !b.isEmpty() ) {
					
					for ( ZRBlock zb : b ) {
						
						Block block = w.getBlockAt( zb.x, zb.y, zb.z );
						block.setType( Material.getMaterial( zb.materialId ) );
						block.setData( zb.data );
						block.getState().update( true );
					}
					
				}
				
				
			} else {
				

				MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater( w );
				
				
				if ( !b.isEmpty() ) {
					boolean r = false;
					for ( ZRBlock zb : b ) {
						
						//if ( zb.deferred ) {
						//	deferred.add( zb );
						//} else {
							r = mbu.setBlock(zb.x, zb.y, zb.z, zb.materialId, zb.data);
						//}
						

						
							//String str = "setting " + Material.getMaterial( zb.materialId).toString() + " = " + ( r ? " true " : "false");
							//Bukkit.getLogger().info( str );
							
							if ( zb.hasInventory ) {
								Block block  = w.getBlockAt( zb.x, zb.y, zb.z);
								InventoryHolder ih = (InventoryHolder)block.getState();
								ih.getInventory().clear();
								
								for ( ItemSerializable i : zb.items ) {
									ih.getInventory().addItem( i.getItemStack() );
								}
							}
							
							//if ( zb.materialId == Material.WALL_SIGN.getId() ) {
							//	
							//	Block block = w.getBlockAt( zb.x, zb.y, zb.z );
							//	org.bukkit.material.Sign signData = (org.bukkit.material.Sign) block.getState().getData();
							//	BlockFace face = signData.getFacing();
							//	boolean brr = false;
							//}							
							
						
							
							
							if ( zb.lines != null ) {
								
								Block block = w.getBlockAt( zb.x, zb.y, zb.z );
								if ( block.getState() instanceof Sign ) {
									
									Sign s = (Sign)block.getState();
									
									for ( int y=0; y < zb.lines.size(); y++) {
										s.setLine(y, zb.lines.get(y) );
									}
									
									s.update( true );
								}
								
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
		

		if ( !deferred.isEmpty() ) {
			
			World w = Bukkit.getWorld( this.worldName );
			
			for ( ZRBlock zb : deferred ) {
				
				String str = "setting " + Material.getMaterial( zb.materialId).toString() + " " + zb.x + ", " + zb.y + " " + zb.z;
				Bukkit.getLogger().info( str );				
				
				Block block = w.getBlockAt( zb.x, zb.y, zb.z );
				block.setType( Material.getMaterial( zb.materialId ) );
				block.setData( zb.data );
				block.getState().update( true );				
				
			}
			
		}
		
		if ( !mobs.isEmpty() ) {
			
			for ( MobSerializable mob : mobs ) {
				
				mob.spawnEntity();
				
			}
			
		}
		
		
	}
	
	public boolean loadBlocks() {
				
		try {
			FileInputStream fileIn = new FileInputStream( Config.folderSnapshots + File.separator + this.tag + ".ser" );
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
        
        if ( z.getBlocks().size() == 0 ) {
        	z = null;
        }
        
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
		this.trigOnPlayerQuitList.clear();
		this.clearTimedMessages();
		this.onInteractLocation = null;
		this.spawns = null;
	}	
	
}
