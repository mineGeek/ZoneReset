package com.github.mineGeek.ZoneReset.Commands;


import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Player.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zones;

public class Save extends CommandBase {
	
	public Save(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		
		if ( !(sender instanceof Player) ) {
			execMessage = "You must be logged in as a player with an active zone";
			return true;
		}
		
		Player p = (Player) sender;
		
		if ( !p.hasMetadata("ZREditMode") ) {
			execMessage = "You are not currently editing a zone! Cancelling edit. No Changes were made";
			return true;
		}
		
		Zone z = (Zone) p.getMetadata("ZREditMode").get(0).value();
		
		Config.saveZoneConfig( z );
		
		
		if ( Zones.getZone( z.getTag() ) == null ) {
			Zones.addZone( z.getTag(), Config.c.getConfigurationSection("zones." + z.getTag() ) );
		}
		
		z.saveBlocks();
		Markers.hideZoneBoundaries( p );
		Utilities.clearPlayerMetaData( p );
		p.sendMessage( z.getTag() + " has been saved.");
		
		/*
		if ( p.getItemInHand().getTypeId() != Config.wandId ) {
			execMessage = "You must be in edit mode with " + Material.getMaterial( Config.wandId).toString().toLowerCase().replace("_",  "");
			return true;
		}
		
		//Make sure player has 2 areas?
		if ( !Utilities.playerMarkers.containsKey( p.getName() ) ) {
			execMessage = "You have no markers selected. Left and right click 2 corners to set the area";
			return true;
		}
		
		Markers m = Utilities.playerMarkers.get( p.getName() );		
		
		if ( m.ne == null || m.sw == null ) {
			execMessage = "You need to set 2 opposing corners by left clicking one, and right clicking the other.";
			return true;
		}
		
		Area area = new Area( m.ne, m.sw);
		Zone z = Zones.getZone( args[0] );

		if ( z == null ) {
			//new zone
			z = new Zone();
			z.setTag( args[0] );
			z.setSnapShotName( args[0] );
			plugin.getConfig()area.set ( "zones." + args[0] + ".ne", z.getSnapShotName() );
			plugin.getConfig().set( "zones." + args[0] + ".snapshot", z.getSnapShotName() );
			plugin.saveConfig();
		}
		
		z.setArea( area );
		z.saveBlocks();		
		p.sendMessage( args[0] + " zone has been saved.");
		return true;
		//Player player = (Player)this.sender;
		
		//Zone zone = Zones.getZone( args[0] );
		//return this.plugin.restoreSnapShot( player , zone );
		*/
		

		
		return true;
		
		
	}
}
