package com.github.mineGeek.ZoneReset.Tasks;

import java.util.ArrayList;
import java.util.List;


public class Tasks {

	public List<ITask> tasks = new ArrayList<ITask>();
	@SuppressWarnings("unused")
	private Integer resume = null;
	private Long lastStart = null;
	
	public void add( List<ITask> tasks ) {
		
		for ( ITask t : tasks ) add( t );
	}
	
	public void add( ITask task ) {
		tasks.add(task);
	}
	
	public void setResume( Integer i ) {
		resume = i;
		for ( ITask t : tasks ) t.setResume( i );
	}
	
	public Integer getResume() {
		
		//Are any tasks running?
		Integer m = null;
		boolean yes = false;
		for ( ITask t : tasks ) {
			
			if ( t.isRunning() ) {
				yes = true;
				break;
			}
			
		}
		
		if ( lastStart != null && yes ) {
			
			m = (int)( System.currentTimeMillis() - lastStart ) / 1000;
			
		}
		
		/*
		Integer m = 0;
		Integer o = null;
		
		for ( ITask t : tasks ) {
			o = t.getResume();
			if ( o != null && o < m ) {
				m = 0;
			}
		}
		*/
		return m;
	}
	
	public void start() {
		
		for ( ITask x : tasks ) x.start();
		this.lastStart = System.currentTimeMillis();
		
	}
	
	public void stop() {
		for ( ITask x : tasks ) x.stop();
	}
	
	
	public void restart() {
		for ( ITask x : tasks ) x.restart();
		this.lastStart = System.currentTimeMillis();
	}
	
	public void clear() {
		stop();
		tasks.clear();
	}
	
	
}
