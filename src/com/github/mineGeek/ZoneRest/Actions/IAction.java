package com.github.mineGeek.ZoneRest.Actions;

import org.bukkit.configuration.ConfigurationSection;

public interface IAction {
	public void run();
	public void setToConfig( String root, ConfigurationSection c );	
	public void loadFromConfig( String root, ConfigurationSection c );
	public boolean isEnabled();
}
