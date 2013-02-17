package com.github.mineGeek.ZoneReset.Spawners;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


public class BookSpawn extends ItemSpawn {

	private BookMeta meta;
	
	public BookSpawn() {}
	
	public BookSpawn( ItemStack i ) {
		
		super(i);
		this.meta = (BookMeta) i.getItemMeta();

	}
	
	@Override
	public Map<String, Object> getList() {
		
		Map<String, Object> o = super.getList();
		
		if ( this.meta.hasAuthor() ) {
			o.put("author", this.meta.getAuthor() );
		}
		
		if ( this.meta.hasDisplayName() ) {
			o.put( "display", this.meta.getDisplayName() );
		}
		
		if ( this.meta.hasLore() ) {
			o.put("lore", this.meta.getLore() );
		}
		
		if ( this.meta.hasTitle() ) {
			o.put( "title", this.meta.getTitle() );
		}
		
		if ( this.meta.hasPages() ) {
			
			o.put( "pages", this.meta.getPages() );
			
		}
		
		return o;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setList( Map<String, Object> list ) {
		
		
		super.setList( list );
		this.meta = (BookMeta) super.getAsItemStack().getItemMeta();
		if ( list.containsKey( "author") ) {
			this.meta.setAuthor( (String) list.get("author"));
		}
		
		if ( list.containsKey( "display" ) ) {
			this.meta.setDisplayName( (String) list.get( "display") );
		}
		
		if ( list.containsKey( "lore" ) ) {
			this.meta.setLore( (List<String>) list.get( "lore") );
		}

		if ( list.containsKey( "title" ) ) {
			this.meta.setTitle( (String) list.get( "title") );
		}
		
		if ( list.containsKey( "pages" ) ) {
			this.meta.setPages( (List<String>) list.get( "pages") );
		}

		
		
	}
	
	@Override
	public ItemStack getAsItemStack() {
		
		ItemStack i = super.getAsItemStack();
		i.setItemMeta( this.meta );
		return i;
	}

	
}
