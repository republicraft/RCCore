package net.republicraft.rccore;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class RCCore extends JavaPlugin {
	
	@Override
	public void onEnable() {
		// Plugin startup logic
		this.getLogger().log(Level.INFO, "Hello world! We are online!");
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
		this.getLogger().log(Level.INFO, "Disabling RCCore!");
	}
}
