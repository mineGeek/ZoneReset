package com.github.mineGeek.ZoneReset.Commands;



import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;



public class Reset extends CommandBase {

	public Reset(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		Utilities.setZone();
		return true;
		
		//Player player = (Player)this.sender;
		
		//Zone zone = Zones.getZone( args[0] );
		//return this.plugin.restoreSnapShot( player , zone );

		
	}
	
	
}
