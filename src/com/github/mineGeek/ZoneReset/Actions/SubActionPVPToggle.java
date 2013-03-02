package com.github.mineGeek.ZoneReset.Actions;

import java.util.ArrayList;
import java.util.List;

import com.github.mineGeek.ZoneReset.Tasks.ITask;
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
	public String 		tag;
	
	public SubActionPVPToggle( String tag ) { this.tag=tag;}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage( String value ) {
		
		messageIsTokened = value.contains("%");
		message = value;
		
	}
	
	public void setSeconds( String value ) {		
		seconds = Utilities.getSecondsFromText(value);		
	}
	
	public void addCountdownFrequency( String value ) {
		countdownFrequencyText.add( value );
		countdownFrequency.add( Utilities.getSecondsFromText(value));
	}
	
	public List<ITask> getCountdown() {
		
		List<ITask> tasks = new ArrayList<ITask>();
		
		if ( !countdownFrequency.isEmpty() ) {
			
			for ( Integer x : countdownFrequency ) {
				
				MessageTask m = new MessageTask();
				m.tag = tag;
				m.setMessage( message );
				m.secStart = x;
				m.secEnd = seconds;
				tasks.add( m );
			}
			
		}
		
		return tasks;
		
	}
	
}
