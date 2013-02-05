package com.github.mineGeek.ZoneReset.Commands;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.mineGeek.ZoneReset.ZoneReset;
import com.github.mineGeek.ZoneReset.Utilities.RestoreWESnapshot;

public class Reset extends CommandBase {

	public Reset(ZoneReset plugin) {
		super(plugin);
	}

	@Override
	protected Boolean exec( String cmdName, String[] args ) {
		
		
		Player player = (Player)this.sender;
		
		
		RestoreWESnapshot restore = new RestoreWESnapshot( player, player.getWorld());
		Location l1 = new Location( player.getWorld(), -157, 82, 288);
		Location l2 = new Location( player.getWorld(), -126, 67, 322);
		return restore.restore(l1, l2);
		
		
	}
	
	
}
