package com.github.mineGeek.ZoneReset.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.github.mineGeek.ZoneReset.Utilities.Utilities;

abstract class Task implements Runnable {

	public Integer 	id = null;
	public Integer 	endId;
	public Integer 	secStart;
	public Integer 	secEnd;
	public Integer 	secInterval;
	public Long		lastStarted;
	public Long		lastStopped;
	public String	tag;
	
	public Integer	secResume;
	
	public Task() {}
	
	public Task( String tag ) { this.tag = tag; }
	
	public Task( Integer start ) {
		this.secStart = start;
	}
	public Task( Integer start, Integer end ) {
		this(start);
		this.secEnd = end;
	}
	public Task(Integer start, Integer end, Integer interval ) {
		this(start, end);
		this.secInterval = interval;
	}
	
	
	public void setResume( Integer i ) { this.secResume = i; }
	public Integer getResume() { return (int) (this.getTimeSinceStart()/1000); }
	
	public boolean isRunning() { 
		if( id != null ) {
			return Bukkit.getScheduler().isCurrentlyRunning( id ) || Bukkit.getScheduler().isQueued( id );
		}
		return false;
	}
	
	public void start() {

		BukkitTask task = null;
		
		Integer toStart = secStart;
		
		if ( secResume != null ) {
			if ( secResume < toStart ) {
				toStart -= secResume;
			} else {
				toStart = secResume - toStart;
			}
		}
		
		if ( secInterval != null && toStart != null ) {
			task = Bukkit.getScheduler().runTaskTimer( Utilities.plugin, this, toStart * 20, secInterval * 20 );
		} else if ( toStart != null ){
			task = Bukkit.getScheduler().runTaskLater(Utilities.plugin, this, (toStart == 0 ? 2 : toStart * 20 ) );
		}
		
		lastStarted = System.currentTimeMillis();
		
		if ( secResume != null ) lastStarted -= secResume * 1000; 
		
		id = task.getTaskId();
		final int endTaskId = new Integer(id);
		
		if ( secEnd != null ) {
			
			Integer toEnd = secEnd;
			if ( secResume != null ) {
				if ( toEnd < secResume ) {
					toEnd = secResume - toEnd;
					
				} else {
					toEnd -= secResume;
				}
			}
			
			BukkitTask endTask = Bukkit.getScheduler().runTaskLater( Utilities.plugin, new Runnable() {
				
				public void run() {
					Bukkit.getScheduler().cancelTask( endTaskId );
				}
				
			}, toEnd * 20 );
			
			endId = endTask.getTaskId();
			
		}
		
		secResume = null;
		
		
	}
	
	public void stop() {
		if ( id != null ) 	Bukkit.getScheduler().cancelTask( id );
		if ( endId != null) Bukkit.getScheduler().cancelTask( endId );
		lastStopped = System.currentTimeMillis();
	}
	
	public void restart() {
		this.stop();
		this.start();
	}
	
	public String getTextLastStart() {
		Long i = this.lastStarted;
		if ( i != null ) return Utilities.getTimeStampAsString( i );
		return "";		
	}
	
	public String getTextSinceStart() {
		Long i = this.getTimeSinceStart();
		if ( i != null ) return Utilities.getTimeStampAsString( i );
		return "";
	}
	
	public Long getTimeSinceStart() {
		if ( this.lastStarted != null ) return System.currentTimeMillis() - this.lastStarted;
		return null;
	}
	
	public String getTextToEnd() {
		Long i = this.getTimeToEnd();
		if ( i != null ) return Utilities.getTimeStampAsString( i );
		return "";		
	}
	
	public Long getTimeToEnd() {
		if ( this.lastStarted != null && this.secEnd != null ) return ( lastStarted + ( secEnd * 1000 ) ) - System.currentTimeMillis();
		return null;
	}
	

	@Override
	public void run() {
		
		
	}
	
}
