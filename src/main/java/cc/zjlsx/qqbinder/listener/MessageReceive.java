package cc.zjlsx.qqbinder.listener;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.data.ConfigManager;
import me.albert.amazingbot.events.message.GroupMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MessageReceive implements Listener {
    private final Main plugin;
    private final ConfigManager configManager;

    public MessageReceive(Main plugin) {
        this.plugin = plugin;
        configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onGroupMessage(GroupMessageEvent e) {
        if (!configManager.isEnabled(e.getGroup().getGroupID())) {
            return;
        }

        String message = e.getMsg();
        if (!message.startsWith("#")) {
            return;
        }
        String command = message.substring(1);
        String[] args = command.split(" ");

        plugin.getGroupCommandManager().execute(e, args);
    }

}
