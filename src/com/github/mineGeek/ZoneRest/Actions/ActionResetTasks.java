package com.github.mineGeek.ZoneRest.Actions;

import org.bukkit.configuration.ConfigurationSection;

import com.github.mineGeek.ZoneReset.Data.Zones;


public class ActionResetTasks extends Action {

	public ActionResetTasks(String tag) {
		super(tag);
	}

	@Override
	public void run() {
		
		Zones.getZone( this.tag ).tasks.restart();
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		return;
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		return;
		
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}	

}
