package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Player.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Area;
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
		
		if ( args.length == 0 ) {
			sender.sendMessage( "You must have a name for your zone.");
			return false;
		}
		
		if ( !(sender instanceof Player) ) {
			sender.sendMessage("You must be logged in as a player with an active zone");
			return true;
		}
		
		//Make sure player has a wand?!?
		Player p = (Player) sender;
		
		if ( p.getItemInHand().getTypeId() != Config.wandId ) {
			p.sendMessage("You must be in edit mode with " + Material.getMaterial( Config.wandId).toString().toLowerCase().replace("_",  ""));
			return true;
		}
		
		//Make sure player has 2 areas?
		if ( !Utilities.playerMarkers.containsKey( p.getName() ) ) {
			p.sendMessage("You have no markers selected. Left and right click 2 corners to set the area");
			return true;
		}
		
		Markers m = Utilities.playerMarkers.get( p.getName() );		
		
		if ( m.ne == null || m.sw == null ) {
			p.sendMessage("You need to set 2 opposing corners by left clicking one, and right clicking the other.");
			return true;
		}
		
		Area area = new Area( m.ne, m.sw);
		Zone z = Zones.getZone( args[0] );

		if ( z == null ) {
			//new zone
			z = new Zone();
			z.setTag( args[0] );
			plugin.getConfig().set( "zones." + args[0] + ".hi", 0 ); 
		}
		
		z.setArea( area );
		z.saveBlocks();		
		p.sendMessage( args[0] + " zone has been saved.");
		return true;
		//Player player = (Player)this.sender;
		
		//Zone zone = Zones.getZone( args[0] );
		//return this.plugin.restoreSnapShot( player , zone );

		
	}
}
