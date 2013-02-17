package com.github.mineGeek.ZoneReset.Commands;


import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Markers.Markers;

public class Edit extends CommandBase {

	public Edit(ZoneReset plugin) {
		super(plugin);
	}
	
	
	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		if ( !(sender instanceof Player) ) {
			mess = "You cannot use this command from the console.";
			return true;
		}
		
		Player p = (Player) sender;
		
		
		if ( Zones.getZone( args[0] ) == null ) {

			this.createZone(p, args[0] );
			mess = "You are now editing a new Zone called " + args[0] + ". /cancel or /save to complete. Set options via /zr set [options]";
			Markers.showZoneBoundaries(p, this.getEditZone(p) );

		} else {
			 Zones.getZone( args[0] ).setEnabled( false );
			this.setEditZone(p,new Zone( Zones.getZone( args[0] ), args[0]) );
			mess = "You are now editing zone " + args[0] + ". /cancel or /save to end edit mode. Set options via /zr set [options]";
			Markers.showZoneBoundaries(p, this.getEditZone(p) );
		}			
		return true;
		
	}
	


}
