package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Player.Markers;

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
		
		if ( !p.hasMetadata("ZREditMode") ) {
			mess = "You are not currently editing a zone! Cancelling edit.";
			return true;
		}
		
		String tag = p.getMetadata("ZREditMode").get(0).asString();
		
		if ( tag == null ) {
			mess = "You do not have a zone name! Cancelling edit.";
		}
		
		Markers.hideZoneBoundaries(p);
		p.removeMetadata("ZREditMode", this.plugin );
		p.removeMetadata("ZR_1", this.plugin );
		p.removeMetadata("ZR_2", this.plugin );
		mess = "Edit mode cancelled.";
		return true;
		
	}
	
}
