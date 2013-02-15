package com.github.mineGeek.ZoneReset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ZoneReset.Commands.Cancel;
import com.github.mineGeek.ZoneReset.Commands.Create;
import com.github.mineGeek.ZoneReset.Commands.Edit;
import com.github.mineGeek.ZoneReset.Commands.Reset;
import com.github.mineGeek.ZoneReset.Commands.Save;
import com.github.mineGeek.ZoneReset.Commands.Set;
import com.github.mineGeek.ZoneReset.Events.Listeners;
import com.github.mineGeek.ZoneReset.Player.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zone.ZRMethod;
import com.github.mineGeek.ZoneReset.Utilities.Zones;

/**
 * Main
 * @author Moí
 *
 */
public class ZoneReset extends JavaPlugin {

	/**
	 * List of tasks that control automatic resets.
	 */
	private List<BukkitTask> tasks = new ArrayList<BukkitTask>();
	
	/**
	 * Process shut down or disable
	 */
    @Override
    public void onDisable() {

    	
    	/**
    	 * Empty task queue
    	 */
    	if ( !this.tasks.isEmpty() ) {
	    	for ( BukkitTask task : this.tasks ) {
	    		task.cancel();
	    	}
	    	this.tasks.clear();
    	}
    	
    	/**
    	 * Shut down and dispose of any markers currently set
    	 */
    	Markers.close();
    	
    	/**
    	 * Dispose of any related player data (for the hell of it)
    	 */
    	if ( this.getServer().getOnlinePlayers().length > 0 ) {
    	
	    	for ( Player p : getServer().getOnlinePlayers() ) {
	    		Utilities.clearPlayerMetaData(p);
	    	}
    	
    	}
    	
    	/**
    	 * Shut down and dispose of Zones
    	 */
    	Zones.close();
    	
    	/**
    	 * Tell the console that it won.
    	 */
        getLogger().info( this.getName() + " disabled." );
        
    }	
    
    
    /**
     * Get this party started
     */
    @Override
    public void onEnable() {
    	
    	/**
    	 * If config.yml went awol, get a new one.
    	 */
		this.saveDefaultConfig();
		
		
		/**
		 * Commands
		 */
    	getCommand("reset").setExecutor( new Reset( this ) );
    	getCommand("save").setExecutor( new Save( this ) );
    	getCommand("set").setExecutor( new Set( this ) );
    	getCommand("cancel").setExecutor( new Cancel( this ) );
    	getCommand("create").setExecutor( new Create(this) );
    	getCommand("edit").setExecutor( new Edit( this ) );
    	
    	
    	/**
    	 * Listeners
    	 */
    	this.getServer().getPluginManager().registerEvents( new Listeners(), this);
    	
    	/**
    	 * Set up config
    	 */
    	Config.c = this.getConfig();
    	Config.folderPlugin = this.getDataFolder().toString();
    	Config.folderPlayers = this.getDataFolder().toString() + File.separator + "players";
    	Config.folderSnapshots = this.getDataFolder().toString() + File.separator + "snapshots";
    	
    	/**
    	 * Zone serialize directory
    	 */
    	File file = new File( Config.folderSnapshots );
    	
    	if ( !file.isDirectory() ) {
    		try {
    			file.mkdir();
    		} catch (Exception e ) {
    			this.getLogger().info("Failed making plugins/ZoneReset/snapshots");
    		}
    	}
    	
    	/**
    	 * Load config.yml
    	 */
    	Config.loadConfig();
    	
    	/**
    	 * Load zone saved data
    	 */
    	Zones.setDataFolder( this.getDataFolder().toString() );
    	Zones.loadDataZones();
    	this.queueResets();
    	
    	/**
    	 * Force loads the class for shits and giggles.
    	 */
    	Markers.enabled = true;
    	
    	/**
    	 * Tell console we are ready to go.
    	 */
    	getLogger().info( this.getName() + " enabled loaded " + Zones.count() + " rules total.");
    	
    }
    
    
    /**
     * Clear reset queue
     */
    public void clearResets() {
    	
    	if ( !this.tasks.isEmpty() ) {
	    	for ( BukkitTask task : this.tasks ) {
	    		task.cancel();
	    	}
	    	this.tasks.clear();
    	}
    	
    }
    
    
    /**
     * Scans through zones and queues up tasks to 
     * run timed resets.
     */
    public void queueResets() {
    	
    	this.clearResets();
    	
    	Map<String, Zone > zones = Zones.getZones();
    	
    	for ( Zone z : zones.values() ) {
    		
    		if ( z.getTrigTimer() > 0 ) {
    			
    			Long next = z.getNextTimedReset();
    			
    			if ( next != null && next != 0 ) {
    				
    				if ( next < System.currentTimeMillis() ) {
    					//overdue. Run now.
    					next = 0L;
    				} else {
    					next = next - System.currentTimeMillis();
    				}
    				
    			}
    			
    			final Long nextRun = next * 20 + 1;
    			final Long repeatRun = z.getTrigTimer() * 20;
    			final String tag = z.getTag();
    			
    			BukkitTask task = this.getServer().getScheduler().runTaskTimer( this, new Runnable() {
    	    	    @Override  
    	    	    public void run() {
    	    	    	try {
    	    	    		Zones.getZone(tag).reset( ZRMethod.TIMED );
    	    	    		Zones.getZone(tag).setNextTimedRest( Zones.getZone(tag).getTrigTimer() + System.currentTimeMillis() );
    	    	    	} catch (Exception e ) {}
    	    	    }
    	    	}, nextRun , repeatRun );
    			
    			this.tasks.add( task );
    			
    		}
    		
    	}
    	
    }
}
