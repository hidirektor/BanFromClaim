package no.vestlandetmc.BanFromClaim.handler;

import java.util.ArrayList;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import no.vestlandetmc.BanFromClaim.BfcPlugin;

public class MessageHandler {

	public static ArrayList<String> spamMessageClaim = new ArrayList<>();

	public static void sendAction(Player player, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendTitle(Player player, String title, String subtitle) {
		player.sendTitle(colorize(title), colorize(subtitle));
	}

	public static void sendMessage(Player player, String message) {
		player.sendMessage(colorize(message));
	}

	public static void sendConsole(String message) {
		BfcPlugin.getInstance().getServer().getConsoleSender().sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String placeholders(String message, String time, String tpName, String tp1, String tp2) {
		final String converted = message.
				replaceAll("%time%", time).
				replaceAll("%tpname%", tpName).
				replaceAll("%teleport1%", tp1).
				replaceAll("%teleport2%", tp2);

		return converted;

	}

}
