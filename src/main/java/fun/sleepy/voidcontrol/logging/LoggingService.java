package fun.sleepy.voidcontrol.logging;

import fun.sleepy.voidcontrol.VoidControl;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LoggingService {
    VoidControl plugin;

    private final String MESSAGE_PREFIX = "&dVoidControl &5| &a";

    public LoggingService(VoidControl plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender Command sender; could be a player or the console
     * @param message The message you would like to send to the command sender
     */
    public void sendCommandSenderMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(MESSAGE_PREFIX + message));
    }

    /**
     *
     * @param message The message you want to colorize
     * @return A string with all & characters converted to the proper color code character
     */
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     *
     * @param message Message you would like to log as a warning in the console
     */
    private void logWarning(String message) {
        plugin.getLogger().warning(MESSAGE_PREFIX + "Warning! " + message);
    }

    /**
     *
     * @param ruleNumber The index + 1 of the rule that is not properly formatted
     */
    public void logRuleKeyWarning(Integer ruleNumber) {
        logWarning(
                "Rule #" + ruleNumber + " is invalid, and will not be registered. " +
                "Please see the documentation for proper formatting / required fields."
        );
    }

    public void logRuleWorldWarning(Integer ruleNumber, String worldName) {
        logWarning(
                "Rule #"+ruleNumber+" contains a world name that is invalid. " +
                "World \"" + worldName + "\" does not exist or is malformed (names are case-sensitive)."
        );
    }

    public void logGeneralRuleWarning(Integer ruleNumber) {
        logWarning(
                "Rule #" + ruleNumber + " could not be processed due to malformed configuration; see " +
                "Documentation for proper formatting."
        );
    }

    public void logMissingWorldsWarning(Integer ruleNumber) {
        logWarning(
                "Rule #" + ruleNumber + " is missing valid worlds, and will therefore not be added to checks. " +
                "See documentation for proper formatting."
        );
    }

    public void logMultipleWorldsInstancesWarning(Integer ruleNumber, String worldName) {
        logWarning(
                "Rule #" + ruleNumber + " contains the world name \"" + worldName + "\", which is already in use "+
                "by another Rule. This rule will not be registered, as you may only include each world in a single rule."
        );
    }
}
