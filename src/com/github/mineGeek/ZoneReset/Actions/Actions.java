package com.github.mineGeek.ZoneReset.Actions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Actions {

	public List<IAction> actions = new ArrayList<IAction>();
	public String tag;
	
	public Actions( String tag ) { this.tag = tag; }
		
	public void add( List<IAction> actions ) {
		for ( IAction a : actions ) actions.add( a );
	}
	
	public void add( IAction action ) {
		actions.add( action );
	}
	
	public void run() {
		for (IAction a : actions ) a.run();
	}
	
	public IAction getByClass( String className ) {
		
		for ( IAction a : actions ) {
			try {

				if ( a.getClass().getSimpleName().equalsIgnoreCase( className ) ) {
					return a;
				}
			} catch (Exception e) {}
		}

		Class<?> clazz;
		IAction a = null;
		try {
			clazz = Class.forName("com.github.mineGeek.ZoneReset.Actions." + className );
			//clazz = Class.forName( className );
			Constructor<?> ctor = clazz.getConstructor(String.class);
			a = (IAction) ctor.newInstance(new Object[] { this.tag });			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		add(a);
		return a;

		
	}
	
	public List<IAction> getByType( String type ) {
		
		List<IAction> list = new ArrayList<IAction>();
		
		for ( IAction a : actions ) {
			
			if ( a.getClass().getSimpleName().equalsIgnoreCase( type ) ) {
				list.add( a );
			}
			
		}
		
		return list;
		
	}
	
}
