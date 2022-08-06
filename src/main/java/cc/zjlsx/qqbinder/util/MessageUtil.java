package cc.zjlsx.qqbinder.util;

import cc.zjlsx.qqbinder.enums.Messages;
import me.albert.amazingbot.events.message.GroupMessageEvent;

public class MessageUtil {
    public static long reply(GroupMessageEvent e, String message) {
        return e.response("[CQ:reply,id=" + e.getMessageID() + "] "  + message);
    }
}
