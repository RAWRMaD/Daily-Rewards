package com.rawrmad.DailyRewards.Managers;

import com.google.common.base.Splitter;
import com.rawrmad.DailyRewards.Main.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RewardManager {
    private static Random r = new Random();

    static Main plugin = (Main) Main.getPlugin(Main.class);

    public static void noReward(Player player) {
        String sound = SettingsManager.getConfig().getString("noreward.sound.type");
        int volume = SettingsManager.getConfig().getInt("noreward.sound.volume");
        int pitch = SettingsManager.getConfig().getInt("noreward.sound.pitch");
        if (SettingsManager.getConfig().getBoolean("noreward.sound.enabled"))
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
    }

    public static void setReward(final Player player) {
        String sound = SettingsManager.getConfig().getString("claim.sound.type");
        int volume = SettingsManager.getConfig().getInt("claim.sound.volume");
        int pitch = SettingsManager.getConfig().getInt("claim.sound.pitch");
        if (SettingsManager.getConfig().getBoolean("claim.sound.enabled"))
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        for (String prize : SettingsManager.getConfig().getConfigurationSection("rewards").getKeys(false)) {
            String ip = player.getAddress().getAddress().getHostAddress();
            ip = ip.replace(".", "-");
            long toSet = Math.abs(System.currentTimeMillis())
                    + Math.abs(SettingsManager.getConfig().getInt("cooldown"));
            SettingsManager.getData().set(String.valueOf(ip) + ".millis", Long.valueOf(toSet));
            SettingsManager.getData().set(player.getUniqueId() + ".millis", Long.valueOf(toSet));
            if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                MySQLManager.updateCooldownIP(ip, Long.valueOf(toSet).longValue());
                MySQLManager.updateCooldownUUID(player.getUniqueId(), Long.valueOf(toSet).longValue());
            }
            SettingsManager.saveData();
            if (!SettingsManager.getConfig().getBoolean("rewards." + prize + ".permission")) {
                String claim = SettingsManager.getConfig().getString("rewards." + prize + ".claim-message");
                if (!claim.equalsIgnoreCase("")) {
                    if (Main.papi)
                        claim = PlaceholderAPI.setPlaceholders(player, claim);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', claim));
                }
                if (!SettingsManager.getConfig().getString("rewards." + prize + ".broadcast").equalsIgnoreCase("")) {
                    String msg = SettingsManager.getConfig().getString("rewards." + prize + ".broadcast");
                    msg = msg.replace("%player%", player.getName());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                (new BukkitRunnable() {
                    public void run() {
                        if (SettingsManager.getConfig().getBoolean("rewards." + prize + ".random")) {
                            List<String> commandList = SettingsManager.getConfig()
                                    .getStringList("rewards." + prize + ".commands");
                            int index = RewardManager.r.nextInt(commandList.size());
                            String selectedCommand = commandList.get(index);
                            selectedCommand = selectedCommand.replace("%player%", player.getName());
                            if (Main.papi)
                                selectedCommand = PlaceholderAPI.setPlaceholders(player, selectedCommand);
                            if (selectedCommand.contains(";")) {
                                List<String> split = Splitter.on(";").splitToList(selectedCommand);
                                for (String finalcommand : split)
                                    Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), finalcommand);
                            } else {
                                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), selectedCommand);
                            }
                        } else {
                            Iterator<String> iterator = SettingsManager.getConfig()
                                    .getStringList("rewards." + prize + ".commands").iterator();
                            while (iterator.hasNext()) {
                                String selectedCommand = iterator.next();
                                selectedCommand = selectedCommand.replace("%player%", player.getName());
                                if (Main.papi)
                                    selectedCommand = PlaceholderAPI.setPlaceholders(player, selectedCommand);
                                if (selectedCommand.contains(";")) {
                                    List<String> split = Splitter.on(";").splitToList(selectedCommand);
                                    for (String finalcommand : split)
                                        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), finalcommand);
                                    continue;
                                }
                                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), selectedCommand);
                            }
                        }
                    }
                }).runTaskLater((Plugin) plugin, 3L);
                continue;
            }
            if (player.hasPermission("dr." + SettingsManager.getConfig().getString("rewards." + prize + ".name"))) {
                String claim = SettingsManager.getConfig().getString("rewards." + prize + ".claim-message");
                if (!claim.equalsIgnoreCase("")) {
                    if (Main.papi)
                        claim = PlaceholderAPI.setPlaceholders(player, claim);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', claim));
                }
                if (!SettingsManager.getConfig().getString("rewards." + prize + ".broadcast").equalsIgnoreCase("")) {
                    String msg = SettingsManager.getConfig().getString("rewards." + prize + ".broadcast");
                    msg = msg.replace("%player%", player.getName());
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
                }
                (new BukkitRunnable() {
                    public void run() {
                        if (SettingsManager.getConfig().getBoolean("rewards." + prize + ".random")) {
                            List<String> commandList = SettingsManager.getConfig()
                                    .getStringList("rewards." + prize + ".commands");
                            int index = RewardManager.r.nextInt(commandList.size());
                            String selectedCommand = commandList.get(index);
                            selectedCommand = selectedCommand.replace("%player%", player.getName());
                            if (Main.papi)
                                selectedCommand = PlaceholderAPI.setPlaceholders(player, selectedCommand);
                            if (selectedCommand.contains(";")) {
                                List<String> split = Splitter.on(";").splitToList(selectedCommand);
                                for (String finalcommand : split)
                                    Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), finalcommand);
                            } else {
                                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), selectedCommand);
                            }
                        } else {
                            Iterator<String> iterator = SettingsManager.getConfig()
                                    .getStringList("rewards." + prize + ".commands").iterator();
                            while (iterator.hasNext()) {
                                String selectedCommand = iterator.next();
                                selectedCommand = selectedCommand.replace("%player%", player.getName());
                                if (Main.papi)
                                    selectedCommand = PlaceholderAPI.setPlaceholders(player, selectedCommand);
                                if (selectedCommand.contains(";")) {
                                    List<String> split = Splitter.on(";").splitToList(selectedCommand);
                                    for (String finalcommand : split)
                                        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), finalcommand);
                                    continue;
                                }
                                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), selectedCommand);
                            }
                        }
                    }
                }).runTaskLater((Plugin) plugin, 3L);
            }
        }
    }
}

