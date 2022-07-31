package cc.zjlsx.qqbinder;

import cc.zjlsx.qqbinder.command.base.BukkitCommand;
import cc.zjlsx.qqbinder.command.group.base.GroupCommandManager;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.data.DataManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.manager.QQBindManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class Main extends JavaPlugin {
    private final ConsoleCommandSender logger = Bukkit.getConsoleSender();
    private GroupCommandManager groupCommandManager;
    private ConfigManager configManager;
    private DataManager dataManager;
    private QQBindManager qqBindManager;
    public final String packageName = getClass().getPackage().getName();

    @Override
    public void onEnable() {
        //加载配置文件
        (configManager = new ConfigManager()).init(this, "config");
        (dataManager = new DataManager()).init(this, "data");

        qqBindManager = new QQBindManager();

        //注册监听器
        registerListeners();
        //注册命令
        registerCommands();

        logger.sendMessage(Messages.Enable.getMessage());
    }

    @Override
    public void onDisable() {
        qqBindManager.removeAllRequest();
        logger.sendMessage(Messages.Disable.getMessage());
    }

    private void registerCommands() {
        for (Class<? extends BukkitCommand> clazz : new Reflections(packageName + ".command.bukkit").getSubTypesOf(BukkitCommand.class)) {
            try {
                BukkitCommand bukkitCommand = clazz.getDeclaredConstructor(Main.class).newInstance(this);
                getCommand(bukkitCommand.getCommandInfo().name()).setExecutor(bukkitCommand);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerListeners() {
        for (Class<? extends Listener> clazz : new Reflections(packageName + ".listener").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = clazz.getDeclaredConstructor(Main.class).newInstance(this);
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        groupCommandManager = new GroupCommandManager(this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public QQBindManager getQQBindManager() {
        return qqBindManager;
    }

    public GroupCommandManager getGroupCommandManager() {
        return groupCommandManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
