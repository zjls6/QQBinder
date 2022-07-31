package cc.zjlsx.qqbinder.model;

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

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public GamePlayer(String uuid, int signInDays, int keepSignInDays, long lastSignInTime, long lastUnbanTime) {
        this.uuid = UUID.fromString(uuid);
        this.signInDays = signInDays;
        this.keepSignInDays = keepSignInDays;
        this.lastSignInTime = lastSignInTime;
        this.lastUnbanTime = lastUnbanTime;
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
        dataManager.save();
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getLastUnbanTime() {
        return lastUnbanTime;
    }

    public void setLastUnbanTime(long lastUnbanTime) {
        this.lastUnbanTime = lastUnbanTime;
    }
}
