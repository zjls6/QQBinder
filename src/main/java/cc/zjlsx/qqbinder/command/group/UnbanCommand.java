package cc.zjlsx.qqbinder.command.group;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.CommandInfo;
import cc.zjlsx.qqbinder.command.base.GroupCommand;
import cc.zjlsx.qqbinder.data.DataManager;
import cc.zjlsx.qqbinder.enums.Messages;
import cc.zjlsx.qqbinder.model.GamePlayer;
import litebans.api.Database;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.message.GroupMessageEvent;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@CommandInfo (name = "unban")
public class UnbanCommand extends GroupCommand {

    private final DataManager dataManager;

    public UnbanCommand(Main plugin) {
        super(plugin);
        dataManager = plugin.getDataManager();
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

        Optional<GamePlayer> optionalGamePlayer = dataManager.getGamePlayer(uuid);
        GamePlayer gamePlayer = optionalGamePlayer.orElseGet(() -> new GamePlayer(uuid));

        Date lastUnbanTime = new Date(gamePlayer.getLastUnbanTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (sdf.format(lastUnbanTime).equals(sdf.format(new Date()))) {
            e.response(Messages.Already_Self_Unbanned.getMessage(userID));
            return;
        }
        gamePlayer.setLastUnbanTime(System.currentTimeMillis());
        gamePlayer.save(dataManager);

        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unban " + uuid + " --sender=QQ群自助解封 QQ群自助解封"));

        e.response(Messages.Unban_Succeeded.getMessage(userID));
    }
}