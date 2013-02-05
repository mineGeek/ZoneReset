package com.github.mineGeek.ZoneReset.Utilities;


import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;


/**
 * Utility wrapper for configuration
 *
 */
public class Config {


	
	/**
	 * The actual config file from getConfig()
	 */
	public static FileConfiguration c;
	
	/**
	 * Text to display to player when there are players still in the zone
	 */
	public static String txtPlayersStillInArea = "There are still players in the area";
	

	/**
	 * Timeout of restriction messages to prevent flooding player
	 */
	public static int spamPlayerMessageTimeout = 1500;
	
	
	/**
	 * Debugging parameters
	 */
	public static boolean debug_area_chunkEntrance = false;
	public static boolean debug_area_chunkExit = false;
	public static boolean debug_area_chunkChange = false;
	
	
	/**
	 * Loads all areaRules from the config
	 * @param c
	 */
	public static void loadAreaRulesFromConfig( MemorySection c ) {
		
		if ( c.contains("areaRules") ) {
			for ( String x : c.getConfigurationSection("areaRules").getKeys( false ) ) {
				AreaRules.addRule(x, c.getConfigurationSection("areaRules." + x) );
			}
		}
		
		/**
		 * DO not monitor player movement unless we 
		 * have area rules active.
		 */
		if ( !AreaRules.activeChunks.isEmpty() ) {
			Config.monitorPlayerLocations = true;			
		}
		
	}
	
	
	
	/**
	 * Load all rules from the config
	 * @param c
	 */
	public static void loadRulesFromConfig( MemorySection c) {
		
		/**
		 * Load aliases
		 */
		if ( c.contains("aliases") ) {
			
			for ( String x : c.getConfigurationSection("aliases").getKeys( false ) ) {
				
				Rules.addItemAlias( x, c.getStringList("aliases." + x) );
				
			}
			
		}
		
		/**
		 * Load automatically applied rules
		 */
		if ( c.contains("rules") ) {
			
			for ( String x : c.getConfigurationSection( "rules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection( "rules." + x ) );
				
			}
			
		}
		
		/**
		 * Load manually applied rules
		 */
		if ( c.contains("manualRules") ) {
			
			for ( String x : c.getConfigurationSection( "manualRules").getKeys( false ) ) {
				
				Rules.addRule( x, c.getConfigurationSection("manualRules." + x), true );
				
			}
			
		}		
		
		Config.server().getLogger().info("ItemRules loaded " + Rules.count() + " rules total.");
		
	}
	
	
	
	
	/**
	 * Load other variables from config
	 */
	public static void loadConfig() {
		
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.levelIncreasesItemLevel", true );
		Config.XPLossReducesItemLevel 		= c.getBoolean("XP.levelDecreasesItemLevel", true );
		Config.XPLevelIncreasesItemLevel 	= c.getBoolean("XP.itemLevelDefaultsToXPLevel", true );
		Config.txtCanDoPrefix				= c.getString("text.CanDoPrefix", "Not restricted: ");
		Config.txtCanDoNextPrefix			= c.getString("text.CanDoNextPrefix", "Next rules change: ");
		Config.txtCannotDoPrefix			= c.getString("text.CannotDoPrefix", "Restricted: ");
		Config.txtCanNoLongerDo				= c.getString("text.CanNoLongerDo", "You can no longer ");
		Config.txtCanNowDo					= c.getString("text.CanNowDo", "You can now ");
		Config.txtDefaultRestrictedMessage	= c.getString("text.DefaultRestrictedMessage", "You cannot do that");
		Config.txtDefaultUnrestrictedMessage= c.getString("text.DefaultUnrestrictedMessage", "");
		Config.txtNoRules					= c.getString("text.NoRulesToDisplay", "You have no rules applied to you.");
		Config.txtNoChangesToDisplay		= c.getString("text.NoChangesToYourRules", "There are no changes to your rules.");
		Config.spamPlayerMessageTimeout		= c.getInt( "playerMessageTimeout", 1500 );
		
		Config.debug_area_chunkEntrance		= c.getBoolean("debug.area.chunkEntrance", false);
		Config.debug_area_chunkExit			= c.getBoolean("debug.area.chunkExit", false);
		Config.debug_area_chunkChange		= c.getBoolean("debug.area.chunkChange", false);
		
	}
	
	/**
	 * Good guy brings closure
	 */
	public static void close() {
		Config.c = null;
		
	}
	
	
}
