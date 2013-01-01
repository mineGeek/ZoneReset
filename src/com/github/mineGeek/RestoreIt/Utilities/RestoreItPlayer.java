package com.github.mineGeek.RestoreIt.Utilities;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class RestoreItPlayer extends BukkitPlayer {

	private Player player;
	
	public RestoreItPlayer(WorldEditPlugin plugin, ServerInterface server,
			Player player) {
		super(plugin, server, player);
		this.player = player;
		// TODO Auto-generated constructor stub
	}
	
	
    @Override
    public void print(String msg) { }

    @Override
    public void printDebug(String msg) {
        for (String part : msg.split("\n")) {
            player.sendMessage("\u00A77" + part);
        }
    }

    @Override
    public void printError(String msg) {
        for (String part : msg.split("\n")) {
            player.sendMessage("\u00A7c" + part);
        }
    }	
	

}
