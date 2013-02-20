package com.github.mineGeek.ZoneRest.Actions;

import java.util.ArrayList;
import java.util.List;

public class Actions {

	public List<IAction> actions = new ArrayList<IAction>();
	
	public ActionEmptyPlayerInventory inventoryEmpty = new ActionEmptyPlayerInventory();
	public ActionFillPlayerInventory inventoryFill = new ActionFillPlayerInventory();
	public ActionFillTileEntities containerFill = new ActionFillTileEntities();
	public ActionMovePlayers movePlayers = new ActionMovePlayers();
	public ActionRemoveEntities removeEntities = new ActionRemoveEntities();
	public ActionResetTasks resetTasks = new ActionResetTasks();
	public ActionSetSpawnPoints removeSpawnPoints = new ActionSetSpawnPoints();
	public ActionSetSpawnPoints setSpawnPoints = new ActionSetSpawnPoints();
	public ResetAction reset = new ResetAction();
	
	public Actions() {
		
		add(inventoryEmpty);
		add(inventoryFill);
		add(containerFill);
		add(movePlayers);
		add(removeEntities);
		add(resetTasks);
		add(removeSpawnPoints);
		add(setSpawnPoints);
		add(reset);
		
	}
	
	public void add( List<IAction> actions ) {
		for ( IAction a : actions ) actions.add( a );
	}
	
	public void add( IAction action ) {
		actions.add( action );
	}
	
	public void run() {
		for (IAction a : actions ) a.run();
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
