package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Utilities.Zone;

public class Set extends CommandBase {

	public Set( ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		if ( !(sender instanceof Player) ) {
			mess = "You cannot use this command from the console.";
			return true;
		}
		
		Player p = (Player) sender;
		Zone z = this.getEditZone(p);
		
		if ( z == null ) {
			mess = "Dude, you need to be editing a zone. Right now you aren't. Type /edit ZONENAME to get started";
			return true;
		}
		
		if ( args.length < 1 ) {
			mess = "Too few arguments.";
			return false;
		}
		
		
		String key = args[0].toLowerCase();
		
		if ( key.equals( "world" ) ) {
			
			z.setWorldName( args[1] );
			mess = z.getTag() + " world set to " + args[1];
			
		} else if ( key.equals("zone") ) {
			
			if ( args.length == 1 ) {
				
				this.setAreaEdit( p , this.getAreaEdit(p));
				
			} else if ( args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") ) {
				
				this.setAreaEdit(p, true );
				
			} else {
				
				this.setAreaEdit(p, false );
			}
				
			mess = z.getTag() + " area edit is now " + ( this.getAreaEdit(p) ? " ON " : " OFF");
			
		} else if ( key.equals( "req") ) {
			
			if ( args.length < 3) {
				mess = "Incorrect number of parameters";
			}
			
			String noun = args[2].toLowerCase();
			
			if ( noun.equals( "players" ) ) {
				
				if ( args.length == 2 ) {
					z.setRequireNoPlayers( !z.isRequireNoPlayers() );
				} else if ( args[3].equalsIgnoreCase( "on" ) || args[3].equalsIgnoreCase("true") ) {
					z.setRequireNoPlayers( true );
				} else {
					z.setRequireNoPlayers( false );
				}
				
				mess = z.getTag() + ( !z.isRequireNoPlayers() ? " does not require " : " requires " ) + "zone to be empty of players";
				
			}
			
		} else if ( key.equals("no") ) {
			
			String noun = args[2].toLowerCase();
			
			if ( noun.equals("mobs") ) {
				
				if ( args.length == 2 ) {
					z.setKillEntities( !z.isKillEntities() );
				} else if ( args[3].equalsIgnoreCase( "on" ) || args[3].equalsIgnoreCase("true") ) {
					z.setKillEntities( true );
				} else {
					z.setKillEntities( false );
				}
				
				mess = z.getTag() + ( !z.isKillEntities() ? " will remove " : " will NOT remove " ) + "mobs in zone.";				
				
			} else if ( noun.equals("spawns") ) {
				
				if ( args.length == 2 ) {
					z.setRemoveSpawnPoints( !z.isRemoveSpawnPoints() );
				} else if ( args[3].equalsIgnoreCase( "on" ) || args[3].equalsIgnoreCase("true") ) {
					z.setRemoveSpawnPoints( true );
				} else {
					z.setRemoveSpawnPoints( false );
				}
				
				mess = z.getTag() + ( !z.isRemoveSpawnPoints() ? " will remove " : " will NOT remove " ) + "any online player spawn points in zone.";
				
			}
			
			
		} else if ( key.equals("location") || key.equals("loc") ) {
			
			if ( args.length != 6 ) {
				
				mess = "Invalid number of arguments. Expected: /set loc NOUN ACTION x y z";
				return false;
				
			}
			
			String 	noun 		= args[2].toLowerCase();
			String 	action 		= args[3].toLowerCase();
			Integer locx 		= Integer.parseInt( args[4] );
			Integer locy 		= Integer.parseInt( args[5] );
			Integer locz 		= Integer.parseInt( args[6] );
			
			if ( noun.equals("move") ) {
				
				if ( action.equals( "players") ) {

					z.setTransportPlayers( z.getWorldName(), locx, locy, locz);
					mess = "Any players in '" + z.getTag() + "' will be moved to " + locx + ", " + locy + ", " + locz + " when zone resets.";
					
				} else if ( action.equals("spawn") ) {

					z.setResetSpawnPoints( z.getWorldName(), locx, locy, locz);
					mess = "Any players in '" + z.getTag() + "' will have their spawn set to " + locx + ", " + locy + ", " + locz + " when zone resets.";					
					
				}
				
			}
			
		}
		
		return true;
	}
}
