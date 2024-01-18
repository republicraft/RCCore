package net.republicraft.rccore.api;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Command implements TabExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
		if (commandSender instanceof Player player) playerExecution(player, command, label, args);
		else if (commandSender instanceof ConsoleCommandSender console) consoleExecution(console, command, label, args);
		else handleOtherCommandSender(commandSender, command, label, args);
		return true;
	}
	
	public abstract void playerExecution(@NotNull Player commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
	
	public abstract void consoleExecution(@NotNull ConsoleCommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
	
	public abstract void handleOtherCommandSender(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
		if (commandSender instanceof Player player) return playerTabCompletion(player, command, label, args);
		else if (commandSender instanceof ConsoleCommandSender console) return consoleTabCompletion(console, command, label, args);
		else return handleOtherTabCompletion(commandSender, command, label, args);
	}
	
	public abstract List<String> playerTabCompletion(@NotNull Player commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
	
	public abstract List<String> consoleTabCompletion(@NotNull ConsoleCommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
	
	public abstract List<String> handleOtherTabCompletion(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
}
