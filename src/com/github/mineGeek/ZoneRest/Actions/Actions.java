package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.List;

public class Actions {

	public List<IAction> actions = new ArrayList<IAction>();
	
	public void add( List<IAction> actions ) {
		for ( IAction a : actions ) actions.add( a );
	}
	
	public void add( IAction action ) {
		actions.add( action );
	}
	
	public void run() {
		for (IAction a : actions ) a.run();
	}
	
}
