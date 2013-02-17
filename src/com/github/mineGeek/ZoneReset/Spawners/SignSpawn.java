package com.github.mineGeek.ZoneReset.Spawners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignSpawn extends SpawnBase {

	List<String> lines = new ArrayList<String>();
	private Material material;
	
	public SignSpawn( Sign s ) {
		
		this.setType(ZRSPAWNTYPE.SIGN );
		this.setLocation( s.getLocation() );
		this.setData(s.getBlock().getState().getRawData());
		this.material = s.getType();
		this.setLines( s.getLines() );
	}
		
	
	public SignSpawn() {}


	public List<String> getLines() {
		return this.lines;
	}
	
	public void setLines( List<String> lines ) {
		this.lines = lines;
	}
	
	public void clearLines() {
		this.lines.clear();
	}
	
	public void setLines(String[] lines ) {
		this.clearLines();
		
		for ( int x = 0; x < lines.length; x++) {
			this.lines.add(lines[x]);
		}
	}
	
	@Override
	public Map<String, Object> getList() {

		Map<String, Object> r = super.getList( this );
		r.put("lines", this.lines );
		return r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setList( Map<String, Object> list ) {
		
		this.setType( ZRSPAWNTYPE.SIGN );
		super.setList(list);
		if ( list.containsKey("lines") ) {

			this.setLines( (List<String>) list.get("lines") );

		}
		
	}
	
	@Override
	public void spawn() {

		Location l = this.getLocation();
		Block b = l.getWorld().getBlockAt(l);
		Material m = b.getType();
		if ( !b.getType().equals( Material.SIGN ) ) { 
			//b.setType( Material.SIGN );
			//b.setData( this.getData() );
		}
		
		Sign s = (Sign)b.getState();
		
		
		for ( int x = 0; x < s.getLines().length; x++ ) {
			s.setLine(x, this.lines.get(x) );
		}
		
		s.update( true );
		
	
		
	}

	
}
