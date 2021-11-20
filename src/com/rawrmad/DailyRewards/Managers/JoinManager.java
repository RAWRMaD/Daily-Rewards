package com.rawrmad.DailyRewards.Managers;

import com.rawrmad.DailyRewards.Main.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinManager implements Listener {
    static Main plugin = (Main) Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled"))
            MySQLManager.createPlayer(player);
        (new BukkitRunnable() {
            public void run() {
            	if (player.getName().equalsIgnoreCase("rawrmad"))
                    player.sendMessage(ChatColor.GREEN + "Hey that's cool, they use DailyRewards! :)"
                            + JoinManager.plugin.getDescription().getVersion());
                if (player.getName().equalsIgnoreCase("halflove"))
                    player.sendMessage(ChatColor.GREEN + "DailyRewards misses you Halflove! :)"
                            + JoinManager.plugin.getDescription().getVersion());
                if(player.isOp()) {
                    new UpdateChecker(plugin, 16708).getLatestVersion(version -> {
                        if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                            player.sendMessage(ChatColor.GOLD + "*** Daily Rewards is Outdated! ***");
                            player.sendMessage(ChatColor.YELLOW + "You're on " + ChatColor.WHITE + plugin.getDescription().getVersion() + ChatColor.YELLOW + " while " + ChatColor.WHITE + version + ChatColor.YELLOW + " is available!");
                            player.sendMessage(ChatColor.YELLOW + "Update Here: " + ChatColor.WHITE + "https://bit.ly/3x2Ma4S");
                        }
                    });
                }
            }
        }).runTaskLater((Plugin) plugin, 50L);
        if (SettingsManager.getConfig().getBoolean("loginclaim.enabled") && player.hasPermission("dr.claim")) {
            (new BukkitRunnable() {
                public void run() {
                    if (player.hasPermission("dr.claim")) {
                        String ip = player.getAddress().getAddress().getHostAddress();
                        ip = ip.replace(".", "-");
                        if (SettingsManager.getConfig().getBoolean("savetoip")) {
                            if (!CooldownManager.getAllowRewardip(player)) {
                                long releaseip;
                                String noreward = SettingsManager.getMsg().getString("no-rewards");
                                if (!noreward.equalsIgnoreCase("")) {
                                    if (Main.papi)
                                        noreward = PlaceholderAPI.setPlaceholders(player, noreward);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', noreward));
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
                            String noreward = SettingsManager.getMsg().getString("no-rewards");
                            if (!noreward.equalsIgnoreCase("")) {
                                if (Main.papi)
                                    noreward = PlaceholderAPI.setPlaceholders(player, noreward);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noreward));
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
                        msg = msg.replace("%player%", player.getName());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                    }
                }
            }).runTaskLater((Plugin) plugin, SettingsManager.getConfig().getInt("loginclaim.delay"));
        } else if (player.hasPermission("dr.claim")
                && (CooldownManager.getAllowRewardip(player) || CooldownManager.getAllowRewardUUID(player))) {
            (new BukkitRunnable() {
                public void run() {
                    String available = SettingsManager.getMsg().getString("reward-available");
                    if (!available.equals("")) {
                        if (Main.papi)
                            available = PlaceholderAPI.setPlaceholders(player, available);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', available));
                    }
                }
            }).runTaskLater((Plugin) plugin, 50L);
        }
    }
}
