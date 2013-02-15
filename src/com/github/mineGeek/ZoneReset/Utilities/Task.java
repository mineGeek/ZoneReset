package com.github.mineGeek.ZoneReset.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Messaging.Message;
import com.github.mineGeek.ZoneReset.Messaging.Message.ZRMessageType;
import com.github.mineGeek.ZoneReset.Utilities.Zone.ZRMethod;

public class Task implements Runnable {

	private String 	tag;
	private Long 	nextRun;
	private Long 	lastRun;
	private Long 	interval;
	public  ZoneReset plugin;
	
	private Map<Message, Long> messageQueue = new HashMap<Message, Long>();
	private List<BukkitTask> bukkitTasks = new ArrayList<BukkitTask>();
	
	public Task( ZoneReset plugin, String zoneTag ) {
		
		this.plugin = plugin;
		Zone z = Zones.getZone( zoneTag );
		
		if ( z == null ) return;
		
		Long curTime = System.currentTimeMillis();
		Long interval = z.getTrigTimer();
		Long next = z.getNextTimedReset();
		
	}
	
	public void addMessageTask( Long timeFromNow, Message message ) {
		
		this.messageQueue.put( message, timeFromNow);
		
		final Long time = timeFromNow;
		final String text = message.text;
		final ZRMessageType type = message.type;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask( this.plugin, new Runnable() {
				
	    	    @Override  
	    	    public void run() {
	    	    	try {
	    	    		
	    	    		if ( type.equals( ZRMessageType.REGION ) ) {
	    	    			
	    	    		}
	    	    		
	    	    		Zones.getZone(tag).reset( ZRMethod.TIMED );
	    	    		Zones.getZone(tag).setNextTimedRest( Zones.getZone(tag).getTrigTimer() + System.currentTimeMillis() );
	    	    	} catch (Exception e ) {}
	    	    }
		
		}, time * 20 );
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
