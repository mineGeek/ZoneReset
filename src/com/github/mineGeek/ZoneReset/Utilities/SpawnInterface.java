package com.github.mineGeek.ZoneReset.Utilities;

import java.util.Map;

public interface SpawnInterface {

	public enum ZR_SPAWN_TYPE {ITEM};
	public void setList( Map<?, ?> item );
	public Map<?, ?> getList();
	public void spawn();
	public ZR_SPAWN_TYPE getType();
	
	
}
