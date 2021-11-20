package com.rawrmad.DailyRewards.Commands;

import com.rawrmad.DailyRewards.Main.Main;
import com.rawrmad.DailyRewards.Managers.CooldownManager;
import com.rawrmad.DailyRewards.Managers.MySQLManager;
import com.rawrmad.DailyRewards.Managers.RewardManager;
import com.rawrmad.DailyRewards.Managers.SettingsManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("reward"))
            onCommand(player);
        return true;
    }

    public static void onCommand(Player player) {
        if (player.hasPermission("dr.claim")) {
            String ip = player.getAddress().getAddress().getHostAddress();
            ip = ip.replace(".", "-");
            if (SettingsManager.getConfig().getBoolean("savetoip")) {
                if (!CooldownManager.getAllowRewardip(player)) {
                    long releaseip;
                    String norewards = SettingsManager.getMsg().getString("no-rewards");
                    if (!norewards.equalsIgnoreCase("")) {
                        if (Main.papi)
                            norewards = PlaceholderAPI.setPlaceholders(player, norewards);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', norewards));
                    }
                    long current = System.currentTimeMillis();
                    if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                        releaseip = MySQLManager.getCooldownIP(ip);
                    } else {
                        releaseip = SettingsManager.getData().getLong(String.valueOf(ip) + ".millis");
                    }
                    long millis = releaseip - current;
                    String cdmsg = SettingsManager.getMsg().getString("cooldown-msg");
                    cdmsg = cdmsg.replace("%time%", CooldownManager.getRemainingTime(millis));
                    cdmsg = cdmsg.replace("%s%", CooldownManager.getRemainingSec(millis));
                    cdmsg = cdmsg.replace("%m%", CooldownManager.getRemainingMin(millis));
                    cdmsg = cdmsg.replace("%h%", CooldownManager.getRemainingHour(millis));
                    cdmsg = cdmsg.replace("%time", CooldownManager.getRemainingTime(millis));
                    cdmsg = cdmsg.replace("%s", CooldownManager.getRemainingSec(millis));
                    cdmsg = cdmsg.replace("%m", CooldownManager.getRemainingMin(millis));
                    cdmsg = cdmsg.replace("%h", CooldownManager.getRemainingHour(millis));
                    if (!cdmsg.equalsIgnoreCase("")) {
                        if (Main.papi)
                            cdmsg = PlaceholderAPI.setPlaceholders(player, cdmsg);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', cdmsg));
                    }
                    RewardManager.noReward(player);
                } else {
                    RewardManager.setReward(player);
                }
            } else if (!CooldownManager.getAllowRewardUUID(player)) {
                long releaseip;
                String norewards = SettingsManager.getMsg().getString("no-rewards");
                if (!norewards.equalsIgnoreCase("")) {
                    if (Main.papi)
                        norewards = PlaceholderAPI.setPlaceholders(player, norewards);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', norewards));
                }
                long current = System.currentTimeMillis();
                if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                    releaseip = MySQLManager.getCooldownUUID(player.getUniqueId());
                } else {
                    releaseip = SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
                }
                long millis = releaseip - current;
                String cdmsg = SettingsManager.getMsg().getString("cooldown-msg");
                cdmsg = cdmsg.replace("%time%", CooldownManager.getRemainingTime(millis));
                cdmsg = cdmsg.replace("%s%", CooldownManager.getRemainingSec(millis));
                cdmsg = cdmsg.replace("%m%", CooldownManager.getRemainingMin(millis));
                cdmsg = cdmsg.replace("%h%", CooldownManager.getRemainingHour(millis));
                cdmsg = cdmsg.replace("%time", CooldownManager.getRemainingTime(millis));
                cdmsg = cdmsg.replace("%s", CooldownManager.getRemainingSec(millis));
                cdmsg = cdmsg.replace("%m", CooldownManager.getRemainingMin(millis));
                cdmsg = cdmsg.replace("%h", CooldownManager.getRemainingHour(millis));
                if (!cdmsg.equalsIgnoreCase("")) {
                    if (Main.papi)
                        cdmsg = PlaceholderAPI.setPlaceholders(player, cdmsg);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cdmsg));
                }
                RewardManager.noReward(player);
            } else {
                RewardManager.setReward(player);
            }
        } else {
            String msg = SettingsManager.getMsg().getString("no-permission");
            if (!msg.equalsIgnoreCase("")) {
                if (Main.papi)
                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                msg = msg.replace("%player%", player.getName());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }
    }
}

