package com.github.mineGeek.ZoneReset.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.mineGeek.ZoneReset.Player.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.Utilities.Zone;
import com.github.mineGeek.ZoneReset.Utilities.Zones;



public class Listeners implements Listener {


	/**
	 * When player logs in.
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin( PlayerJoinEvent evt ) {

		Utilities.clearPlayerMetaData( evt.getPlayer() );
		Zones.triggerPlayerJoin( evt.getPlayer() );
	}
	
	
	/**
	 * When player leaves
	 * @param evt
	 */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent evt )
    {
    	Utilities.clearPlayerMetaData( evt.getPlayer() );
    	Zones.triggerPlayerQuit( evt.getPlayer() );
    	
    }
    
	/**
	 * When a player interacts with a block/entity
	 * @param evt
	 */
	@EventHandler(priority = EventPriority.LOWEST)
    public void PlayerInteract( PlayerInteractEvent evt ){
        
		if ( evt.getClickedBlock() == null ) return;
		
		/**
		 * only respond if player is in editmode
		 */
		if ( evt.getPlayer().hasMetadata("zra") || evt.getPlayer().hasMetadata("zrinteract")) {
			
			
			/**
			 * make sure they have permission ( though this should already be done )
			 */
			if ( evt.getPlayer().hasPermission("ZoneReset.edit") ) {
				
				/**
				 * Get zone they are editing
				 */
				Object o = evt.getPlayer().getMetadata("zr").get(0).value();				
				if ( o  == null ) return;
				Zone z = (Zone) evt.getPlayer().getMetadata("zr").get(0).value();				
				
				/**
				 * Get location of what they are clicking
				 */
				Location l = evt.getClickedBlock().getLocation();
				String m = l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ();
				
				/**
				 * Right clicking block
				 */
				if ( evt.getAction() == Action.RIGHT_CLICK_BLOCK ) {
					
					/**
					 * Editing area
					 */
					if ( evt.getPlayer().hasMetadata("zra" ) ) {
						z.getArea().setNe( l );
						evt.getPlayer().sendMessage("You set " + m );
					}
					
					/**
					 * Editing interaction
					 */
					if ( evt.getPlayer().hasMetadata("zrinteract") ) {
						z.setOnInteractMaterialId( evt.getClickedBlock().getTypeId() );
						z.setOnInteractLocation( l.getWorld().getName(), l.getBlockX(), l.getBlockY(), l.getBlockZ() );
						evt.getPlayer().sendMessage("You set a trigger on " + Material.getMaterial( evt.getClickedBlock().getTypeId() ).name() + " at " + m );
						evt.getPlayer().removeMetadata("zrinteract", Bukkit.getPluginManager().getPlugin("ZoneReset"));
					}
					
				/**
				 * Left click	
				 */
				} else if ( evt.getAction() == Action.LEFT_CLICK_BLOCK ) {
					
					if ( evt.getPlayer().hasMetadata( "zra") ) {
						z.getArea().setSw( l );
						evt.getPlayer().sendMessage("You set " + m );
					}
					
				}			
				
				/**
				 * Set task to reposition any markers
				 */
				final String peep = evt.getPlayer().getName();				
				
				Bukkit.getScheduler().scheduleSyncDelayedTask( Bukkit.getPluginManager().getPlugin("ZoneReset"), new Runnable() {
				    @Override 
				    public void run() {
				    	Player p = Bukkit.getPlayer( peep );
				    	
				    	if ( p.hasMetadata("zr") ) {
					    	Zone z = (Zone) p.getMetadata("zr").get(0).value();
					    	if ( p != null && p.isOnline() ) {
					    		Markers.showZoneBoundaries( p, z );
					    	}
				    	}
				    }
				}, 3L);
				
			}
			
		/**
		 * If they aren't editing... are they interacting with something that may
		 * trigger a reset?
		 */
		} else if ( evt.getClickedBlock() != null && evt.getAction() == Action.RIGHT_CLICK_BLOCK ) {
			Zones.triggerInteract( evt.getMaterial(), evt.getClickedBlock().getLocation() );
		}
			
		
    }
	
}
