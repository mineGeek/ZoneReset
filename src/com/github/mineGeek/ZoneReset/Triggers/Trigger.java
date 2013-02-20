package com.github.mineGeek.ZoneReset.Triggers;

public class Trigger {

	public String 	tag;
	public int 		resetSeconds;
	public boolean 	restartTimer = true;
	public boolean 	enabled = false;
	
	public boolean test() {
		return true;
	}
	
	public void run() {
		
	}
	
	public void close() {
		
	}
	
}
