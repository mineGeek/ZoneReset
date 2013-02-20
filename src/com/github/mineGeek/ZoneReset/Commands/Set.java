package com.github.mineGeek.ZoneReset.Commands;



import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneRest.Actions.IAction;
import com.github.mineGeek.ZoneRest.Actions.ResetAction;



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
			
			
			if ( args.length == 1 ) {
				z.resetActions.reset.resetBlocks = !z.resetActions.reset.resetBlocks;
				z.resetActions.reset.resetMobs = !z.resetActions.reset.resetMobs;
				z.resetActions.reset.resetContainers = !z.resetActions.reset.resetContainers;
				mess = z.getTag() + " will " + ( z.resetActions.reset.resetMobs ? "" : "not") + "spawn mobs, " + ( z.resetActions.reset.resetBlocks ? "" : "not") + "reset blocks and " + ( z.resetActions.reset.resetContainers ? "" : "not") + "reset containers inventory.";
				return true;				
			}
			
			String noun = args[1].toLowerCase();
			Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );

			if ( noun.equals( "mobs") ) {

				if ( toggle == null ) {
					
					z.resetActions.reset.resetMobs = !z.resetActions.reset.resetMobs;
					
				} else {
					z.resetActions.reset.resetMobs = toggle;
				}
				
				mess = z.getTag() + " will " + ( z.resetActions.reset.resetMobs ? "no longer " : "now ") + "spawn mobs on reset";
			}
			
			if ( noun.equals( "blocks") ) {

				if ( toggle == null ) {
					
					z.resetActions.reset.resetBlocks = !z.resetActions.reset.resetBlocks;
					
				} else {
					z.resetActions.reset.resetBlocks = toggle;
				}
				
				mess = z.getTag() + " will " + ( z.resetActions.reset.resetBlocks ? "no longer " : "now ") + "reset blocks";
			}
			
			if ( noun.equals( "container") || noun.equals( "containers") ) {

				if ( toggle == null ) {
					
					z.resetActions.reset.resetContainers = !z.resetActions.reset.resetContainers;
					
				} else {
					z.resetActions.reset.resetContainers = toggle;
				}
				
				mess = z.getTag() + " will " + ( z.resetActions.reset.resetContainers ? "no longer " : "now ") + "reset containers";
			}			
			
			
		
		} else if ( key.equals("no") ) {
			
			String noun = args[1].toLowerCase();
			Boolean toggle = ( args.length == 3 ? args[2].equalsIgnoreCase("on") || args[2].equalsIgnoreCase("true") : null );
			
			if ( noun.equals("mobs") ) {
				
				if ( toggle == null ) {
					
					z.preActions.removeEntities.removeMobs = !z.preActions.removeEntities.removeMobs;
					
				} else {
					z.preActions.removeEntities.removeMobs = toggle;
				}				
				
			} else if ( noun.equals("animals") ) {
					
					if ( toggle == null ) {
						
						z.preActions.removeEntities.removeAnimals = !z.preActions.removeEntities.removeAnimals;
						
					} else {
						z.preActions.removeEntities.removeAnimals = toggle;
					}
					
			} else if ( noun.equals("drops") ) {
				
				if ( toggle == null ) {
					
					z.preActions.removeEntities.removeAnimals = !z.preActions.removeEntities.removeDrops;
					
				} else {
					z.preActions.removeEntities.removeDrops = toggle;
				}
				
			} else if ( noun.equals("containers") ) {
				
				if ( toggle == null ) {
					
					z.preActions.removeEntities.removeTiles = !z.preActions.removeEntities.removeTiles;
					
				} else {
					z.preActions.removeEntities.removeTiles = toggle;
				}
				
			} else if ( noun.equals("spawnpoints") ) {
				
				if ( toggle == null ) {
					
					z.preActions.removeSpawnPoints.enabled = false;
					
				} else {
					z.preActions.removeEntities.enabled = toggle;
				}					
				
			}
			
			mess = "Before " + z.getTag() + "resets, the following will be removed - Animals:" + z.preActions.removeEntities.removeAnimals + ", " +
					"Mobs:" + z.preActions.removeEntities.removeMobs + ", " +
					"Drops:" + z.preActions.removeEntities.removeDrops + ", " +
					"Containers :" + z.preActions.removeEntities.removeTiles + ", " +
					"spawnpoints:" + z.preActions.removeSpawnPoints + ".";
			
			
			
		} else if ( key.equals("location") || key.equals("loc") ) {
			
			String option = args[1].toLowerCase();
			String option2 = ( args.length == 3 ? args[2].toLowerCase() : null );
			
			if ( args.length < 3 ) {
				
			}
				if ( option.equals( "move" ) && option2 == null ) {
					
					z.preActions.movePlayers.enabled = true;
					z.preActions.movePlayers.worldName = p.getWorld().getName();
					z.preActions.movePlayers.toX = p.getLocation().getBlockX();
					z.preActions.movePlayers.toY = p.getLocation().getBlockY();
					z.preActions.movePlayers.toZ = p.getLocation().getBlockZ();
					
					mess = "Players in " + z.preActions.movePlayers.scope.toString().toLowerCase() + " will move to here on reset.";
				
				} else if ( option.equals( "move") ) {
					
					z.preActions.movePlayers.enabled = false;
					mess = "Players in " + z.preActions.movePlayers.scope.toString().toLowerCase() + " will no longer move on reset.";
					
				} else if ( option.equals( "spawn") && option2 == null ) {
					
					z.preActions.setSpawnPoints.enabled = true;
					z.preActions.setSpawnPoints.location = p.getLocation().clone();
					
					mess = "Players in " + z.preActions.movePlayers.scope.toString().toLowerCase() + " will respawn here on reset.";
					
				} else if ( option.equals( "spawn") ) {
					
					z.preActions.setSpawnPoints.enabled = false;
					mess = "Players in " + z.preActions.movePlayers.scope.toString().toLowerCase() + " will no longer respawn here on reset.";
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
				
					
				if ( noun.equals( "players") ) {
					
					z.preActions.movePlayers.enabled = true;
					z.preActions.movePlayers.worldName = action;
					z.preActions.movePlayers.toX = locx;
					z.preActions.movePlayers.toY = locy;
					z.preActions.movePlayers.toZ = locz;
					
					mess = "Any players in '" + z.preActions.movePlayers.scope.toString().toLowerCase() + "' will be moved to " + action + " ("+ locx + ", " + locy + ", " + locz + ") when " + z.getTag() + " resets.";
					
				} else if ( noun.equals("spawn") ) {

					z.preActions.setSpawnPoints.enabled = true;
					z.preActions.setSpawnPoints.location = new Location( Bukkit.getWorld( action ), locx, locy, locz );

					mess = "Any players in '" + z.preActions.setSpawnPoints.scope.toString().toLowerCase() + "' will have their spawn set to " + action + " (" + locx + ", " + locy + ", " + locz + ") when " + z.getTag() + " resets.";					
					
				}
			}
			
		/*			
		} else if ( key.equals("trigger" ) ) {
				
			if ( args.length < 2 ) {
				
				mess = "Invalid number of arguments";
				return false;
			}
			
			String type = args[1].toLowerCase();
			
			if ( type.equals("join") ) {
				
				if ( args.length == 2 ) {
					
					z.setTrigOnPlayerJoin( !z.isTrigOnPlayerJoin() );
					mess = z.getTag() + " will " + ( z.isTrigOnPlayerJoin() ? "now" : "no longer") + " auto reset when a player joins.";
					
				} else if ( args.length > 2 ) {
					
					if ( args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("on") ) {
						z.setTrigOnPlayerJoin( true );
						mess = z.getTag() + " will now auto reset when a player joins.";
					} else if ( args[2].equalsIgnoreCase( "false" ) || args[2].equalsIgnoreCase("off" ) ) {
						z.setTrigOnPlayerJoin( false );
						mess = z.getTag() + " will no longer auto reset when a player joins.";
					} else if ( args[2].equalsIgnoreCase("add" ) && args.length > 3) {
						
						for ( int x = 3; x < args.length; x++ ) {
							z.getTrigOnPlayerJoinList().remove( args[x] );
							z.getTrigOnPlayerJoinList().add( args[x] );
							p.sendMessage( "Player " + args[x] + " will now reset zone '" + z.getTag() + "' when they join." );
						}								
						
						
					} else if ( args[2].equalsIgnoreCase("remove") && args.length > 2 ) {
						
						for ( int x = 3; x < args.length; x++ ) {
							z.getTrigOnPlayerJoinList().remove( args[x] );
							p.sendMessage( "Player " + args[x] + " will no longer reset zone '" + z.getTag() + "' when they join." );
						}							
						
					} else if ( args[2].equalsIgnoreCase( "clear") ) {
						
						z.getTrigOnPlayerJoinList().clear();
						mess = z.getTag() + " player reset onJoin list cleared.";
						
					}
				}
					
			} else if ( type.equals("quit") ) {
					
				if ( args.length == 2 ) {
					
					z.setTrigOnPlayerQuit( !z.isTrigOnPlayerQuit() );
					mess = z.getTag() + " will " + ( z.isTrigOnPlayerQuit() ? "now" : "no longer") + " auto reset when a player leaves.";
					
				} else if ( args.length > 2 ) {
					
					if ( args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("on") ) {
						z.setTrigOnPlayerQuit( true );
						mess = z.getTag() + " will now auto reset when a player quits.";
					} else if ( args[2].equalsIgnoreCase( "false" ) || args[2].equalsIgnoreCase("off" ) ) {
						z.setTrigOnPlayerQuit( false );
						mess = z.getTag() + " will no longer auto reset when a player quits.";
					} else if ( args[2].equalsIgnoreCase("add" ) && args.length > 3) {
						
						for ( int x = 3; x < args.length; x++ ) {
							z.getTrigOnPlayerQuitList().remove( args[x] );
							z.getTrigOnPlayerQuitList().add( args[x] );
							p.sendMessage( "Player " + args[x] + " will now reset zone '" + z.getTag() + "' when they quit." );
						}								
						
						
					} else if ( args[2].equalsIgnoreCase("remove") && args.length > 2 ) {
						
						for ( int x = 3; x < args.length; x++ ) {
							z.getTrigOnPlayerQuitList().remove( args[x] );
							p.sendMessage( "Player " + args[x] + " will no longer reset zone '" + z.getTag() + "' when they quit." );
						}							
						
					} else if ( args[2].equalsIgnoreCase( "clear") ) {
						
						z.getTrigOnPlayerQuitList().clear();
						mess = z.getTag() + " player reset onQuit list cleared.";
						
					}
					
				}
					
			} else if ( type.equals( "time" ) ) {
					
				if ( args.length == 2 ) {
					mess = "Invalid option. Expect a format of #d#h#m#s (e.g. 2d3m for every 48 hours and 3 minutes)";
					return true;
				}
				
				z.setTrigTimerText( args[2] );
				
				if ( z.getTrigTimer() == 0 ) {
					mess = "Failure reading time argument. format should be in days, hours, minutes, seconds #[d|h|m|s]. Example for 24 hours: 1d or 24h. You can combined them all together, eg : /zr set time 1d3h5m0s";
				} else {
					mess = "Set Zone '" + z.getTag() + "' to automatically reset evey " + z.getTrigTimerText();
				}
				
				return true;
					
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
			
		}*/
			
		
		return true;
	}
}
