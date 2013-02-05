package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.mineGeek.ZoneReset.ZoneReset;


abstract class CommandBase  implements CommandExecutor{

	protected ZoneReset 	plugin;
	protected String execMessage 	= null;
	protected CommandSender sender 	= null;
	
	public CommandBase( ZoneReset plugin ) {
		
		this.plugin = plugin;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		this.execMessage = null;
		this.sender = sender;
		
		Boolean result = exec( cmd.getName().toLowerCase(), args );
		
		if ( execMessage != null ) {
			
			sender.sendMessage( this.execMessage );
			
		}
		
		return result;
		
		
	}
	
	protected Boolean exec( String cmdName, String[] args ) {
		
		return false;
	}	
	
	
	
}

