package cc.zjlsx.qqbinder.command.group;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.group.base.GroupCommand;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.manager.QQBindManager;
import cc.zjlsx.qqbinder.model.QQBindRequest;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.message.GroupMessageEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@CommandInfo (name ="qqbind")
public class QQBindGroupCommand extends GroupCommand {

    private final QQBindManager qqBindManager;
    private final ConfigManager configManager;

    public QQBindGroupCommand(Main plugin) {
        super(plugin);
        qqBindManager = plugin.getQQBindManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(GroupMessageEvent e, String[] args) {
        Long userID = e.getUserID();
        //等级不够
        long level = Bot.getApi().getStrangerInfo(userID, false).getLevel();
        long minLevel = configManager.getMinLevel();
        if (level < minLevel) {
            e.response(Messages.Not_Enough_Level.getMessage(userID).replace("%level%", String.valueOf(minLevel)));
            return;
        }
        if (args.length != 1) {
            //未指定玩家名
            e.response(Messages.QQ_Bind_Usage.getMessage(userID));
            return;
        }
        UUID uuid = Bot.getApi().getPlayer(userID);
        if (uuid != null) {
            //该QQ已经绑定了一个玩家
            e.response(Messages.Already_Bind_Player.getMessage(userID));
            return;
        }
        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            e.response(Messages.Player_Not_Online.getMessage(userID));
            return;
        }
        Long userId = Bot.getApi().getUser(player.getUniqueId());
        if (userId != null) {
            //该玩家已经绑定了一个QQ
            e.response(Messages.Already_Bind_QQ.getMessage(userID));
            return;
        }
        Optional<QQBindRequest> optionalRequest = qqBindManager.getRequest(playerName);
        if (optionalRequest.isPresent()) {
            QQBindRequest qqBindRequest = optionalRequest.get();
            if (qqBindRequest.getQQCode().equals(userID)) {
                e.response(Messages.Already_Have_Request.getMessage(userID));
            } else {
                e.response(Messages.Already_Binding_QQ.getMessage(userID));
            }
            return;
        }

        QQBindRequest qqBindRequest = new QQBindRequest(userID, playerName, player.getUniqueId(), configManager.getExpiresTime());
        qqBindManager.addRequest(qqBindRequest);

        TextComponent bindQQRRequestText = new TextComponent(Messages.Bind_QQ_Request.getMessage(qqBindRequest));
        TextComponent clickToBindText = new TextComponent(Messages.Click_To_Bind.getMessage());
        clickToBindText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/qqbind " + qqBindRequest.getToken().toString()));

        player.spigot().sendMessage(bindQQRRequestText, clickToBindText);

        e.response(Messages.Sent_Bind_Request.getMessage(userID).replace("%expires_time%", String.valueOf(configManager.getExpiresTime())));
    }
}
