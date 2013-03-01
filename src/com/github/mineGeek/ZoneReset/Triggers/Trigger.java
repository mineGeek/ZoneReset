package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Tasks.ResetTask;
import com.github.mineGeek.ZoneReset.Triggers.Triggers.ZRTriggerMode;

abstract class Trigger {

	
	public String 	tag;
	public Integer	resetSeconds = null;
	public boolean	restartTimer = false;
	public boolean 	enabled = false;
	public ZRTriggerMode method = ZRTriggerMode.NONE;
	
	public Trigger( String tag ) { this.tag = tag; }
	
	public boolean test() {
		return true;
	}
	
	public void run() {
		if ( this.restartTimer ) Zones.getZone( tag ).tasks.restart();
	}
	
	public void run( Player p ) {
		
		if ( !method.equals( ZRTriggerMode.NONE ) ) {
			
			if ( method.equals( ZRTriggerMode.INSTANT ) ) {
				Zones.getZone(tag).reset();
			} else if ( method.equals( ZRTriggerMode.TIMER ) ) {
				Zones.getZone( tag ).tasks.restart();
			} else if ( method.equals( ZRTriggerMode.DELAYED ) && this.resetSeconds != null) {
				
				//One off task
				ResetTask r = new ResetTask( tag );
				r.secStart = 0;
				r.secEnd = this.resetSeconds;
				r.start();				
				
			}
			
		}
		
		if ( this.restartTimer ) Zones.getZone( tag ).tasks.restart();

	}
	
	public void close() {
		
	}
	
}
