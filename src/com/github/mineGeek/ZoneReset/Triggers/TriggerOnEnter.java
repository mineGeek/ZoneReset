package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.entity.Player;


public class TriggerOnEnter extends Trigger {

	public String message = null;
	
	public TriggerOnEnter(String tag) {
		super(tag);
	}

	public void run( Player p ) {
		
		super.run(p);
		if ( message != null ) p.sendMessage( message );
		
	}
	
}
