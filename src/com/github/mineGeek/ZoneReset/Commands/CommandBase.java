package com.github.mineGeek.ZoneReset.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Data.Zone;
import com.github.mineGeek.ZoneReset.Data.Zones;
import com.github.mineGeek.ZoneReset.Markers.Markers;
import com.github.mineGeek.ZoneReset.Utilities.Config;
import com.github.mineGeek.ZoneReset.Utilities.Utilities;


abstract class CommandBase  implements CommandExecutor {

	protected ZoneReset 	plugin;
	protected String mess 	= null;
	protected CommandSender sender 	= null;
	
	public CommandBase( ZoneReset plugin ) {
		
		this.plugin = plugin;
		
	}
	
	protected Zone getEditZone( Player p ) {
		
		if ( p.hasMetadata("zr") ) {
			
			return (Zone)p.getMetadata("zr").get(0).value();

			
		}
		
		return null;
		
	}
	
	protected void setEditZone( Player p, Zone z ) {
		
		if ( p.hasMetadata("zr") ) p.removeMetadata("zr", this.plugin );	
		p.setMetadata("zr", new FixedMetadataValue( this.plugin, z ) );
	}

	
	protected void saveZone( Player p ) {
		
		Zone z = this.getEditZone(p);
		
		if ( z == null ) {
			this.mess = "You are not currently editing a zone. Start with /zredit zonename";
			return;
		}
		
		Config.saveZoneConfig(z);
		Zones.saveZone( z );
		
		Zones.addZone( z.getTag(), Config.c.getConfigurationSection("zones." + z.getTag() ) );
		Zones.loadInteractKeys();
		Markers.hideZoneBoundaries( p );
		Utilities.clearPlayerMetaData( p );
		Zones.getZone( z.getTag() ).start();
		this.mess = z.getTag() + " has been saved.";		
		
		
	}
	
	protected void cancelEdit( Player p ) {
		Utilities.clearPlayerMetaData(p);
	}
	
	
	protected void copyZone( Player p, String newTag ) {
		Zone z = this.getEditZone( p );
		
		if ( z == null ) {
			p.sendMessage( "You are not currently editing a zone. Start with /zredit zonename" );
			return;
		} else {
			this.setEditZone(p, new Zone(z, newTag));
		}
	}
	
	protected void createZone( Player p, String tag ) {
		
		if ( Zones.getZone(tag) != null ) {
			p.sendMessage( "Zone " + tag + " already exists." );
			return;
		}
		
		Zone z = new Zone();
		z.setTag(tag);
		z.setWorldName( p.getWorld().getName() );
		this.setEditZone(p, z);
		p.sendMessage( tag + " created but not yet saved. type [/zr save] when done editing or [/zr cancel] to exit without saving changes.");
		
	}
	
	protected void setAreaEdit( Player p, boolean on ) {
		
		if ( p.hasMetadata("zra") ) p.removeMetadata("zra", this.plugin );
		
		if ( on ) {
			Zone z = this.getEditZone(p);
			if ( z == null ) {
				p.sendMessage("You have no zone being edited. /zr zoneName to start editing.);");
				return;
			}
			p.setMetadata("zra", new FixedMetadataValue( this.plugin, true ) );
		}
		
	} 
	
	protected boolean getAreaEdit( Player p ) {
		return p.hasMetadata("zra");
	}
	
	protected void setInteractEditOn( Player p ) {
		
		Zone z = this.getEditZone( p );
		if ( z == null ) {
			this.mess = "You have no zone being edited. /zr zonename to start editing.";
			return;
		}
		
		p.setMetadata( "zrinteract", new FixedMetadataValue( this.plugin, true ) );
		
	}
	
	protected void setInteractEditOff( Player p ) {
		if ( p.hasMetadata("zrinteract" ) ) p.removeMetadata("zrinteract", this.plugin );
	}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		this.mess = null;
		this.sender = sender;
		
		Boolean result = exec( cmd.getName().toLowerCase(), args );
		
		if ( mess != null ) {
			
			sender.sendMessage( this.mess );
			
		}
		
		return result;
		
		
	}
	
	protected Boolean exec( String cmdName, String[] args ) {
		
		return false;
	}	
	
	
	
}

