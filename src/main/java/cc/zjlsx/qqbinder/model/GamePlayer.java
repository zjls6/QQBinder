package cc.zjlsx.qqbinder.model;

import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.data.DataManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class GamePlayer {
    private final UUID uuid;
    private int signInDays = 0;
    private int keepSignInDays = 0;
    private long lastSignInTime = 0;
    private long lastUnbanTime = 0;
    private int unbanToday = 0;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public GamePlayer(String uuid, int signInDays, int keepSignInDays, long lastSignInTime, long lastUnbanTime, int unbanToday) {
        this.uuid = UUID.fromString(uuid);
        this.signInDays = signInDays;
        this.keepSignInDays = keepSignInDays;
        this.lastSignInTime = lastSignInTime;
        this.lastUnbanTime = lastUnbanTime;
        this.unbanToday = unbanToday;
    }

    public void save(DataManager dataManager) {
        FileConfiguration config = dataManager.config;
        if (!config.isConfigurationSection(uuid.toString())) {
            config.createSection(uuid.toString());
        }
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        section.set("sign_in_days", signInDays);
        section.set("keep_sign_in_days", keepSignInDays);
        section.set("last_sign_in_time", lastSignInTime);
        section.set("last_unban_time", lastUnbanTime);
        section.set("unban_today", unbanToday);
        dataManager.save();
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getSignInDays() {
        return signInDays;
    }

    public void addSignInDays(long time) {
        signInDays++;
        lastSignInTime = time;
    }

    public long getLastSignInTime() {
        return lastSignInTime;
    }

    public int getKeepSignInDays() {
        return keepSignInDays;
    }

    public void addKeepSignInDays() {
        keepSignInDays++;
    }

    public void resetKeepSignInDays() {
        keepSignInDays = 0;
    }

    public long getLastUnbanTime() {
        return lastUnbanTime;
    }

    public void addUnbanToday(long time) {
        unbanToday++;
        this.lastUnbanTime = time;
    }

    public int getUnbanToday() {
        return unbanToday;
    }

    public void resetUnbanToday() {
        this.unbanToday = 0;
    }

    public boolean canUnbanToday(ConfigManager configManager) {
        return unbanToday < configManager.getMaxUnbanPerDay();
    }
}
