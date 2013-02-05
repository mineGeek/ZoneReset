package com.github.mineGeek.ZoneReset;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.ZoneReset.Commands.Reset;
import com.github.mineGeek.ZoneReset.Utilities.RestoreWESnapshot;

public class ZoneReset extends JavaPlugin {

	
    @Override
    public void onDisable() {

        getLogger().info( this.getName() + " disabled." );
    }	
    
    @Override
    public void onEnable() {
    	

    	getCommand("restoreit").setExecutor( new Reset( this ) );
    	getLogger().info( this.getName() + " enabled" );
    	
    	
    	
    }
     
    public Boolean restoreSnapShot( Player player, Location ne, Location sw ) {
    	
		RestoreWESnapshot restore = new RestoreWESnapshot( player, player.getWorld());
		return restore.restore( ne, sw );
    	
    }
    
	
}
