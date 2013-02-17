package com.github.mineGeek.ZoneReset.Spawners;

import java.util.Map;

/**
 * Interface for handling classes responsible for spawning items and entities
 * @author Moí
 *
 */
public interface SpawnInterface {

	public enum ZRSPAWNTYPE {NONE, ITEM, CONTAINER, MOB, INVENTORY, SIGN};
	public Map<String, Object> getList();
	public void setWorldName( String worldName );
	public void setList( Map<String, Object> list );
	public void spawn();
	public ZRSPAWNTYPE getType();
	
	
}
