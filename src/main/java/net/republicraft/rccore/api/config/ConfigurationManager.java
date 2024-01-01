package net.republicraft.rccore.api.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
	private final List<ConfigurationFile> configurationFiles = new ArrayList<>();
	private final JavaPlugin plugin;
	
	public ConfigurationManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean registerConfiguration(ConfigurationFile configurationFile) {
		return configurationFiles.add(configurationFile);
	}
	
	public List<ConfigurationFile> getConfigurationFiles() {
		return configurationFiles;
	}
	
	public void saveAll() {
		configurationFiles.forEach(ConfigurationFile::save);
	}
	
	public void reloadAll() {
		configurationFiles.forEach(ConfigurationFile::reloadSettings);
	}
}