package no.vestlandetmc.BanFromClaim.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import no.vestlandetmc.BanFromClaim.config.ClaimData;
import no.vestlandetmc.BanFromClaim.config.Messages;
import no.vestlandetmc.BanFromClaim.handler.MessageHandler;

public class BfcCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			MessageHandler.sendConsole("&cThis command can only be used in-game.");
			return true;
		}

		final Player player = (Player) sender;
		final Location loc = player.getLocation();
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);

		if(args.length == 0) {
			MessageHandler.sendMessage(player, Messages.NO_ARGUMENTS);
			return true;
		}

		if(claim == null) {
			MessageHandler.sendMessage(player, Messages.OUTSIDE_CLAIM);
			return true;
		}

		final Player bannedPlayer = Bukkit.getPlayer(args[0]);
		final String accessDenied = claim.allowGrantPermission(player);
		boolean allowBan = false;

		if(accessDenied == null) { allowBan = true; }
		if(player.hasPermission("bfc.admin")) { allowBan = true; }

		if(bannedPlayer == null) {
			MessageHandler.sendMessage(player, Messages.placeholders(Messages.UNVALID_PLAYERNAME, args[0], player.getDisplayName(), null));
			return true;
		} else {
			if(bannedPlayer == player) {
				MessageHandler.sendMessage(player, Messages.BAN_SELF);
				return true;
			}
		}

		if(bannedPlayer.hasPermission("bfc.bypass")) {
			MessageHandler.sendMessage(player, Messages.placeholders(Messages.PROTECTED, bannedPlayer.getDisplayName(), null, null));
			return true;
		}

		if(!allowBan) {
			MessageHandler.sendMessage(player, Messages.NO_ACCESS);
			return true;
		} else {
			final String claimOwner = claim.getOwnerName();

			if(setClaimData(player, claim.getID().toString(), bannedPlayer.getUniqueId().toString(), true)) {
				if(GriefPrevention.instance.dataStore.getClaimAt(bannedPlayer.getLocation(), true, claim) != null) {
					if(GriefPrevention.instance.dataStore.getClaimAt(bannedPlayer.getLocation(), true, claim) == claim) {
						GriefPrevention.instance.ejectPlayer(bannedPlayer);
					}
				}
				MessageHandler.sendMessage(player, Messages.placeholders(Messages.BANNED, bannedPlayer.getDisplayName(), null, null));
				MessageHandler.sendMessage(bannedPlayer, Messages.placeholders(Messages.BANNED_TARGET, bannedPlayer.getDisplayName(), player.getDisplayName(), claimOwner));
			} else {
				MessageHandler.sendMessage(player, Messages.ALREADY_BANNED);
			}

		}
		return true;
	}

	private boolean setClaimData(Player player, String claimID, String bannedUUID, boolean add) {
		final ClaimData claimData = new ClaimData();

		return claimData.setClaimData(player, claimID, bannedUUID, add);
	}

}
