package com.github.mineGeek.RestoreIt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.mineGeek.RestoreIt.Commands.Restore;
import com.github.mineGeek.RestoreIt.Utilities.RestoreWESnapshot;

public class RestoreIt extends JavaPlugin {

	
    @Override
    public void onDisable() {
    	//this.config.saveConfig();
        getLogger().info( this.getName() + " disabled." );
    }	
    
    @Override
    public void onEnable() {
    	
    	//getCommand("lrcan").setExecutor( ra );
    	getCommand("restoreit").setExecutor( new Restore( this ) );
    	getLogger().info( this.getName() + " enabled" );
    	
    	
    	
    }
     
    public Boolean restoreSnapShot( Player player, Location ne, Location sw ) {
    	
		RestoreWESnapshot restore = new RestoreWESnapshot( player, player.getWorld());
		return restore.restore( ne, sw );
    	
    }
    

	
	/*
	// create a terrain manager object
	TerrainManager tm = new TerrainManager(wep, player);
	// OR - without needing an associated Player
	TerrainManager tm = new TerrainManager(wep, world);
	 
	// don't include an extension - TerrainManager will auto-add ".schematic"
	File saveFile = new File(plugin.getDataFolder(), "backup1");
	 
	// save the terrain to a schematic file
	try {
	  tm.saveTerrain(saveFile, l1, l2);
	} catch (FilenameException e) {
	  // thrown by WorldEdit - it doesn't like the file name/location etc.
	} catch (DataException e) {
	  // thrown by WorldEdit - problem with the data
	} catch (IOException e) {
	  // problem with creating/writing to the file
	}
	 
	// reload a schematic
	try {
	  // reload at the given location
	  Location location = new Location(x, y, z);
	  tm.loadSchematic(saveFile, location);
	  // OR
	  // reload at the same place it was saved
	  tm.loadSchematic(saveFile);
	} catch (FilenameException e) {
	  // thrown by WorldEdit - it doesn't like the file name/location etc.
	} catch (DataException e) {
	  // thrown by WorldEdit - problem with the data
	} catch (IOException e) {
	  // problem with opening/reading the file
	} catch (MaxChangedBlocksException e) {
	  // thrown by WorldEdit - the schematic is larger than the configured block limit for the player
	} catch (EmptyClipboardException e) {
	// thrown by WorldEdit - should be self-explanatory
	}	
	*/
	
}
