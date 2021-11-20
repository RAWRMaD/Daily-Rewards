package com.rawrmad.DailyRewards.Managers;

import com.rawrmad.DailyRewards.Main.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIExtensions extends PlaceholderExpansion {
    static Main plugin = (Main) Main.getPlugin(Main.class);

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "dailyrewards";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        long releaseip;
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        long current = System.currentTimeMillis();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            if (!SettingsManager.getConfig().getBoolean("savetoip")) {
                releaseip = MySQLManager.getCooldownUUID(player.getUniqueId());
            } else {
                releaseip = MySQLManager.getCooldownIP(ip);
            }
        } else if (!SettingsManager.getConfig().getBoolean("savetoip")) {
            releaseip = SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
        } else {
            releaseip = SettingsManager.getData().getLong(String.valueOf(ip) + ".millis");
        }
        long millis = releaseip - current;
        if (identifier.equals("remaining_time"))
            return CooldownManager.getRemainingTime(millis);
        if (identifier.equals("remaining_hours"))
            return CooldownManager.getRemainingHour(millis);
        if (identifier.equals("remaining_minutes"))
            return CooldownManager.getRemainingMin(millis);
        if (identifier.equals("remaining_seconds"))
            return CooldownManager.getRemainingSec(millis);
        if (identifier.equals("player_test_qualification")) {
            boolean output;
            if (!SettingsManager.getConfig().getBoolean("savetoip")) {
                output = CooldownManager.getAllowRewardip(player);
            } else {
                output = CooldownManager.getAllowRewardUUID(player);
            }
            if (output)
                return SettingsManager.getMsg().getString("PlaceholderAPI.reward_available");
            return SettingsManager.getMsg().getString("PlaceholderAPI.no_rewards");
        }
        if (identifier.equals("player_reward_available"))
            return SettingsManager.getMsg().getString("PlaceholderAPI.reward_available");
        if (identifier.equals("player_no_rewards"))
            return SettingsManager.getMsg().getString("PlaceholderAPI.no_rewards");
        return null;
    }
}

