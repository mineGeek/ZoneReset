package com.github.mineGeek.ZoneReset.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.mineGeek.ZoneReset.Data.Zones;
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
		
		List<Player> players = new ArrayList<Player>();
		
		if ( this.scope.equals( ZRScope.REGION ) ) {
			
			List<String> ps = Zones.getZone( this.tag ).getPlayers();
			for ( String x : ps ) {
				if (Bukkit.getPlayer(x) != null ) players.add( Bukkit.getPlayer(x) );
			}
			
		} else if ( this.scope.equals( ZRScope.WORLD ) ) {
			
			players = Bukkit.getWorld( Zones.getZone( this.tag ).getWorldName() ).getPlayers();
			
			
		} else if ( this.scope.equals( ZRScope.SERVER ) ) {
			Player[] ps = (Player[]) ( Bukkit.getServer().getOnlinePlayers() );
			players.addAll( Arrays.asList( ps ) );
		}
		
		if ( !players.isEmpty() ) {
			
			for ( Player p : players ) {
				
				if ( p.isOnline() ) {
					
					if ( on != null ) p.setMetadata("ZRPVP", new FixedMetadataValue( Utilities.plugin, on ) );
				}
				
			}
			
			
		}
		
	}

	@Override
	public void setToConfig(String root, ConfigurationSection c) {
		
		if ( !isEnabled() ) return;
		
		if ( !scope.equals( ZRScope.REGION ) ) c.set( root + ".pvp.scope", scope.toString().toLowerCase() );
		
		if ( on != null ) {
			c.set( root + ".pvp.on", on );
			
		}

		if ( this.start != null ) {
			
			if ( start.on != null ) c.set( root + ".start", value)
			
		}
		
	}

	@Override
	public void loadFromConfig(String root, ConfigurationSection c) {
		
		scope = ZRScope.valueOf( c.getString( root + ".pvp.scope", "region").toUpperCase() );
		on = c.getBoolean( root + ".pvp.on" );

		String[] part = new String[] {"start", "end"};
		
		for ( int x = 0; x <2 ; x++ ) {
			
			String pRoot = root + "." + part[x];
			
			if ( ( c.isSet( pRoot + ".pvp.countdown.message") && c.isSet( pRoot + ".pvp.countdown.frequency") ) || c.isSet(pRoot + ".pvp.on") || c.isSet( pRoot + ".pvp.time") ) {
				
				SubActionPVPToggle pvp = new SubActionPVPToggle();
				
				if ( c.isSet( pRoot + ".pvp.on" ) ) pvp.on = c.getBoolean( pRoot + ".pvp.on" );
				pvp.setSeconds( c.getString( pRoot + ".pvp.time") );
				
				List<String> frequency = c.getStringList( pRoot + ".pvp.countdown.frequency");
				if ( !frequency.isEmpty() ) {
					for ( String y : frequency ) {
						pvp.addCountdownFrequency(y);
					}
					
					pvp.message = c.getString( pRoot + ".pvp.countdown.message");
				}
				
				if ( x == 0 ) {
					this.start = pvp;
				} else if ( x == 1 ) {
					this.end = pvp;
				}				
				
			}
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
