package com.github.mineGeek.ZoneReset.Commands;



import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zones;




public class Reset extends CommandBase {

	public Reset(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		//Utilities.setZone();
		//Utilities.loadZone();
		
		if ( !(sender instanceof Player ) ) {
			execMessage = "You cannot perform this command from the console.";
			return true;
		} else if ( args.length == 1 ){
			
			Player p = (Player)sender;
			Zone z = Zones.getZone( args[0] );
			
			if ( z == null ) {
				execMessage = "There is no zone called " + args[0];
				return true;
			} else {
				
				z.restore();
				return true;
				
			}
			
		}
		return true;
		

		
	}
	
	
}
