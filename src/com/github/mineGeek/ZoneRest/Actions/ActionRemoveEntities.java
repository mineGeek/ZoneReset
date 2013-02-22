package com.github.mineGeek.ZoneRest.Actions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionRemoveEntities extends Action {
	
	public ZRScope scope = ZRScope.REGION;
	public boolean removeMobs = true;
	public boolean removeAnimals = true;
	public boolean removeDrops = true;
	public boolean removeTiles = true;
	
	
	public ActionRemoveEntities(String tag) {
		super( tag );
	}

	public void run() {
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<Chunk> chunks = Utilities.getChunksFromArea( Zones.getZone( this.tag ).getArea() );
			
			for ( Chunk c : chunks ) {
				
				for ( Entity e : c.getEntities() ) {

					remove(e);
					
				}
				
				if ( removeTiles ) {
					for ( BlockState bs : c.getTileEntities() ) {
						bs.setType( Material.AIR );
						bs.setRawData( (byte)0 );
					}
				}
				
			}
			
			
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			List<Entity> entities = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getEntities();
			for ( Entity e : entities ) {
				
				remove(e);
				
			}
			
			Chunk[] chunks = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getLoadedChunks();
			
			for ( Chunk c : chunks ) {
				for ( Entity e : c.getEntities() ) {
					remove(e);
				}
				if ( removeTiles ) {
					for ( BlockState bs : c.getTileEntities() ) {
						bs.setType( Material.AIR );
						bs.setRawData( (byte) 0 );
					}
				}
			}
			
		}
		
	}
	
	private void remove( Entity e ) {
	
		if ( removeMobs && e instanceof Monster ) {
			e.remove();
		} else if ( removeAnimals && e instanceof Monster ) {
			e.remove();
		} else if ( removeDrops && e instanceof Item ) {
			e.remove();
		}		
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !enabled ) return;
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".remove.entities.scope", scope.toString().toLowerCase() );
		
		c.set("remove.entities.mobs", removeMobs );
		c.set("remove.entities.animals", removeAnimals );
		c.set("remove.entities.drops", removeDrops );
		c.set("remove.entities.containers", removeTiles );
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {

		scope = ZRScope.valueOf( c.getString( root + ".remove.entities.scope", "region").toUpperCase() );
		
		removeMobs = c.getBoolean(".remove.entities.mobs", false );
		removeAnimals = c.getBoolean(".remove.entities.animals", false );
		removeDrops = c.getBoolean(".remove.entities.drops", false );
		removeTiles = c.getBoolean(".remove.entities.containers", false );
		
		enabled = ( !scope.equals(ZRScope.REGION) || !removeMobs || !removeAnimals || !removeDrops || !removeTiles  );
		
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}	

}
