package cc.zjlsx.qqbinder.enums;

import cc.zjlsx.qqbinder.model.QQBindRequest;
import me.albert.amazingbot.utils.MsgUtil;
import net.md_5.bungee.api.ChatColor;

public enum Messages {
    Enable("&a插件已开启"),
    Disable("&c插件已关闭"),
    Run_In_Console("&c你不能在控制台执行此命令"),
    No_Permission("&c你没有执行此命令的权限"),
    QQ_Bind_Usage("%at_sender% 用法：#qqbind 玩家名"),
    QQ_Bind_Command("&e请在群内输入 &6#qqbind 玩家名 &e来绑定qq"),
    Bind_Success("&a成功绑定QQ&7：&b%qq_code%"),
    Not_Enough_Level("%at_sender% 您的等级低于 %level% 级，无法绑定"),
    Player_Not_Online("%at_sender% 该玩家不在线，请上线后再绑定"),
    Already_Bind_Player("%at_sender% 错误：这个QQ已经绑定了一个玩家，请先解绑"),
    Already_Bind_QQ("%at_sender% 错误：这个玩家已经绑定了一个QQ，请先解绑"),
    Already_Binding_QQ("%at_sender% 错误：这个玩家正在绑定一个QQ"),
    Already_Have_Request("%at_sender% 错误：您有一个待处理的请求，请查看游戏内聊天栏"),
    Sent_Bind_Request("%at_sender% 已向您的游戏中发送绑定请求，请注意查收\n该请求 %expires_time% 秒有效"),
    Bind_QQ_Request("&eQQ &b%qq_code% &e想要与您的游戏账号绑定，若非本人操作，请忽略该信息 "),
    Click_To_Bind("&a[点击绑定]"),
    Bind_Request_Expired("&c该绑定请求已过期"),
    QQ_Not_Bound("%at_sender% 请先使用 #qqbind 玩家名 将该QQ绑定至游戏玩家后再执行此指令！"),
    Player_Not_Baned("%at_sender% 您没有被封禁，不需要解封！"),
    Already_Self_Unbanned("%at_sender% 您今天已经申请解封过一次了，请明天再试"),
    Unban_Succeeded("%at_sender% 您已成功解封，请遵守服务器规定，做一名合格的玩家！"),
    Reload_Plugin("&a插件配置重载成功");

    private final String configPath;
    private String message;

    Messages(String message) {
        this.message = format(message);
        this.configPath = "messages." + name().toLowerCase().replace("_", "-");
    }

    Messages(String message, String configPath) {
        this.message = format(message);
        this.configPath = "messages." + configPath;
    }

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String getConfigPath() {
        return configPath;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(QQBindRequest qqBindRequest) {
        return message.replace("%qq_code%", qqBindRequest.getQQCode().toString())
                .replace("player", qqBindRequest.getPlayerName())
                .replace("uuid", qqBindRequest.getUuid().toString());
    }

    public String getMessage(Long userId) {
        return message.replace("%at_sender%", MsgUtil.getAtMsg(userId.toString()));
    }

    public void setMessage(String message) {
        this.message = format(message);
    }


}
