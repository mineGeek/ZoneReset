package com.github.mineGeek.ZoneReset.Commands;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Data.Zone.ZRMethod;



/**
 * Handles /Reset ZONENAME
 * @author Moí
 *
 */
public class Reset extends CommandBase {

	public Reset(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		if ( args.length != 1 ) {
			
			mess = "Invalid parameters. try /reset zoneName";
			return false;
			
		} else {
			
			Zone z = Zones.getZone( args[0] );
			
			if ( z == null ) {
				
				mess = "There is no zone called " + args[0];
				return true;
				
			} else {
				
				z.reset( ZRMethod.MANUAL );
				return true;
				
			}
			
		}
		
	}
	
	
}
