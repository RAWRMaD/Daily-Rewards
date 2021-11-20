package com.rawrmad.DailyRewards.Managers;

import org.bukkit.entity.Player;

public class CooldownManager {
    public static boolean getAllowRewardip(Player p) {
        long millis;
        String ip = p.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        long current = System.currentTimeMillis();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            millis = MySQLManager.getCooldownIP(ip);
        } else {
            millis = SettingsManager.getData().getLong(String.valueOf(ip) + ".millis");
        }
        return (current > millis);
    }

    public static boolean getAllowRewardUUID(Player p) {
        long millis, current = System.currentTimeMillis();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            millis = MySQLManager.getCooldownUUID(p.getUniqueId());
        } else {
            millis = SettingsManager.getData().getLong(p.getUniqueId() + ".millis");
        }
        return (current > millis);
    }

    public static boolean getAllowStreakUUID(Player p) {
        long current = System.currentTimeMillis();
        long millis = getStreakUUID(p);
        return (current > millis);
    }

    public static boolean getAllowStreakIP(Player p) {
        long current = System.currentTimeMillis();
        long millis = getStreakIP(p);
        return (current > millis);
    }

    public static long getStreakUUID(Player player) {
        return SettingsManager.getData().getLong(player.getUniqueId() + ".reset");
    }

    public static long getStreakIP(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        return SettingsManager.getData().getLong(String.valueOf(ip) + ".reset");
    }

    public static long getTime(Player player) {
        return SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
    }

    public static long getTimeip(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        return SettingsManager.getData().getLong(String.valueOf(ip) + ".millis");
    }

    public static String formatTime(long secs) {
        String str;
        long seconds = secs;
        long minutes = 0L;
        while (seconds >= 60L) {
            seconds -= 60L;
            minutes++;
        }
        long hours = 0L;
        while (minutes >= 60L) {
            minutes -= 60L;
            hours++;
        }
        if (hours != 0L) {
            if (hours > 1L) {
                str = String.valueOf(hours) + " Hours";
            } else if (minutes > 61L) {
                str = String.valueOf(hours) + " Hour " + minutes + " Minutes";
            } else if (minutes == 61L) {
                str = String.valueOf(hours) + " Hour " + minutes + " Minute";
            } else {
                str = String.valueOf(hours) + " Hour";
            }
        } else if (minutes != 0L) {
            if (seconds == 0L) {
                if (minutes == 1L) {
                    str = String.valueOf(minutes) + " Minute";
                } else {
                    str = String.valueOf(minutes) + " Minutes";
                }
            } else if (minutes == 1L) {
                if (seconds == 1L) {
                    str = String.valueOf(minutes) + " Minute " + seconds + " Second";
                } else {
                    str = String.valueOf(minutes) + " Minute " + seconds + " Seconds";
                }
            } else if (seconds == 1L) {
                str = String.valueOf(minutes) + " Minutes " + seconds + " Second";
            } else {
                str = String.valueOf(minutes) + " Minutes " + seconds + " Seconds";
            }
        } else if (seconds == 1L) {
            str = String.valueOf(seconds) + " Second";
        } else {
            str = String.valueOf(seconds) + " Seconds";
        }
        if (secs <= 0L)
            str = "0 Seconds";
        return str;
    }

    public static String getRemainingTime(long millis) {
        long seconds = millis / 1000L;
        return formatTime(seconds);
    }

    public static String getRemainingSec(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        while (minutes > 60L)
            minutes -= 60L;
        return (new StringBuilder(String.valueOf(seconds))).toString();
    }

    public static String getRemainingMin(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        while (minutes > 60L)
            minutes -= 60L;
        return (new StringBuilder(String.valueOf(minutes))).toString();
    }

    public static String getRemainingHour(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        long hours = 0L;
        while (minutes > 60L) {
            minutes -= 60L;
            hours++;
        }
        return (new StringBuilder(String.valueOf(hours))).toString();
    }
}

