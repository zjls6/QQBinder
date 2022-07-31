package cc.zjlsx.qqbinder.command.bukkit;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.base.BukkitCommand;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.enums.Permissions;
import org.bukkit.command.CommandSender;

@CommandInfo (name = "qqbinder", permission = Permissions.Admin, requiresPlayer = false)
public class QQBinder extends BukkitCommand {
    private final ConfigManager configManager;

    public QQBinder(Main plugin) {
        super(plugin);
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            configManager.load();
            sender.sendMessage(Messages.Reload_Plugin.getMessage());
        }
    }
}
