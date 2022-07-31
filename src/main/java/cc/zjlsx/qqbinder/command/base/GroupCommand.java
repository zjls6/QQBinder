package cc.zjlsx.qqbinder.command.base;

import cc.zjlsx.qqbinder.Main;
import me.albert.amazingbot.events.message.GroupMessageEvent;

import java.util.Objects;

public abstract class GroupCommand {
    public final Main plugin;
    private final CommandInfo commandInfo;

    public GroupCommand(Main plugin) {
        this.plugin = plugin;
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo, "未找到该命令的信息");
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public abstract void execute(GroupMessageEvent e,String[] args);

}
