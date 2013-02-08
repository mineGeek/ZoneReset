package com.github.mineGeek.ZoneReset.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.metadata.FixedMetadataValue;


import com.github.mineGeek.ZoneReset.Player.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;



public class Listeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemHeldChange(PlayerItemHeldEvent evt) {
		
		
		if ( evt.getPlayer().getInventory().getItem( evt.getNewSlot() ) != null && evt.getPlayer().getInventory().getItem( evt.getNewSlot() ).getTypeId() == Config.wandId ) {
			//moving to a wand.
			Utilities.showPlayerMarkers( evt.getPlayer() );
		} else if ( evt.getPlayer().getItemInHand().getTypeId() != 0 && evt.getPlayer().getItemInHand().getTypeId() == Config.wandId ) {
			//Moving away from wand
			Utilities.hidePlayerMarkers( evt.getPlayer() );
			
		}
		
	}
			
	@EventHandler(priority = EventPriority.LOWEST)
    public void rightClicky( PlayerInteractEvent evt ){
        
			
		if ( evt.getPlayer().getItemInHand().getTypeId() == Config.wandId ) {
			if ( evt.getPlayer().hasPermission("ZoneReset.editZone") ) {
				
				if ( !Utilities.playerMarkers.containsKey( evt.getPlayer().getName() ) ) {
					Utilities.playerMarkers.put( evt.getPlayer().getName(), new Markers() );
				}				
				
				Location l = evt.getClickedBlock().getLocation();
				String s = l.getWorld().getName() + "|" + l.getBlockX() + "|" + l.getBlockY() + "|" + l.getBlockZ();
				String m = l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ();
				
				if ( evt.getAction() == Action.RIGHT_CLICK_BLOCK ) {
					evt.getPlayer().setMetadata("ZR_2", new FixedMetadataValue( Bukkit.getServer().getPluginManager().getPlugin("ZoneReset"), s) );
					evt.getPlayer().sendMessage("You set " + m );
					Utilities.playerMarkers.get( evt.getPlayer().getName() ).setNE( evt.getPlayer(), l );	
				} else if ( evt.getAction() == Action.LEFT_CLICK_BLOCK ) {
					evt.getPlayer().setMetadata("ZR_1", new FixedMetadataValue( Bukkit.getServer().getPluginManager().getPlugin("ZoneReset"), s) );
					evt.getPlayer().sendMessage("You set " + m );
					Utilities.playerMarkers.get( evt.getPlayer().getName() ).setSW( evt.getPlayer(), l );
				}
				
				Utilities.playerMarkers.get( evt.getPlayer().getName() ).setMarkers( evt.getPlayer() );
				final String peep = evt.getPlayer().getName();
				Bukkit.getScheduler().scheduleSyncDelayedTask( Bukkit.getServer().getPluginManager().getPlugin("ZoneReset"), new Runnable() {
				    @Override 
				    public void run() {
				    	Utilities.showPlayerMarkers( Bukkit.getPlayer( peep ) );
				    }
				}, 5L);				

				
				evt.setCancelled( true );
			}
		}
			
		
    }
	
}
