package com.github.mineGeek.ZoneReset.Actions;


import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Data.Zone.ZRPVPMode;
import com.github.mineGeek.ZoneReset.Tasks.ITask;
import com.github.mineGeek.ZoneReset.Tasks.PVPTask;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;
import com.github.mineGeek.ZoneReset.ZoneReset.ZRScope;

public class ActionPVP extends Action {

	public ZRScope scope = ZRScope.REGION;
	public Boolean on = null;
	public Boolean toggle = null;
	
	public Integer startSec = null;
	public Boolean startOn = null;
	public String startMessage = null;
	public String preStartMessage = null;
	
	public Boolean preStartCountdown = null;
	
	public Integer endSec = null;	
	public Integer endOn = null;
	public String endMessage = null;
	public String preEndMessage = null;
	public Boolean preEndCountdown = null;
	
	public SubActionPVPToggle start = null;
	public SubActionPVPToggle end = null;

	public ActionPVP(String tag) {
		super(tag);
	}

	
	@Override
	public void run() {
		
		if ( on != null ) {
			Zones.getZone( this.tag ).pvpMode = ( on ? ZRPVPMode.ON : ZRPVPMode.OFF );
		} else {
			Zones.getZone( this.tag ).pvpMode = ZRPVPMode.DEFAULT;
		}
		
	
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !isEnabled() ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".pvp.scope", scope.toString().toLowerCase() );
		
		if ( on != null ) {
			c.set( root + ".pvp.on", on );
			
		}

		String[] part = new String[] {"start", "end"};
		
		for ( int x = 0; x <2 ; x++ ) {
			
			SubActionPVPToggle p = null;
			String pRoot = root + ".pvp." + part[x];
			if ( x == 0 ) {
				p = start;				
			} else {
				p = end;
			}
			
			if ( p.on != null ) c.set( pRoot + ".on", p.on );
			if ( p.seconds != null ) c.set( pRoot + ".time", Utilities.getTimeStampAsShorthand( p.seconds ) );			
			if ( !p.countdownFrequencyText.isEmpty() ) c.set( pRoot + ".countdown.frequency", p.countdownFrequencyText );
			if ( p.message != null ) c.set( pRoot + ".countdown.message", p.message );
		
			
		}
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + "pvp.scope", "region").toUpperCase() );
		on = c.getBoolean( root + "pvp.on" );

		String[] part = new String[] {"start", "end"};
		
		for ( int x = 0; x <2 ; x++ ) {
			
			String pRoot = root + "pvp." + part[x];
			
			if ( ( c.isSet( pRoot + ".countdown.message") && c.isSet( pRoot + ".countdown.frequency") ) || c.isSet(pRoot + ".on") || c.isSet( pRoot + ".time") ) {
				
				SubActionPVPToggle pvp = new SubActionPVPToggle( tag );
				pvp.on = c.getBoolean( pRoot + ".enable" );
				pvp.setSeconds( c.getString( pRoot + ".time") );
				
				List<String> frequency = c.getStringList( pRoot + ".countdown.frequency");
				if ( !frequency.isEmpty() ) {
					for ( String y : frequency ) {
						pvp.addCountdownFrequency(y);
					}
					
					pvp.message = c.getString( pRoot + ".countdown.message");
				}
				
				if ( x == 0 ) {
					this.start = pvp;
				} else if ( x == 1 ) {
					this.end = pvp;
				}				
				
			}
		}
		
		if ( start != null ) {
			
			PVPTask t = new PVPTask( tag );
			if ( start.on != null ) t.mode = ( start.on ? ZRPVPMode.ON : ZRPVPMode.OFF );
			t.secStart = start.seconds;
			Zones.getZone( tag ).tasks.add( t );
			List<ITask> m = start.getCountdown();
			if ( !m.isEmpty() ) Zones.getZone( tag ).tasks.add( m );
			
		}
		
		if ( end != null ) {
			
			PVPTask t = new PVPTask( tag );
			if ( end.on != null ) t.mode = ( end.on ? ZRPVPMode.ON : ZRPVPMode.OFF );
			t.secStart = end.seconds;
			Zones.getZone( tag ).tasks.add( t );
			List<ITask> m = end.getCountdown();
			if ( !m.isEmpty() ) Zones.getZone( tag ).tasks.add( m );
			
		}			

		
	}


	@Override
	public boolean isEnabled() {

		boolean result = false;
		
		if ( !this.scope.equals( ZRScope.REGION) ) return true;
		if ( on != null ) return true;
		if ( this.start != null ) return true;
		if ( this.end != null ) return true;
		
		return result;
	}
	

}
