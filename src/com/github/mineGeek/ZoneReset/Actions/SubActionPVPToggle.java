package com.github.mineGeek.ZoneReset.Actions;

import java.util.ArrayList;
import java.util.List;

import com.github.mineGeek.ZoneReset.Tasks.MessageTask;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;

public class SubActionPVPToggle {

	public String 		secText = null;
	public Boolean		on = null;
	public Integer 		seconds = null;
	public String 		message = null;
	public boolean		messageIsTokened = false;
	public String 		countDownText = null;
	public List<String> countdownFrequencyText = new ArrayList<String>();
	public List<Integer>countdownFrequency = new ArrayList<Integer>();
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage( String value ) {
		
		messageIsTokened = value.contains("%");
		message = value;
		
	}
	
	public void setSeconds( String value ) {		
		seconds = (int)(Utilities.getSecondsFromText(value)/1000);		
	}
	
	public void addCountdownFrequency( String value ) {
		countdownFrequencyText.add( value );
		countdownFrequency.add( (int)(Utilities.getSecondsFromText(value)/1000));
	}
	
	public List<MessageTask> getCountdown() {
		
		List<MessageTask> tasks = new ArrayList<MessageTask>();
		
		if ( !countdownFrequency.isEmpty() ) {
			
			for ( Integer x : countdownFrequency ) {
				
				MessageTask m = new MessageTask();
				m.setMessage( message );
				m.secStart = x;
				tasks.add( m );
			}
			
		}
		
		return tasks;
		
	}
	
}
