package com.github.mineGeek.ZoneReset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ZoneReset.Commands.Reset;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.RestoreWESnapshot;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zones;

public class ZoneReset extends JavaPlugin {

	private List<BukkitTask> tasks = new ArrayList<BukkitTask>();
	
    @Override
    public void onDisable() {

    	for ( BukkitTask task : this.tasks ) {
    		task.cancel();
    	}
    	
    	Zones.close();
    	
        getLogger().info( this.getName() + " disabled." );
    }	
    
    @Override
    public void onEnable() {
    	
		this.saveDefaultConfig();
		
    	getCommand("reset").setExecutor( new Reset( this ) );
    	Config.snapShotFolder = this.getDataFolder() + File.separator + "snapshots";
    	
    	File file = new File( Config.snapShotFolder );
    	
    	if ( !file.isDirectory() ) {
    		try {
    			file.mkdir();
    		} catch (Exception e ) {
    			this.getLogger().info("Failed making plugins/ZoneReset/snapshots");
    		}
    	}
    	
    	//Config.loadConfig();
    	getLogger().info( this.getName() + " enabled loaded " + Zones.count() + " rules total.");
    	
    	
    	
    }
    
    public void queueResets() {
    	
    	Map<String, Zone > zones = Zones.getZones();
    	this.tasks.clear();
    	
    	for ( Zone z : zones.values() ) {
    		
    		if ( z.getOnMinutes() > 0 ) {
    			
    			BukkitTask task = this.getServer().getScheduler().runTaskTimerAsynchronously( this, new Runnable() {
    	    	    @Override  
    	    	    public void run() {
    	    	    	try {
    	    	    		
    	    	    	} catch (Exception e ) {}
    	    	    }
    	    	}, z.getOnMinutes() * 20 * 60 , z.getOnMinutes() * 20 * 60 );
    			
    			this.tasks.add( task );
    			
    		}
    		
    	}
    	
    }
    
    public Boolean restoreSnapShot( Player player, Zone zone ) {
    	
		return RestoreWESnapshot.restore( player, zone );
    	
    }
    
	
}
