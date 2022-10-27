package fun.sleepy.voidcontrol.tasks;

import fun.sleepy.voidcontrol.VoidControl;
import fun.sleepy.voidcontrol.config.ConfigManager;
import fun.sleepy.voidcontrol.config.ConfigRule;
import fun.sleepy.voidcontrol.permissions.VoidControlPermission;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerVoidCheckTask {
    VoidControl plugin;
    ConfigManager configManager;
    Integer taskId = -1;

    public PlayerVoidCheckTask(VoidControl plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        schedulePlayerVoidCheckTask();
    }

    public void schedulePlayerVoidCheckTask() {
        this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                this.plugin,
                this::runPlayerVoidCheckTask,
                0L,
                configManager.getGlobalInterval()
        );
    }

    private void runPlayerVoidCheckTask() {
        HashMap<World, ConfigRule> worldConfigRules = configManager.getWorldConfigRules();
        // For each player on the server
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(VoidControlPermission.BYPASS.toString())) continue;
            World playerWorld = player.getWorld();
            // If this world has no Rules, continue
            if (!worldConfigRules.containsKey(playerWorld)) continue;
            // If we shouldn't damage the player, continue
            if (!shouldDamagePlayer(player.getLocation().getY(), worldConfigRules.get(playerWorld))) continue;
            // Otherwise, damage the player
            player.damage(configManager.getGlobalDamage());
        }
    }

    /**
     *
     * @param playerY The Y coordinate of the player we are checking
     * @param rule The rule we are using the judge whether the player should be damaged
     * @return A boolean; true if the player should be damaged based on their Y level and the rule; false if not
     */
    public boolean shouldDamagePlayer(double playerY, ConfigRule rule) {
        // If this rule's DamageBelow value is not 0, it's enabled; check if the player is below the allowed
        // Y value, and if so, damage them and continue.
        // If this rule's DamageAbove value is not 0, it's enabled; check if the player is above the allowed
        // Y value, and if so, damage them and continue.
        return (yValueIsNotZero(rule.getDamageBelowY()) && Double.compare(playerY, rule.getDamageBelowY()) < 0) ||
               (yValueIsNotZero(rule.getDamageAboveY()) && Double.compare(playerY, rule.getDamageAboveY()) > 0);
    }

    public boolean yValueIsNotZero(double yValue) {
        return (Double.compare(yValue, 0.0D) != 0);
    }

    public void cancel() {
        if (this.taskId == -1) return;
        Bukkit.getScheduler().cancelTask(this.taskId);
    }
}
