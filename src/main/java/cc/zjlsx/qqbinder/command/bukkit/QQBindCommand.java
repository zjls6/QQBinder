package cc.zjlsx.qqbinder.command.bukkit;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.base.BukkitCommand;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.manager.QQBindManager;
import cc.zjlsx.qqbinder.model.QQBindRequest;
import me.albert.amazingbot.bot.Bot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@CommandInfo (name = "qqbind", requiresPlayer = true)
public class QQBindCommand extends BukkitCommand {
    private final QQBindManager qqBindManager;
    private final ConfigManager configManager;

    public QQBindCommand(Main plugin) {
        super(plugin);
        qqBindManager = plugin.getQQBindManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Messages.QQ_Bind_Command.getMessage());
            return;
        }
        UUID token = UUID.fromString(args[0]);
        Optional<QQBindRequest> optionalRequest = qqBindManager.getRequest(token);
        if (!optionalRequest.isPresent()) {
            return;
        }
        QQBindRequest qqBindRequest = optionalRequest.get();
        if (qqBindRequest.isExpired()) {
            player.sendMessage(Messages.Bind_Request_Expired.getMessage());
            return;
        }
        Bot.getApi().setBind(qqBindRequest.getQQCode(), qqBindRequest.getUuid());
        qqBindManager.removeRequest(qqBindRequest);
        configManager.getBindSucceedCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));

        player.sendMessage(Messages.Bind_Success.getMessage(qqBindRequest));
    }
}
