package com.github.mineGeek.ZoneReset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.ZoneReset.Commands.Reset;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.RestoreWESnapshot;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zones;

public class ZoneReset extends JavaPlugin {

	
    @Override
    public void onDisable() {

        getLogger().info( this.getName() + " disabled." );
    }	
    
    @Override
    public void onEnable() {
    	

    	getCommand("reset").setExecutor( new Reset( this ) );
    	Config.loadConfig();
    	getLogger().info( this.getName() + " enabled loaded " + Zones.count() + " rules total.");
    	
    	
    	
    }
     
    public Boolean restoreSnapShot( Player player, Zone zone ) {
    	
		return RestoreWESnapshot.restore( player, zone );
    	
    }
    
	
}
