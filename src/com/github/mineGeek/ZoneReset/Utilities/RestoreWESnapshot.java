package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.commands.SnapshotUtilCommands;
import com.sk89q.worldedit.regions.CuboidRegionSelector;

public class RestoreWESnapshot {

	
	public static Boolean restore( Player player, Zone zone ) {
		
		Boolean result = false;
		
		WorldEditPlugin we = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		if ( we != null ) {
			
			LocalWorld world = ( LocalWorld ) player.getWorld();
			
			Location ne = zone.area.ne;
			Location sw = zone.area.sw;
			
			
			Vector upper = new Vector( Math.max( ne.getX(), sw.getX()) , Math.max( ne.getY(), sw.getY() ), Math.max(ne.getZ(), sw.getZ()) );
			Vector lower = new Vector( Math.min( ne.getX(), sw.getX()) , Math.min( ne.getY(), sw.getY() ), Math.min(ne.getZ(), sw.getZ()) );
			
			ZoneResetPlayer			localPlayer = new ZoneResetPlayer( we, we.getServerInterface(), player);
			LocalSession 			localSession = we.getWorldEdit().getSession( localPlayer );
			EditSession 			editSession = localSession.createEditSession( localPlayer );
			CuboidRegionSelector 	region = new CuboidRegionSelector( world, upper, lower );			
			
			localSession.setRegionSelector( world, region);			
			SnapshotUtilCommands cmd = new SnapshotUtilCommands( we.getWorldEdit() );
			
			try {
				
				CommandContext args = new CommandContext("");
				cmd.restore( args, localSession, localPlayer, editSession);
				result = true;
				
			} catch ( Exception e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		return result;
	}	
	
}
