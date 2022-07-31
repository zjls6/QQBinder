package cc.zjlsx.qqbinder.data.base;

import cc.zjlsx.qqbinder.Main;

public interface BaseYamlConfig {
    void init(Main plugin, String name);

    void load();

    void reload();

    void save();

}
