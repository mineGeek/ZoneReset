package com.github.mineGeek.ZoneReset.Triggers;

import com.github.mineGeek.ZoneReset.Utilities.Utilities;
public class Triggerd implements Runnable {

	public enum ZRTriggerMethod { PLAYERJOIN, PLAYERQUIT, INTERACT, TIMED, NONE }
	
	private String zoneTag;
	private ZRTriggerMethod method = ZRTriggerMethod.NONE;
	private Long stampStart = 0L;
	private Long stampStop = 0L;
	private Long timeStarted = 0L;
	private String timerStart = null;
	private String timerStop = null;
	private Long timerStartOffset = 0L;
	private Long timerStopOffset = 0L;
	private boolean repeat = false;
	
	public String getZoneTag() {
		return this.zoneTag;
	}
	
	public void setZoneTag( String value ) {
		this.zoneTag = value;
	}
	
	public ZRTriggerMethod getMethod() {
		return this.method;
	}
	
	public void setMethod( ZRTriggerMethod method ) {
		this.method = method;
	}
	
	public void setRepeat( boolean value ) {
		this.repeat = value;
	}
	
	public boolean getRepeat() {
		return this.repeat;
	}
	
	public Long getTimeStarted() {
		return this.timeStarted;
	}
	
	public void setTimerStart( String value ) {		
		this.timerStart = value;
		this.setTimerStartOffset( Utilities.getSecondsFromText( value ) );		
	}
	
	public String getTimerStart() {
		return this.timerStart;
	}
	
	public void setTimerStartOffset( Long value ) {
		this.timerStartOffset = value;
		this.setStampStart( (System.currentTimeMillis()/1000) + value );
	}
	
	public Long getTimerStartOffset() {
		return this.timerStartOffset;
	}
	
	public void setStampStart( Long value ) {
		this.stampStart = value;
	}
	
	public Long getStampStart() {
		return this.stampStart;
	}
	
	public void setTimerStop( String value ) {
		this.timerStop = value;
		this.setTimerStopOffset( Utilities.getSecondsFromText(value));
	}
	
	public String getTimerStop() {
		return this.timerStop;
	}
	
	public void setTimerStopOffset( Long value ) {
		this.timerStopOffset = value;
		this.setStampStop( ( System.currentTimeMillis()/1000) + value );
	}
	
	public Long getTimerStopOffset() {
		return this.timerStopOffset;
	}
	
	public void setStampStop( Long value ) {
		this.stampStop = value;
	}
	
	public Long getStampStop() {
		return this.stampStop;
	}
	
	public Long getTimeRemaining() {
		return ( System.currentTimeMillis() /1000) - this.timeStarted - this.getTimerStopOffset();
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
