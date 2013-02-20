package com.github.mineGeek.ZoneRest.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.InventoryHolder;

import com.github.mineGeek.ZoneReset.Data.ZArea;
import com.github.mineGeek.ZoneReset.Data.ZBlock;
import com.github.mineGeek.ZoneReset.Data.ZItem;
import com.github.mineGeek.ZoneReset.Data.ZMob;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.nms.CraftMassBlockUpdate;
import com.github.mineGeek.ZoneReset.nms.MassBlockUpdate;
import com.github.mineGeek.ZoneReset.nms.NMSAbstraction;
import com.github.mineGeek.ZoneReset.nms.NMSHelper;

public class ResetAction extends Action {

	public boolean resetBlocks = true;
	public boolean resetContainers = true;
	public boolean resetMobs = true;
	@Override
	public void run() {
		ZArea z = this.getArea();
		if ( z != null ) this.resetArea( z );
	}
	
	private ZArea getArea() {
		
		try {
			FileInputStream fileIn = new FileInputStream( Config.folderSnapshots + File.separator + this.tag + ".ser" );
			ObjectInputStream in = new ObjectInputStream( fileIn );
			ZArea z = ( ZArea ) in.readObject();
			in.close();
			fileIn.close();
			return z;

		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void resetArea( ZArea z ) {
		
		List<ZBlock> deferred = new ArrayList<ZBlock>();
		List<ZMob> mobs = z.getMobs();
		
		if ( this.resetBlocks ) {
		
			try {
				NMSAbstraction nms = NMSHelper.init( Bukkit.getPluginManager().getPlugin("ZoneReset") );				
				List<ZBlock> b = z.getBlocks();				
				World w = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() );			
				
				if ( nms == null || Config.noNMS ) {
					
					if ( !b.isEmpty() ) {
						
						for ( ZBlock zb : b ) {
							
							Block block = w.getBlockAt( zb.x, zb.y, zb.z );
							block.setType( Material.getMaterial( zb.materialId ) );
							block.setData( zb.data );
							block.getState().update( true );
						}
						
					}					
					
				} else {
					
	
					MassBlockUpdate mbu = CraftMassBlockUpdate.createMassBlockUpdater( w );				
					if ( !b.isEmpty() ) {	
						for ( ZBlock zb : b ) {
	
								mbu.setBlock(zb.x, zb.y, zb.z, zb.materialId, zb.data);	
								
								if ( zb.hasInventory && this.resetContainers ) {
									
									Block block  = w.getBlockAt( zb.x, zb.y, zb.z);
									InventoryHolder ih = (InventoryHolder)block.getState();
									ih.getInventory().clear();									
									for ( ZItem i : zb.items ) {
										ih.getInventory().addItem( i.getItemStack() );
									}
								}
	
								if ( zb.lines != null ) {
									
									Block block = w.getBlockAt( zb.x, zb.y, zb.z );
									if ( block.getState() instanceof Sign ) {
										
										Sign s = (Sign)block.getState();
										
										for ( int y=0; y < zb.lines.size(); y++) {
											s.setLine(y, zb.lines.get(y) );
										}
										
										s.update( true );
									}
									
								}
						}
						
						mbu.notifyClients();
						
					}
					
				}			
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			
	
			if ( !deferred.isEmpty() ) {
				
				World w = Bukkit.getWorld( Zones.getZone( this.tag).getWorldName() );
				
				for ( ZBlock zb : deferred ) {			
					
					Block block = w.getBlockAt( zb.x, zb.y, zb.z );
					block.setType( Material.getMaterial( zb.materialId ) );
					block.setData( zb.data );
					block.getState().update( true );				
					
				}
				
			}
		}
		
		if ( this.resetMobs ) {
			if ( !mobs.isEmpty() ) {
				
				for ( ZMob mob : mobs ) {
					
					mob.spawnEntity();
					
				}
				
			}
		}
		
		
	}	

}
