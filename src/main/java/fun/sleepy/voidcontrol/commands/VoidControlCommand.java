package fun.sleepy.voidcontrol.commands;

import fun.sleepy.voidcontrol.VoidControl;
import fun.sleepy.voidcontrol.logging.LoggingService;
import fun.sleepy.voidcontrol.permissions.VoidControlPermission;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoidControlCommand implements CommandExecutor, TabCompleter {

    VoidControl plugin;
    LoggingService loggingService;
    private static final String COMMAND_NAME = "voidcontrol";
    private static final String RELOAD_ARGUMENT = "reload";

    public VoidControlCommand(VoidControl plugin) {
        this.plugin = plugin;
        this.loggingService = plugin.getLoggingService();
        Objects.requireNonNull(plugin.getCommand(COMMAND_NAME)).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(COMMAND_NAME)).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(VoidControlPermission.ADMIN.toString())) {
            loggingService.sendCommandSenderMessage(
                    sender,
                    "You do not have permission to use this command."
            );
            return true;
        }
        if (args.length == 1) {
            if (!args[0].equals(RELOAD_ARGUMENT)) {
                loggingService.sendCommandSenderMessage(
                        sender,
                        "This argument does not exist on this command. Allowed usage: &d/" + COMMAND_NAME + " reload"
                );
                return true;
            }
            plugin.reloadPlugin();
            loggingService.sendCommandSenderMessage(sender, "The plugin & its config have been reloaded.");
        }
        loggingService.sendCommandSenderMessage(
                sender,
                "Command used incorrectly; Allowed usage: &d/"+COMMAND_NAME+" reload"
        );
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission(VoidControlPermission.ADMIN.toString())) return null;
        List<String> arguments = new ArrayList<>();
        arguments.add(RELOAD_ARGUMENT);
        return arguments;
    }
}
