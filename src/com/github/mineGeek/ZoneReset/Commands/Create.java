package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;

public class Create extends CommandBase {

	public Create(ZoneReset plugin) {
		super(plugin);
	}
	
	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		if ( !(sender instanceof Player ) ) {
			
			mess = "You cannot run this command from the console :( ";
			return true;
			
		}
		
		if ( args.length == 1 ) {
			
			this.createZone( (Player)sender, args[0]);
			mess = args[0] + " created (but is unsaved. type /edit [option] to change settings or type /save or /cancel to complete.";
			
		} else {
			
			mess = "Incorrect number of argumnets. Expect 1. e.g. /create TedTheZone.";
			
		}
		
		return true;		
		
	}

}
