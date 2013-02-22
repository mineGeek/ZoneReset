package com.github.mineGeek.ZoneRest.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.mineGeek.ZoneReset.Data.Area;
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

	public ResetAction(String tag) {
		super(tag);
	}

	public boolean resetBlocks = true;
	public boolean resetContainers = true;
	public boolean resetMobs = true;
	public boolean enabled = true;
	public Area area;
	
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

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
			
		if ( area != null && area.ne() != null && area.sw() != null ) {
			c.set( root + ".reset.world", area.worldName );
			c.set( root + ".reset.ne", new ArrayList<Integer>( Arrays.asList( area.ne().getBlockX(), area.ne().getBlockY(), area.ne().getBlockZ() ) ) );
			c.set( root + ".reset.sw", new ArrayList<Integer>( Arrays.asList( area.sw().getBlockX(), area.sw().getBlockY(), area.sw().getBlockZ() ) ) );
		}		
		
		c.set( root + ".reset.blocks", this.resetBlocks );
		c.set( root + ".reset.containers", this.resetContainers );
		c.set( root + ".reset.mobs", this.resetMobs );
		
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		String worldName = Zones.getZone( this.tag ).getWorldName();
		worldName = c.getString(".reset.world", worldName );
		List<Integer> neXyz = c.getIntegerList(".reset.ne");
		List<Integer> swXyz = c.getIntegerList(".reset.sw");
		
		if ( neXyz != null && swXyz != null ) {
			this.area = new Area( worldName, neXyz.get(0), neXyz.get(1), neXyz.get(2), swXyz.get(0), swXyz.get(1), swXyz.get(2));
		}
		
		this.resetBlocks = c.getBoolean(".reset.blocks", true );
		this.resetContainers = c.getBoolean(".reset.containers", true );
		this.resetMobs = c.getBoolean( ".reset.mobs", true);
		
		enabled = ( this.area != null );
		
		
		
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
