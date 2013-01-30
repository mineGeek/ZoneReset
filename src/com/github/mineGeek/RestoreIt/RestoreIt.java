package com.github.mineGeek.RestoreIt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.RestoreIt.Commands.Restore;
import com.github.mineGeek.RestoreIt.Utilities.RestoreWESnapshot;

public class RestoreIt extends JavaPlugin {

	
    @Override
    public void onDisable() {

        getLogger().info( this.getName() + " disabled." );
    }	
    
    @Override
    public void onEnable() {
    	

    	getCommand("restoreit").setExecutor( new Restore( this ) );
    	getLogger().info( this.getName() + " enabled" );
    	
    	
    	
    }
     
    public Boolean restoreSnapShot( Player player, Location ne, Location sw ) {
    	
		RestoreWESnapshot restore = new RestoreWESnapshot( player, player.getWorld());
		return restore.restore( ne, sw );
    	
    }
    
	
}
