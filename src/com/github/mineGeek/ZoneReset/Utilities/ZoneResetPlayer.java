package com.github.mineGeek.ZoneReset.Utilities;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class ZoneResetPlayer extends BukkitPlayer {

	
	public ZoneResetPlayer(WorldEditPlugin plugin, ServerInterface server,
			Player player) {
		super(plugin, server, player);


	}
	
	
    @Override
    public void print(String msg) { }

	
	

}
