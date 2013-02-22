package com.github.mineGeek.ZoneRest.Actions;



abstract class Action implements IAction {

	public String tag;
	public boolean enabled = false;
	

	
	public Action( String tag ) {
		this.tag = tag;
	}

	
}
