package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Actions.ActionMovePlayers;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveEntities;
import com.github.mineGeek.ZoneReset.Actions.ActionRemoveSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.ActionSetSpawnPoints;
import com.github.mineGeek.ZoneReset.Actions.ResetAction;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;



public class Set extends CommandBase {

	public Set( ZoneReset plugin) {
		super(plugin);
	}

	/**
	 * Allows setting of zone properties in game.
	 */
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
				
				this.setAreaEdit( p , !this.getAreaEdit(p));
				
			} else if ( args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") ) {
				
				this.setAreaEdit(p, true );
				
			} else {
				
				this.setAreaEdit(p, false );
			}
				
			mess = z.getTag() + " area edit is now " + ( this.getAreaEdit(p) ? " ON " : " OFF");
			
		} else if ( key.equals( "req") ) {
			
			if ( args.length < 2 ) {
				mess = "Incorrect number of parameters";
				return false;
			}
			
			String noun = args[1].toLowerCase();
			
			if ( noun.equals( "players" ) ) {
				
				if ( args.length == 2 ) {
					z.requiresNoPlayers = !z.requiresNoPlayers ;
				} else if ( args[3].equalsIgnoreCase( "on" ) || args[3].equalsIgnoreCase("true") ) {
					z.requiresNoPlayers = true;
				} else {
					z.requiresNoPlayers = false;
				}
				
				mess = z.getTag() + ( !z.requiresNoPlayers ? " does not require " : " now requires " ) + "zone to be empty of players";
				
			}
			
		} else if ( key.equals( "reset") ) {
			
			
			if ( z.getArea().ne() == null || z.getArea().ne() == null ) {
				mess = "You must have the zone set before running this command.";
				return true;
			}
			
			ResetAction reset = (ResetAction) z.resetActions.getByClass( "ResetAction" );
			
			if ( reset == null ) {
				
				sender.sendMessage("There has been an error with this command.");
				return false;
				
			} else if ( args.length == 1 ) {
				
				reset.resetBlocks = !reset.resetBlocks;
				reset.resetMobs = !reset.resetMobs;
				reset.resetContainers = !reset.resetContainers;
				reset.resetAnimals = !reset.resetAnimals;
				
				mess = z.getTag() + " will " + ( reset.resetMobs ? "" : "not ") + "spawn mobs, " + 
				( reset.resetBlocks ? "" : "not") + "reset blocks and " + ( reset.resetContainers ? "" : "not ") + "reset containers inventory." +
				( reset.resetBlocks ? "" : "not") + "reset blocks and " + ( reset.resetAnimals ? "" : "not ") + "spawn animals.";
				return true;
				
			} else {			
				String noun = args[1].toLowerCase();
				Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );
	
				if ( noun.equals( "mobs") ) {
	
					if ( toggle == null ) {
						
						reset.resetMobs = !reset.resetMobs;
						
					} else {
						reset.resetMobs = toggle;
					}
					
					mess = z.getTag() + " will " + ( reset.resetMobs ? "no longer " : "now ") + "spawn mobs on reset";
				}
				
				if ( noun.equals( "blocks") ) {
	
					if ( toggle == null ) {
						
						reset.resetBlocks = !reset.resetBlocks;
						
					} else {
						reset.resetBlocks = toggle;
					}
					
					mess = z.getTag() + " will " + ( reset.resetBlocks ? "no longer " : "now ") + "reset blocks";
				}
				
				if ( noun.equals( "container") || noun.equals( "containers") ) {
	
					if ( toggle == null ) {
						
						reset.resetContainers = !reset.resetContainers;
						
					} else {
						reset.resetContainers = toggle;
					}
					
					mess = z.getTag() + " will " + ( reset.resetContainers ? "no longer " : "now ") + "reset containers";
				}
				
				if ( noun.equals( "animals") || noun.equals( "animal") ) {
					
					if ( toggle == null ) {
						
						reset.resetAnimals = !reset.resetAnimals;
						
					} else {
						reset.resetAnimals = toggle;
					}
					
					mess = z.getTag() + " will " + ( reset.resetAnimals ? "no longer " : "now ") + "reset animals";
				}				
				
			}
			
		
		} else if ( key.equals("no") ) {
			
			String noun = args[1].toLowerCase();
			Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );			
			
			ActionRemoveSpawnPoints nospawn = null;
			ActionRemoveEntities remove = (ActionRemoveEntities) z.preActions.getByClass( "ActionRemoveEntities" );
			
			if ( remove == null ) {
				sender.sendMessage("There has been an error with this command.");
				return false;
			}			
			
			if ( noun.equals("mobs") ) {
				
				if ( toggle == null ) {
					
					remove.removeMobs = !remove.removeMobs;
					
				} else {
					remove.removeMobs = toggle;
				}				
				
			} else if ( noun.equals("animals") ) {
					
					if ( toggle == null ) {
						
						remove.removeAnimals = !remove.removeAnimals;
						
					} else {
						remove.removeAnimals = toggle;
					}
					
			} else if ( noun.equals("drops") ) {
				
				if ( toggle == null ) {
					
					remove.removeAnimals = !remove.removeDrops;
					
				} else {
					remove.removeDrops = toggle;
				}
				
			} else if ( noun.equals("containers") ) {
				
				if ( toggle == null ) {
					
					remove.removeTiles = !remove.removeTiles;
					
				} else {
					remove.removeTiles = toggle;
				}
				
			} else if ( noun.equals("spawnpoints") ) {
				
				nospawn = (ActionRemoveSpawnPoints) z.preActions.getByClass( "ActionRemoveEntities" );
				
				if ( nospawn == null ) {
					sender.sendMessage("There has been an error with this command.");
					return false;
				}				
				
				if ( toggle == null ) {
					
					nospawn.enabled = false;
					
				} else {
					nospawn.enabled = toggle;
				}					
				
			}
			
			mess = "Before " + z.getTag() + " resets, the following will be removed - ";
			
			if ( remove != null ) {
				mess = mess + "Animals:" + remove.removeAnimals + ", " +
					"Mobs:" + remove.removeMobs + ", " +
					"Drops:" + remove.removeDrops + ", " +
					"Containers :" + remove.removeTiles + " ";			
			}

			if ( nospawn != null ) mess = mess + "spawnpoints:" + nospawn.enabled + ".";
			
			
			
		} else if ( key.equals("location") || key.equals("loc") ) {
			
			if ( args.length == 1 ) {
				
				mess = ChatColor.RED + "Incorrect amount of parameters. Try /set loc [move|spawn]";
				return false;
				
			}
			
			String option = args[1].toLowerCase();
			String option2 = ( args.length == 3 ? args[2].toLowerCase() : null );
			
			ActionMovePlayers move = (ActionMovePlayers) z.preActions.getByClass( "ActionMovePlayers" );
			ActionSetSpawnPoints spawn = null;
			
			if ( move == null ) {
				sender.sendMessage("There has been an error with this command.");
				return false;
			}			
			
			
			if ( args.length < 3 ) {
				
				if ( option.equals( "move" ) && option2 == null ) {
					
					move.enabled = true;
					move.location = p.getLocation().clone();
					
					mess = "Players in " + move.scope.toString().toLowerCase() + " will move to here on reset.";
				
				} else if ( option.equals( "move") ) {
					
					move.enabled = false;
					mess = "Players in " + move.scope.toString().toLowerCase() + " will no longer move on reset.";
					
				} else if ( option.equals( "spawn") && option2 == null ) {
					
					spawn = (ActionSetSpawnPoints) z.preActions.getByClass( "ActionSetSpawnPoints" );
					
					spawn.enabled = true;
					spawn.location = p.getLocation().clone();
					
					mess = "Players in " + spawn.scope.toString().toLowerCase() + " will respawn here on reset.";
					
				} else if ( option.equals( "spawn") ) {
					
					spawn = (ActionSetSpawnPoints) z.preActions.getByClass( "ActionRemoveEntities" );
					spawn.enabled = false;
					mess = "Players in " + spawn.scope.toString().toLowerCase() + " will no longer respawn here on reset.";
				}
			
			} else {
				
			
				if ( args.length < 5 ) {
					
					mess = "Invalid number of arguments. Expected: /set loc [players|spawn] worldname x y z";
					return false;
					
				}
						
				
				String 	noun 		= args[2].toLowerCase();
				String 	action 		= Bukkit.getWorld( args[3].toLowerCase() ).getName();
				Integer locx 		= Integer.parseInt( args[4] );
				Integer locy 		= Integer.parseInt( args[5] );
				Integer locz 		= Integer.parseInt( args[6] );
				
				Location loc = new Location( Bukkit.getWorld( action ), locx, locy, locz );
					
				if ( noun.equals( "players") ) {
					
					move.enabled = true;
					move.location = loc;
					
					
					mess = "Any players in '" + move.scope.toString().toLowerCase() + "' will be moved to " + action + " ("+ locx + ", " + locy + ", " + locz + ") when " + z.getTag() + " resets.";
					
				} else if ( noun.equals("spawn") ) {

					spawn = (ActionSetSpawnPoints) z.preActions.getByClass( "ActionRemoveEntities" );
					
					spawn.enabled = true;
					spawn.location = loc;

					mess = "Any players in '" + spawn.scope.toString().toLowerCase() + "' will have their spawn set to " + action + " (" + locx + ", " + locy + ", " + locz + ") when " + z.getTag() + " resets.";					
					
				}
			}
			
				
		} else if ( key.equals("trigger" ) ) {
				
			if ( args.length < 2 ) {
				
				mess = "Invalid number of arguments";
				return false;
			}
			String type = args[1].toLowerCase();
			
			if ( type.equals("join") ) {
				
				Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );
				
				if ( toggle == null ) {
					
					z.triggers.onJoin.enabled = !z.triggers.onJoin.enabled;
					
				} else  {
					
					z.triggers.onJoin.enabled = toggle;
				}
				
				mess = z.getTag() + " will " + ( z.triggers.onJoin.enabled ? "now" : "no longer") + " auto reset when a player joins.";
					
			} else if ( type.equals("quit") ) {
					
				
				Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );
				
				if ( toggle == null ) {
					
					z.triggers.onQuit.enabled = !z.triggers.onQuit.enabled;
					
				} else  {
					
					z.triggers.onQuit.enabled = toggle;
				}
				
				mess = z.getTag() + " will " + ( z.triggers.onQuit.enabled ? "now" : "no longer") + " auto reset when a player quits.";
					
			} else if ( type.equals( "time" ) ) {
				
				String option = ( args.length > 2 ? args[2].toLowerCase() : null );
				String option2 = ( args.length == 4 ? args[3].toLowerCase() : null );
				
				if ( option == null || option2 == null ) {
					mess = "Invalid option. Expect a format of  #d#h#m#s e.g. /set trigger time [interval|reset] 2d3m (for every 48 hours and 3 minutes)";
					return true;
				}
				
				z.triggers.getOnTimed().enabled = true;
				
				if ( option.equals("interval") ) {
					z.triggers.getOnTimed().resetSeconds = Utilities.getSecondsFromText( option2 )*1;
					z.triggers.getOnTimed().restartTimer = true;
				}
				
				mess = z.getTag() + " will reset every " + Utilities.getTimeStampAsString( z.triggers.getOnTimed().resetSeconds ) + ".";
			
					
			} else if ( type.equals( "interact") ) {
					
				if ( args.length == 3 ) {
					if ( args[2].equalsIgnoreCase("off") ) {
						this.setInteractEditOff(p);
						mess = "Interaction trigger editing off.";
						return true;
					}
				}
				
				this.setInteractEditOn(p);
				mess = "Right click object to set trigger.";
				return true;
					
			}
		}
			
			
		
		return true;
	}
}
