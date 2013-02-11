package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.github.mineGeek.ZoneReset.Utilities.EntityInfoBase.EntityInfoType;

public interface EntityInfoInterface {

	public abstract String getEntityName();

	public abstract EntityInfoType getEntityInfoType();

	public abstract EntityType getEntityType();

	public abstract String getWorldName();

	public abstract int getX();

	public abstract int getY();

	public abstract int getZ();

	public abstract Location getLocation();

}