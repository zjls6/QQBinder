package cc.zjlsx.qqbinder.command.group;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.group.base.GroupCommand;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.data.DataManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.model.GamePlayer;
import cc.zjlsx.qqbinder.util.MessageUtil;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.message.GroupMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CommandInfo(name = "qd")
public class SignInGroupCommand extends GroupCommand {
    private final DataManager dataManager;
    private final ConfigManager configManager;

    public SignInGroupCommand(Main plugin) {
        super(plugin);
        dataManager = plugin.getDataManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(GroupMessageEvent e, String[] args) {
        Long userID = e.getUserID();
        UUID uuid = Bot.getApi().getPlayer(userID);
        if (uuid == null) {
            e.response(Messages.QQ_Not_Bound.getMessage(userID));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GamePlayer gamePlayer = dataManager.getGamePlayer(uuid);

        Date date = new Date(gamePlayer.getLastSignInTime());

        if (sdf.format(date).equals(sdf.format(new Date()))) {
            MessageUtil.reply(e, Messages.Already_Signed_Today.getMessage(userID));
            return;
        }
        gamePlayer.addSignInDays(System.currentTimeMillis());

        String yesterday = sdf.format(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
//        上一次签到的日期是昨天
        if (sdf.format(date).equals(yesterday)) {
//            增加连续签到
            gamePlayer.addKeepSignInDays();
        } else {
//            重置连续签到
            gamePlayer.resetKeepSignInDays();
        }

        gamePlayer.save(dataManager);

        MessageUtil.reply(e, Messages.getSignMessage(userID, gamePlayer));
    }
}
