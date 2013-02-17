package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Markers.Markers;

public class Cancel extends CommandBase {

	public Cancel(ZoneReset plugin) {
		super(plugin);
	}
	
	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		if ( !(sender instanceof Player ) ) {
			mess = "You cannot run this command from the console.";
			return false;
		}
		
		Player p = (Player)sender;
		Markers.hideZoneBoundaries(p);
		Zone z = this.getEditZone(p);
		if ( z == null ) {
			
			mess = "You are not currently editing a zone! Cancelling edit.";
			return true;			
			
		}
		
		String t = z.getTag();
		this.cancelEdit( p );
		
		if ( Zones.getZone( t ) != null ) {
			Zones.getZone( t ).setEnabled( true );
		}
		mess = "you are no longer editing '" + t + "'";
		
		return true;
		
	}
	
}
