package cc.zjlsx.qqbinder.data.base;

import org.bukkit.plugin.java.JavaPlugin;

public interface BaseYamlConfig {
    void init(JavaPlugin plugin, String name);

    void load();

    void reload();

    void save();

}
