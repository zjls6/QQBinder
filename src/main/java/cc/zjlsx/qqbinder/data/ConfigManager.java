package cc.zjlsx.qqbinder.data;

import cc.zjlsx.qqbinder.data.base.BaseYamlConfigProvider;
import cc.zjlsx.qqbinder.enums.Messages;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends BaseYamlConfigProvider {
    private long expiresTime;
    private long minLevel;
    private List<String> bindSucceedCommands = new ArrayList<>();
    private List<Long> enabledGroups = new ArrayList<>();

    @Override
    public void load() {
        reload();
        // 生成默认的消息
        config.addDefault("messages.prefix", "&7[&bQQ&6Binder&7]");
        for (Messages message : Messages.values()) {
            config.addDefault(message.getConfigPath(), message.getMessage());
        }
        config.options().copyDefaults(true);
        save();
        //加载消息
        loadMessages();
        expiresTime = config.getLong("expiresTime");
        minLevel = config.getLong("minLevel");
        bindSucceedCommands = config.getStringList("bind-succeed-commands");
        enabledGroups = config.getLongList("enabledGroups");
    }

    private void loadMessages() {
        for (Messages message : Messages.values()) {
            String prefix = config.getString("messages.prefix");
            String configPath = message.getConfigPath();
            if (configPath == null) {
                getPlugin().getLogger().severe("无法获取消息 " + message + " 请尝试删除配置文件后重启重新生成");
                continue;
            }
            String string = config.getString(configPath);
            if (string == null) {
                getPlugin().getLogger().severe("无法获取消息 " + message.getConfigPath() + " 请尝试删除配置文件后重启重新生成");
                continue;
            }
            message.setMessage(string.replace("%prefix%", prefix));
        }
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public List<String> getBindSucceedCommands() {
        return bindSucceedCommands;
    }

    public long getMinLevel() {
        return minLevel;
    }

    public boolean isEnabled(Long groupId) {
        return enabledGroups.contains(groupId);
    }
}
