package com.github.mineGeek.ZoneReset.Commands;


import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;

public class Save extends CommandBase {
	
	public Save(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		
		if ( !( sender instanceof Player ) ) {
			mess = "You cannot use this command from the console.";
			return true;
		}
		
		Player p = (Player) sender;	
		this.saveZone(p);
		return true;		
	}
		
}
