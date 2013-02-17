package com.github.mineGeek.ZoneReset.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;



public class ZMob implements Serializable {

	private static final long serialVersionUID = 1543092309859360391L;
	public String worldName;
	public String entityType;
	public double x;
	public double y;
	public double z;
	public float pitch;
	public float yaw;
	public int health;
	public int age;
	public double vx;
	public double vy;
	public double vz;
	
	Map<String, ZItem> items;
	
	
	public ZMob( Entity e ) {
		
		
		Location l = e.getLocation();
		this.entityType = e.getType().getName();
		this.worldName = e.getWorld().getName();
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.pitch = l.getPitch();
		this.yaw = l.getYaw();
		//this.direction = l.getDirection();
		this.vx = l.getDirection().getX();
		this.vy = l.getDirection().getY();
		this.vz = l.getDirection().getZ();
		
		if ( e instanceof LivingEntity ) {

			LivingEntity le = (LivingEntity) e;

			if ( le.getEquipment().getArmorContents().length > 0 ) {
				this.items = new HashMap<String, ZItem>();
				this.items.put( "boots", new ZItem( le.getEquipment().getBoots() ) );
				this.items.put( "chest", new ZItem( le.getEquipment().getChestplate() ) );
				this.items.put( "helmet", new ZItem( le.getEquipment().getHelmet() ) );
				this.items.put( "hand", new ZItem( le.getEquipment().getItemInHand() ) );
				this.items.put( "leg", new ZItem( le.getEquipment().getLeggings() ) );
				
			}			
			
		}
		
		if ( e instanceof Damageable ) {
			
			this.health = ((Damageable)e).getHealth();
			
		}
		
		if ( e instanceof Ageable ) {
			
			this.age = ((Ageable)e).getAge();
			
		}
		
	}
	
	public Entity spawnEntity() {
		
		//Vector v = new Vector( this.vx, this.vy, this.vz);
		Location l = new Location( Bukkit.getWorld( this.worldName), this.x, this.y, this.z, this.pitch, this.yaw );

		Entity e = Bukkit.getWorld( this.worldName ).spawnEntity(l, EntityType.fromName( this.entityType) );
		
		
		
		
		if ( e instanceof LivingEntity ) {
			
			if ( this.items != null && !this.items.isEmpty() ) {
				
				if ( this.items.containsKey("boots") ) ((LivingEntity)e).getEquipment().setBoots( this.items.get("boots").getItemStack() );
				if ( this.items.containsKey("chest") ) ((LivingEntity)e).getEquipment().setChestplate( this.items.get("chest").getItemStack() );
				if ( this.items.containsKey("helmet") ) ((LivingEntity)e).getEquipment().setHelmet( this.items.get("helmet").getItemStack() );
				if ( this.items.containsKey("hand") ) ((LivingEntity)e).getEquipment().setItemInHand( this.items.get("hand").getItemStack() );
				if ( this.items.containsKey("leg") ) ((LivingEntity)e).getEquipment().setLeggings( this.items.get("leg").getItemStack() );
			}
			
		}
		
		if ( e instanceof Damageable ) {
			((Damageable) e).setHealth( this.health );
		}
		
		if ( e instanceof Ageable ) {
			((Ageable) e).setAge(this.age );
		}
		
		return null;
	}
	
}
