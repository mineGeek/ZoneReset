package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.commands.SnapshotUtilCommands;
import com.sk89q.worldedit.regions.CuboidRegionSelector;

public class RestoreWESnapshot {

	private Player player;
	private LocalWorld world;
	
	public RestoreWESnapshot( Player player, World world ) {
		
		this.player = player;
		this.world = Bukkit.getWorld( world );
		
	}
	
	public Boolean restore(Location ne, Location sw) {
		Boolean result = false;
		
		WorldEditPlugin wep = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		if (wep != null) {
			Vector l1 = new Vector( ne.getX(), ne.getY(), ne.getZ() );
			Vector l2 = new Vector( sw.getX(), sw.getY(), sw.getZ() );
			//Vector l1 = new Vector(-157, 82, 288);
			//Vector l2 = new Vector(-126, 67, 322);
			
			
			//BukkitPlayer localPlayer = wep.wrapPlayer( this.player );
			ZoneResetPlayer localPlayer = new ZoneResetPlayer(wep, wep.getServerInterface(), this.player);
			LocalSession localSession = wep.getWorldEdit().getSession( localPlayer );
			EditSession editSession = localSession.createEditSession( localPlayer );
			CuboidRegionSelector region = new CuboidRegionSelector( this.world, l1, l2);
			
			
			localSession.setRegionSelector( this.world, region);
			
			SnapshotUtilCommands cmd = new SnapshotUtilCommands( wep.getWorldEdit() );
			try {
				CommandContext args = new CommandContext("");
				cmd.restore( args, localSession, localPlayer, editSession);
				result = true;
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
		
		
	}	
	
}
