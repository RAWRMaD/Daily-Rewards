package com.rawrmad.DailyRewards.Managers;

import com.rawrmad.DailyRewards.Main.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandManager {
    public void adminHelp(Player player) {
        player.sendMessage(ChatColor.BOLD + "DailyRewards Admin Help");
        player.sendMessage(
                ChatColor.YELLOW + "/dr reload" + ChatColor.WHITE + ChatColor.ITALIC + " Reload all DR files.");
        player.sendMessage(
                ChatColor.YELLOW + "/dr reset" + ChatColor.WHITE + ChatColor.ITALIC + " Reset your cooldown.");
        player.sendMessage(ChatColor.YELLOW + "/dr reset (player)" + ChatColor.WHITE + ChatColor.ITALIC
                + " Reset a player's cooldown.");
    }

    public void playerHelp(Player player) {
        String msg = SettingsManager.getMsg().getString("player-help");
        if (msg.equalsIgnoreCase("")) {
            if (Main.papi)
                msg = PlaceholderAPI.setPlaceholders(player, msg);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
}

