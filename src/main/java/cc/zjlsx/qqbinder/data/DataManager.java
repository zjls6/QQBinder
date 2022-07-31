package cc.zjlsx.qqbinder.data;

import cc.zjlsx.qqbinder.data.base.BaseYamlConfigProvider;
import cc.zjlsx.qqbinder.model.GamePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataManager extends BaseYamlConfigProvider {

    private final List<GamePlayer> gamePlayers = new ArrayList<>();

    @Override
    public void load() {
        reload();
        gamePlayers.clear();
        config.getKeys(false).forEach(uuid -> {
            ConfigurationSection playerDataSection = config.getConfigurationSection(uuid);
            int signInDays = playerDataSection.getInt("sign_in_days");
            int keepSignInDays = playerDataSection.getInt("keep_sign_in_days");
            long lastSignInTime = playerDataSection.getLong("last_sign_in_time");
            long lastUnbanTime = playerDataSection.getLong("last_unban_time");
            int unbanToday = playerDataSection.getInt("unban_today");
            GamePlayer gamePlayer = new GamePlayer(uuid, signInDays, keepSignInDays, lastSignInTime, lastUnbanTime, unbanToday);
            gamePlayers.add(gamePlayer);
        });
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
    }

    public Optional<GamePlayer> getGamePlayer(UUID uuid) {
        return gamePlayers.stream().filter(gamePlayer -> gamePlayer.getUuid().equals(uuid)).findFirst();
    }
}
