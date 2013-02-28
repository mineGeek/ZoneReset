package com.github.mineGeek.ZoneReset.Triggers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class Triggers {

	public TriggerOnJoin 	onJoin;
	public TriggerOnQuit 	onQuit;
	public TriggerOnInteract onInteract;
	public Trigger 			onTimed;
	public TriggerOnEnter 	onEnter;
	public TriggerOnExit 	onExit;
	
	public void onJoin( Player p ) 					{ if ( onJoin != null ) onJoin.run(p); }
	public void onQuit( Player p ) 					{ if ( onQuit != null ) onQuit.run(p); }
	public void onInteract( Material m, Location l ){ if ( onInteract != null ) onInteract.run(m,l); }
	public void onTime() 							{ if ( onTimed != null ) onTimed.run(); }
	public void onEnter( Player p ) 				{ if ( onEnter != null) onEnter.run( p ); }
	public void onExit( Player p ) 					{ if ( onExit != null ) onExit.run( p ); }
	
	public void close() {
		
		if ( onJoin != null ) onJoin.close();
		if ( onQuit != null ) onQuit.close();
		if ( onInteract != null ) onInteract.close();
		if ( onTimed != null ) onTimed.close();
		if ( onEnter != null ) onEnter.close();
		if ( onExit != null ) onExit.close();
		
	}
	
	public TriggerOnJoin getOnJoin() {
		if ( onJoin == null ) onJoin = new TriggerOnJoin();
		return onJoin;
	}
	
	public TriggerOnQuit getOnQuit() {
		if ( onQuit == null ) onQuit = new TriggerOnQuit();
		return onQuit;
	}
	
	public TriggerOnInteract getOnInteract() {
		if ( onInteract == null ) onInteract = new TriggerOnInteract();
		return onInteract;
	}
	
	public Trigger getOnTimed() {
		if ( onTimed == null ) onTimed = new TriggerOnEnter();
		return onTimed;
	}
	
	public TriggerOnEnter getOnEnter() {
		if ( onEnter == null ) onEnter = new TriggerOnEnter();
		return onEnter;
	}
	
	public TriggerOnExit getOnExit() {
		if ( onExit == null ) onExit = new TriggerOnExit();
		return onExit;
	}	
	
	
	
}
