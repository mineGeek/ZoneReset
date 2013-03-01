package com.github.mineGeek.ZoneReset;

import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.ZoneReset.Commands.Cancel;
import com.github.mineGeek.ZoneReset.Commands.Create;
import com.github.mineGeek.ZoneReset.Commands.Edit;
import com.github.mineGeek.ZoneReset.Commands.Reset;
import com.github.mineGeek.ZoneReset.Commands.Save;
import com.github.mineGeek.ZoneReset.Commands.Set;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Events.Listeners;
import com.github.mineGeek.ZoneReset.Markers.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Tracking;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

/**
 * Main
 * @author Moí
 *
 */
public class ZoneReset extends JavaPlugin {

	
	public enum ZRScope {LIST, REGION, WORLD, SERVER};


	/**
	 * Process shut down or disable
	 */
    @Override
    public void onDisable() {
   	
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
    	Tracking.close();
    	Zones.close();
    	
    	this.getServer().getScheduler().cancelTasks( this );
    	Utilities.plugin = null;
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
		
		Utilities.plugin = this;
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
    	Config.folderZones = this.getDataFolder().toString() + File.separator + "zonedata";
    	
    	/**
    	 * Zone serialise directory
    	 */
    	File file = new File( Config.folderSnapshots );
    	
    	if ( !file.isDirectory() ) {
    		try {
    			file.mkdir();
    		} catch (Exception e ) {
    			this.getLogger().info("Failed making plugins/ZoneReset/snapshots");
    		}
    	}
    	
    	file = new File( Config.folderZones );
    	
    	if ( !file.isDirectory() ) {
    		try {
    			file.mkdir();
    		} catch (Exception e ) {
    			this.getLogger().info("Failed making plugins/ZoneReset/zonedata");
    		}
    	}    	
    	
    	
    	/**
    	 * Load config.yml
    	 */
    	Config.loadConfig();
    	
    	/**
    	 * Load zone saved data
    	 */
    	Zones.loadDataZones();

    	
    	if ( Config.trackMovement ) {
    		Tracking.loadZones();
    	}
    	
    	for ( Player p : this.getServer().getOnlinePlayers() ) { Tracking.updatePlayerChunkMap( p ); }
    	
    	
    	/**
    	 * Force loads the class for shits and giggles.
    	 */
    	Markers.enabled = true;
    	
    	/**
    	 * Tell console we are ready to go.
    	 */
    	getLogger().info( this.getName() + " enabled loaded " + Zones.count() + " rules total.");
    	
    }
    
    

    
    

}
