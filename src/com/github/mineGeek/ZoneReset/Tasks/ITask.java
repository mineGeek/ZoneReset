package com.github.mineGeek.ZoneReset.Tasks;

public interface ITask {

	public void start();
	public void stop();
	public void restart();
	public void setResume( Integer i );
	public Integer getResume();
	
}
