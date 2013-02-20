package com.github.mineGeek.ZoneReset.Tasks;

import com.github.mineGeek.ZoneReset.Data.Zones;

public class ResetTask extends Task implements ITask {

	
	@Override
	public void run() {
		
		Zones.getZone( this.tag ).reset();
		
	}
}
