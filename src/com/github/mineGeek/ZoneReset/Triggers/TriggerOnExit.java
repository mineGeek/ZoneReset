package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.entity.Player;


public class TriggerOnExit extends Trigger {

	public String message = null;
	
	public TriggerOnExit(String tag) {
		super(tag);
	}

	@Override
	public void run( Player p ) {
		
		super.run(p);
		if ( message != null ) p.sendMessage( message );
	}
}
