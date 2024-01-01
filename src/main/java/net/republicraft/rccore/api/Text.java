package net.republicraft.rccore.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {
	
	public static void logToConsole(java.util.logging.Level level, String message) {
		Bukkit.getServer().getLogger().log(level, LegacyTextSerializer.color(message));
	}
	
	public static Component color(String message) {
		return color(message, '&', "#", "");
	}
	
	public static Component color(Component plainText) {
		return color(plainText, '&', "#", "");
	}
	
	public static Component color(String message, char colorChar, String startTag, String endTag) {
		return TextSerializer.legacy(message, colorChar, startTag, endTag);
	}
	
	public static Component color(Component plainText, char colorChar, String startTag, String endTag) {
		return TextSerializer.legacy(TextSerializer.text(plainText), colorChar, startTag, endTag);
	}
	
	public static String plainText(Component component, char colorChar, String startTag, String endTag) {
		return TextSerializer.legacy(component, colorChar, startTag, endTag);
	}
	
	public static String plainText(Component component) {
		return plainText(component, '&', "#", "");
	}
	
	public static Component miniMessage(String message) {
		return TextSerializer.miniMessage(message);
	}
	
	public static String miniMessage(Component message) {
		return TextSerializer.miniMessage(message);
	}
	
	public static boolean containsIgnoreCase(String text, String query) {
		if (text == null || query == null) return false;
		final int length = query.length();
		if (length == 0) return true;
		for (int i = text.length() - length; i >= 0; i--) if (text.regionMatches(true, i, query, 0, length)) return true;
		return false;
	}
	
	public static Component createLinkComponent(@NotNull String message, @NotNull String url, boolean underline) {
		URL targetUrl = isValidUrl(url);
		Component linkComponent = TextSerializer.legacy(message);
		if (targetUrl != null) linkComponent = linkComponent.clickEvent(ClickEvent.openUrl(targetUrl)).asComponent();
		if (underline) return linkComponent.decorate(TextDecoration.UNDERLINED).asComponent();
		return linkComponent;
	}
	
	public static Component createLinkComponent(@NotNull String message, @NotNull String url) {
		return createLinkComponent(message, url, false);
	}
	
	private static URL isValidUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static Component prependComponent(Component original, Component toPrepend) {
		return toPrepend.append(original);
	}
	
	public static Component appendComponent(Component original, Component toAppend) {
		return original.append(toAppend);
	}
	
	private class TextSerializer {
		
		private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
		
		private static @NotNull Component legacy(@NotNull String text, @NotNull char colorCodeChar, @NotNull String hexCodeStart, @NotNull String hexCodeEnd) {
			String convertedText = customHexToAdventure(text, colorCodeChar, hexCodeStart, hexCodeEnd);
			LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
					.character(colorCodeChar)
					.hexColors()
					.build();
			Component deserializedText = serializer.deserialize(convertedText);
			if (deserializedText.hasDecoration(TextDecoration.ITALIC)) return deserializedText;
			return serializer.deserialize(convertedText).decoration(TextDecoration.ITALIC, false);
		}
		
		private static @NotNull String legacy(@NotNull Component component, @NotNull char colorCodeChar, @NotNull String hexCodeStart, @NotNull String hexCodeEnd) {
			LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
					.character(colorCodeChar)
					.hexColors()
					.build();
			String legacyString = serializer.serialize(component);
			return adventureHexToCustom(legacyString, colorCodeChar, hexCodeStart, hexCodeEnd);
		}
		
		private static @NotNull String text(@NotNull Component component) {
			PlainTextComponentSerializer plainTextSerializer = PlainTextComponentSerializer.plainText();
			return plainTextSerializer.serialize(component);
		}
		
		private static @NotNull Component text(@NotNull String text) {
			return Component.text(text);
		}
		
		
		private static String customHexToAdventure(String text, char colorCodeChar, String hexCodeStart, String hexCodeEnd) {
			Pattern pattern = Pattern.compile(Pattern.quote(hexCodeStart) + "([A-Fa-f0-9]{6})" + Pattern.quote(hexCodeEnd));
			Matcher matcher = pattern.matcher(text);
			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String hexColor = matcher.group(1);
				matcher.appendReplacement(sb, colorCodeChar + "#" + hexColor);
			}
			matcher.appendTail(sb);
			return sb.toString();
		}
		
		private static String adventureHexToCustom(String text, char colorCodeChar, String hexCodeStart, String hexCodeEnd) {
			Pattern pattern = Pattern.compile(colorCodeChar + "#([A-Fa-f0-9]{6})");
			Matcher matcher = pattern.matcher(text);
			StringBuilder sb = new StringBuilder();
			while (matcher.find()) {
				String hexColor = matcher.group(1);
				matcher.appendReplacement(sb, hexCodeStart + hexColor + hexCodeEnd);
			}
			matcher.appendTail(sb);
			return sb.toString();
		}
		
		
		private static @NotNull Component legacy(@NotNull String text) {
			return legacy(text, '&', "#", "");
		}
		
		
		private static @NotNull String legacy(@NotNull Component component) {
			return legacy(component, '&', "#", "");
		}
		
		private static @NotNull Component miniMessage(@NotNull String miniMessage) {
			Component deserializedText = MINI_MESSAGE.deserialize(miniMessage);
			if (deserializedText.hasDecoration(TextDecoration.ITALIC)) return deserializedText;
			return MINI_MESSAGE.deserialize(miniMessage).decoration(TextDecoration.ITALIC, false);
		}
		
		private static @NotNull String miniMessage(@NotNull Component component) {
			return MINI_MESSAGE.serialize(component);
		}
		
	}
	
	private class LegacyTextSerializer {
		
		private static String color(String message) {
			return color(message, "&", "#", "");
		}
		
		private static String color(String message, String colorChar) {
			return color(message, colorChar, "#", "");
		}
		
		private static String color(String message, String startTag, String endTag) {
			return color(message, "&", startTag, endTag);
		}
		
		private static String color(String message, String colorChar, String startTag, String endTag) {
			message = Pattern
					.compile(colorChar + "([A-Fa-fK-Ok-oRr0-9])")
					.matcher(message)
					.replaceAll("\u00A7$1");
			
			StringBuilder translatedColor = new StringBuilder();
			Pattern pattern = Pattern.compile(startTag + "([a-fA-F0-9]{6})" + endTag);
			for (Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
				String match = matcher.group(1);
				translatedColor.append("\u00A7x");
				for (char c : match.toCharArray()) translatedColor.append("\u00A7").append(c);
				message = message.substring(0, matcher.start()) + translatedColor + message.substring(matcher.end());
				translatedColor.delete(0, translatedColor.length());
			}
			return message;
		}
		
		private static String uncolor(String message) {
			return uncolor(message, "&", "#", "");
		}
		
		private static String uncolor(String message, String colorChar, String startTag, String endTag) {
			StringBuilder translatedColor = new StringBuilder();
			Pattern hexColors = Pattern.compile("§x((§[A-Fa-f0-9]){6})");
			for (Matcher matcher = hexColors.matcher(message); matcher.find(); matcher = hexColors.matcher(message)) {
				String match = matcher.group();
				translatedColor.append("#").append(match.replace("x", "").replace("\u00A7", ""));
				message = message.substring(0, matcher.start()) + translatedColor + message.substring(matcher.end());
				translatedColor.delete(0, translatedColor.length());
			}
			message = Pattern
					.compile("\u00A7([A-Fa-fK-Ok-oRr0-9])")
					.matcher(message)
					.replaceAll("&$1");
			return message;
		}
		
	}
	
	
	
}
