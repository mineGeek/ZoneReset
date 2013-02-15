package com.github.mineGeek.ZoneReset.Spawners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class MobSpawner extends SpawnBase {

	private EntityType entityType;
	
	public MobSpawner( Entity e ) {
		
		this.setType( ZRSPAWNTYPE.MOB );
		this.setEntityType( e.getType() );
		this.setLocation( e.getLocation() );
	}
	
	public MobSpawner() {}

	public EntityType getEntityType() {
		return this.entityType;
	}
	
	public void setEntityType( EntityType e ) {
		this.entityType = e;
	}
	

	@Override
	public Map<String, Object> getList() {
		
		Map<String, Object> r = (Map<String, Object>)super.getList( this );
		r.put("mob", this.getEntityType().toString().toLowerCase() );
		return r;
		
	}
	
	@Override
	public void setList( Map<String, Object> list ) {
		
		this.setType( ZRSPAWNTYPE.MOB );
		super.setList( list );
		if ( list.containsKey("mob") )this.setEntityType( EntityType.fromName( list.get("mob").toString().toUpperCase() ) );
		
	}

	@Override
	public void spawn() {
		
		if ( this.isHasLocation() ) {
			
			Location l = this.getLocation();
			l.getWorld().spawnEntity(l , this.getEntityType() );
			
		}
		
	}

}
