package fun.sleepy.voidcontrol;

import fun.sleepy.voidcontrol.commands.VoidControlCommand;
import fun.sleepy.voidcontrol.config.ConfigManager;
import fun.sleepy.voidcontrol.logging.LoggingService;
import fun.sleepy.voidcontrol.tasks.PlayerVoidCheckTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidControl extends JavaPlugin {
    LoggingService loggingService;
    ConfigManager configManager;
    PlayerVoidCheckTask playerVoidCheckTask;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        this.loggingService = new LoggingService(this);
        this.configManager = new ConfigManager(this);
        this.playerVoidCheckTask = new PlayerVoidCheckTask(this);
        new VoidControlCommand(this);
    }

    public void reloadPlugin() {
        configManager.reload(false);
        playerVoidCheckTask.cancel();
        playerVoidCheckTask.schedulePlayerVoidCheckTask();
    }

    public LoggingService getLoggingService() { return loggingService; }
    public ConfigManager getConfigManager() { return configManager; }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
