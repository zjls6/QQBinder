package cc.zjlsx.qqbinder.data.base;

import cc.zjlsx.qqbinder.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BaseYamlConfigProvider implements BaseYamlConfig {
    public FileConfiguration config;
    private File configFile;
    private Main plugin;

    @Override
    public void init(Main plugin, String name) {
        this.plugin = plugin;
        String fileName = name + ".yml";
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        load();
    }

    @Override
    public void load() {
        reload();
    }

    @Override
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reload();
    }

    public Main getPlugin() {
        return plugin;
    }
}
