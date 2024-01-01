package net.republicraft.rccore.api;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Core {
	
	public static Plugin getPlugin(JavaPlugin javaPlugin, String pluginName) {
		return javaPlugin.getServer().getPluginManager().getPlugin(pluginName);
	}
	
}
