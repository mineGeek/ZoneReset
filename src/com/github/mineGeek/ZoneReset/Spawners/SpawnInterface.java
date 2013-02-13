package com.github.mineGeek.ZoneReset.Spawners;

import java.util.Map;

public interface SpawnInterface {

	public enum ZRSPAWNTYPE {ITEM, CONTAINER, MOB, INVENTORY};
	public Map<String, Object> getList();
	public void setWorldName( String worldName );
	public void setList( Map<String, Object> list );
	public void spawn();
	public ZRSPAWNTYPE getType();
	
	
}
