package com.github.mineGeek.ZoneReset.Tasks;

import com.github.mineGeek.ZoneReset.Data.Zones;

public class ResetTask extends Task implements ITask {

	public ResetTask( String tag ) { super(tag); }
	
	@Override
	public void run() {
		
		Zones.getZone( this.tag ).reset();
		
	}
}
