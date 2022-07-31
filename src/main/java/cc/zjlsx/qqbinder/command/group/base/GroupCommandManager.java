package cc.zjlsx.qqbinder.command.group.base;

import cc.zjlsx.qqbinder.Main;
import cc.zjlsx.qqbinder.command.base.GroupCommand;
import me.albert.amazingbot.events.message.GroupMessageEvent;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCommandManager {
    private final Main plugin;
    private final List<GroupCommand> groupCommands = new ArrayList<>();

    public GroupCommandManager(Main plugin) {
        this.plugin = plugin;
        for (Class<? extends GroupCommand> clazz : new Reflections(plugin.packageName + ".command.group").getSubTypesOf(GroupCommand.class)) {
            try {
                GroupCommand groupCommand = clazz.getDeclaredConstructor(Main.class).newInstance(plugin);
                groupCommands.add(groupCommand);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void execute(GroupMessageEvent e, String[] args) {
        groupCommands.stream().filter(groupCommand -> groupCommand.getCommandInfo().name().equals(args[0])).findAny()
                .ifPresent(groupCommand -> groupCommand.execute(e, Arrays.copyOfRange(args, 1, args.length)));
    }
}