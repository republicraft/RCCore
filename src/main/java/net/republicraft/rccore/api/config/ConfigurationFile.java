package net.republicraft.rccore.api.config;

import net.republicraft.rccore.api.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public abstract class ConfigurationFile {
	protected File file;
	protected FileConfiguration configuration;
	protected JavaPlugin plugin;
	protected String fileName;
	protected String subDirectory;
	
	public ConfigurationFile(JavaPlugin plugin, String fileName, String subDirectory) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.subDirectory = subDirectory;
		
		initializeFile();
		loadConfiguration();
	}
	
	private void initializeFile() {
		file = new File(plugin.getDataFolder() + (subDirectory.isEmpty() ? "" : File.separator + subDirectory), fileName);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(fileName, false);
		}
	}
	
	private void loadConfiguration() {
		configuration = new YamlConfiguration();
		try {
			configuration.load(file);
		} catch (IOException e) {
			handleIOException(e);
		} catch (InvalidConfigurationException e) {
			handleInvalidConfiguration();
		}
		
		validateSettings();
		loadSettings();
	}
	
	private void handleIOException(IOException e) {
		plugin.getLogger().log(Level.WARNING, "Error loading configuration file " + fileName + ": ", e);
	}
	
	protected void handleInvalidConfiguration() {
		File oldConfigFile = new File(plugin.getDataFolder(), fileName + "-old_" + System.currentTimeMillis() + ".yml");
		if (file.renameTo(oldConfigFile)) {
			plugin.getLogger().log(Level.WARNING, "Invalid configuration in " + fileName + ". Old file renamed, loading defaults.");
			initializeFile();
			loadConfiguration();
		}
	}
	
	public FileConfiguration getConfiguration() {
		return configuration;
	}
	
	public void save() {
		try {
			configuration.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.WARNING, "Error saving configuration file " + fileName + ": ", e);
		}
	}
	
	public void reloadSettings() {
		loadConfiguration();
	}
	
	public abstract void validateSettings();
	
	public abstract void loadSettings();
	
	protected Integer getInt(String path) {
		if (!configuration.isInt(path)) return null;
		return configuration.getInt(path);
	}
	
	protected int getInt(String path, int defaultValue) {
		if (!configuration.isInt(path)) {
			reportInvalidConfiguration(path);
			return defaultValue;
		}
		return configuration.getInt(path);
	}
	
	protected List<Integer> getIntList(String path) {
		if (configuration.getIntegerList(path).isEmpty()) return null;
		return configuration.getIntegerList(path);
	}
	
	protected Double getDouble(String path) {
		if (!configuration.isDouble(path)) return null;
		return configuration.getDouble(path);
	}
	
	protected double getDouble(String path, double defaultValue) {
		if (!configuration.isDouble(path)) {
			reportInvalidConfiguration(path);
			return defaultValue;
		}
		return configuration.getDouble(path);
	}
	
	protected List<Double> getDoubleList(String path) {
		if (configuration.getDoubleList(path).isEmpty()) return null;
		return configuration.getDoubleList(path);
	}
	
	protected Boolean getBoolean(String path) {
		if (!configuration.isBoolean(path)) return null;
		return configuration.getBoolean(path);
	}
	
	protected boolean getBoolean(String path, boolean defaultValue) {
		if (!configuration.isBoolean(path)) {
			reportInvalidConfiguration(path, String.valueOf(defaultValue));
			return defaultValue;
		}
		return configuration.getBoolean(path);
	}
	
	protected String getString(String path) {
		return configuration.getString(path);
	}
	
	protected String getString(String path, String defaultValue) {
		if (!configuration.isString(path)) {
			reportInvalidConfiguration(path, defaultValue);
			return defaultValue;
		}
		return configuration.getString(path);
	}
	
	protected List<String> getStringList(String path) {
		if (configuration.getStringList(path).isEmpty()) return null;
		return configuration.getStringList(path);
	}
	
	protected List<String> getStringList(String path, List<String> defaultValue) {
		if (configuration.getStringList(path).isEmpty()) {
			reportInvalidConfiguration(path);
			return defaultValue;
		}
		return configuration.getStringList(path);
	}
	
	protected ConfigurationSection getConfigurationSection(String path) {
		return configuration.getConfigurationSection(path);
	}
	
	protected void reportInvalidConfiguration(String path) {
		Text.logToConsole(Level.WARNING, "&cInvalid configuration value for &e" + path + "&c in &e" + fileName + "&c. Using default value.");
	}
	
	protected void reportInvalidConfiguration(String path, String defaultValue) {
		Text.logToConsole(Level.WARNING, "&cInvalid configuration value for &e" + path + "&c in &e" + fileName + "&c. Using default value of &e" + defaultValue + "&c.");
	}
	
	protected void logInvalidConfiguration(String message) {
		Text.logToConsole(Level.WARNING, message);
	}
}