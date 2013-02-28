package com.github.mineGeek.ZoneReset.Triggers;

import com.github.mineGeek.ZoneReset.Data.Zones;

abstract class Trigger {

	public String 	tag;
	public int 		resetSeconds;
	public boolean 	restartTimer = true;
	public boolean 	enabled = false;
	
	public Trigger( String tag ) { this.tag = tag; }
	
	public boolean test() {
		return true;
	}
	
	public void run() {
		
		Zones.getZone( tag ).reset();
		if ( restartTimer ) Zones.getZone( tag ).tasks.restart();
	}
	
	public void close() {
		
	}
	
}
