package com.rawrmad.DailyRewards.Managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    static SettingsManager instance = new SettingsManager();

    static Plugin p;

    static FileConfiguration config;

    static File cfile;

    static FileConfiguration data;

    static File dfile;

    static FileConfiguration msg;

    static File mfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        cfile = new File(p.getDataFolder(), "config.yml");
        config = p.getConfig();
        config.options().copyDefaults(true);
        config.addDefault("cooldown", Integer.valueOf(86400000));
        config.addDefault("savetoip", Boolean.valueOf(false));
        config.addDefault("regenerate-default-rewards", Boolean.valueOf(true));
        config.addDefault("mysql.enabled", Boolean.valueOf(false));
        config.addDefault("mysql.host-name", "localhost");
        config.addDefault("mysql.port", Integer.valueOf(3306));
        config.addDefault("mysql.database", "example");
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");
        config.addDefault("loginclaim.enabled", Boolean.valueOf(false));
        config.addDefault("loginclaim.delay", Integer.valueOf(3));
        config.addDefault("claim.sound", "");
        config.addDefault("claim.sound.enabled", Boolean.valueOf(true));
        config.addDefault("claim.sound.type", "ENTITY_PLAYER_LEVELUP");
        config.addDefault("claim.sound.volume", Integer.valueOf(1));
        config.addDefault("claim.sound.pitch", Integer.valueOf(1));
        config.addDefault("noreward.sound", "");
        config.addDefault("noreward.sound.enabled", Boolean.valueOf(true));
        config.addDefault("noreward.sound.type", "BLOCK_ANVIL_LAND");
        config.addDefault("noreward.sound.volume", Integer.valueOf(1));
        config.addDefault("noreward.sound.pitch", Integer.valueOf(1));
        List<String> command = new ArrayList<>();
        command.add("give %player minecraft:diamond 1");
        List<String> bworld = new ArrayList<>();
        
        String createDaily = SettingsManager.getConfig().getString("regenerate-default-rewards");
        if(createDaily.equalsIgnoreCase("true")) {
			bworld.add("example_world");
        	bworld.add("example_world2");
        	config.addDefault("rewards.basic.name", "Basic");
        	config.addDefault("rewards.basic.permission", Boolean.valueOf(false));
        	config.addDefault("rewards.basic.random", Boolean.valueOf(false));
        	config.addDefault("rewards.basic.claim-message", "&a&lDaily &2&l>> &aYou claimed the &7Basic&f Daily Reward!");
        	config.addDefault("rewards.basic.broadcast", "");
        	config.addDefault("rewards.basic.commands", command);
        	List<String> command2 = new ArrayList<>();
        	command2.add("give %player% minecraft:diamond 1;say %player% earned a common diamond");
        	command2.add("give %player% minecraft:diamond 1;say %player% earned a common diamond");
        	command2.add("give %player% minecraft:emerald 1;say %player% earned a rare emerald");
        	List<String> bworld2 = new ArrayList<>();
        	bworld2.add("example_world3");
        	bworld2.add("example_world4");
        	config.addDefault("rewards.advanced.name", "Advanced");
        	config.addDefault("rewards.advanced.permission", Boolean.valueOf(true));
        	config.addDefault("rewards.advanced.random", Boolean.valueOf(true));
        	config.addDefault("rewards.advanced.claim-message", "");
        	config.addDefault("rewards.advanced.broadcast", "&a&lDaily &2&l>> &a%player% claimed the &eAdvanced&f Daily Reward!");
        	config.addDefault("rewards.advanced.commands", command2);
		}
        saveConfig();
        if (!p.getDataFolder().exists())
            p.getDataFolder().mkdir();
        dfile = new File(p.getDataFolder(), "data.yml");
        if (!dfile.exists())
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
            }
        data = (FileConfiguration) YamlConfiguration.loadConfiguration(dfile);
        mfile = new File(p.getDataFolder(), "messages.yml");
        if (!mfile.exists())
            try {
                mfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create messages.yml!");
            }
        msg = (FileConfiguration) YamlConfiguration.loadConfiguration(mfile);
        msg.options().copyDefaults(true);
        msg.addDefault("no-rewards", "&a&lDaily &2&l>> &fYou do not have any rewards at the moment.");
        msg.addDefault("cooldown-msg", "&a&lDaily &2&l>> &fTime until next reward: %time%");
        msg.addDefault("no-permission", "&a&lDaily &2&l>> &fYou do not have permission to do ");
        msg.addDefault("reward-available", "&a&lDaily &2&l>> &fYou have unclaimed rewards, do &e/reward &fto claim!");
        msg.addDefault("PlaceholderAPI.reward-available", "Unclaimed Rewards Available!");
        msg.addDefault("PlaceholderAPI.no-rewards", "No Rewards Available");
        saveMsg();
    }

    public static FileConfiguration getMsg() {
        return msg;
    }
    
    public void saveMsg() {
        try {
            msg.save(mfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save messages.yml!");
        }
    }

    public void reloadMsg() {
        msg = (FileConfiguration) YamlConfiguration.loadConfiguration(mfile);
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static void saveData() {
        try {
            data.save(dfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
        }
    }

    public void reloadData() {
        data = (FileConfiguration) YamlConfiguration.loadConfiguration(dfile);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }

    public void reloadConfig() {
        config = (FileConfiguration) YamlConfiguration.loadConfiguration(cfile);
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}
