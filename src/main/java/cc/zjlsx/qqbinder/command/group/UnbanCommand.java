package cc.zjlsx.qqbinder.command.group;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.group.base.GroupCommand;
import cc.zjlsx.qqbinder.data.ConfigManager;
import cc.zjlsx.qqbinder.data.DataManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.model.GamePlayer;
import cc.zjlsx.qqbinder.util.LiteBansAddon;
import litebans.api.Database;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.message.GroupMessageEvent;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CommandInfo(name = "unban")
public class UnbanCommand extends GroupCommand {

    private final DataManager dataManager;
    private final ConfigManager configManager;

    public UnbanCommand(Main plugin) {
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
        if (!Database.get().isPlayerBanned(uuid, null)) {
            e.response(Messages.Player_Not_Baned.getMessage(userID));
            return;
        }

//        被管理员手动封禁（不是控制台）的不能申请自助解封
        if (!LiteBansAddon.isBanedByConsole(uuid)) {
            e.response(Messages.Not_Baned_By_Console.getMessage(userID));
            return;
        }

//        被永久封禁的也不能申请自助解封
        if (LiteBansAddon.isPermanentlyBaned(uuid)) {
            e.response(Messages.Permanently_Ban.getMessage(userID));
            return;
        }

        GamePlayer gamePlayer = dataManager.getGamePlayer(uuid);

        Date lastUnbanTime = new Date(gamePlayer.getLastUnbanTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!sdf.format(lastUnbanTime).equals(sdf.format(new Date()))) {
            gamePlayer.resetUnbanToday();
        }

        if (!gamePlayer.canUnbanToday(configManager)) {
            e.response(Messages.Unban_Time_Used_Up.getMessage(userID)
                    .replace("%times%", String.valueOf(configManager.getMaxUnbanPerDay())));
            return;
        }

        gamePlayer.addUnbanToday(System.currentTimeMillis());
        gamePlayer.save(dataManager);

        int remainingUnbanTimes = configManager.getMaxUnbanPerDay() - gamePlayer.getUnbanToday();
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unban " + uuid + " --sender=QQ群自助解封 第" + gamePlayer.getUnbanToday() + "次QQ群自助解封"));

        if (remainingUnbanTimes == 0) {
            e.response(Messages.Last_Unban_Succeeded.getMessage(userID));
        } else {
            e.response(Messages.Unban_Succeeded.getMessage(userID).replace("%times%", String.valueOf(remainingUnbanTimes)));
        }
    }
}