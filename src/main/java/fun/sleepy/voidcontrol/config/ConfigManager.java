package fun.sleepy.voidcontrol.config;

import fun.sleepy.voidcontrol.VoidControl;
import fun.sleepy.voidcontrol.logging.LoggingService;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigManager {
    VoidControl plugin;
    LoggingService loggingService;
    FileConfiguration config;
    Long globalInterval;
    Double globalDamage;
    HashMap<World, ConfigRule> worldConfigRules = new HashMap<>();

    public ConfigManager(VoidControl plugin) {
        this.plugin = plugin;
        this.loggingService = plugin.getLoggingService();
        reload(true);
    }

    public Long getGlobalInterval() { return globalInterval; }
    public Double getGlobalDamage() { return globalDamage; }
    public HashMap<World, ConfigRule> getWorldConfigRules() { return worldConfigRules; }

    public void reload(Boolean init) {
        if (!init) {
            this.plugin.reloadConfig();
        }
        this.config = this.plugin.getConfig();
        setGlobalConfig();
        setAllConfigRules();
    }

    private void setGlobalConfig() {
        this.globalInterval = config.getLong(ConfigKey.GLOBAL+"."+ConfigKey.INTERVAL, 20L);
        this.globalDamage = config.getDouble(ConfigKey.GLOBAL+"."+ConfigKey.DAMAGE_PER_INTERVAL, 4.0D);
    }

    private void setAllConfigRules() {
        // Clear the current ruleWorlds / configRules
        worldConfigRules.clear();

        // Get everything under "rules" section in config as a MapList
        List<Map<?, ?>> rulesConfigList = config.getMapList(ConfigKey.RULES.toString());
        // For each rule in the Rules Config
        for (Map<?, ?> rule : rulesConfigList) {
            Integer ruleNumber = (rulesConfigList.indexOf(rule) + 1);
            // If rule is missing a required key, log warning and continue
            if (!ruleContainsRequiredKeys(rule)) {
                // log warning that required key is missing in rule
                loggingService.logRuleKeyWarning(ruleNumber);
                continue;
            }
            // Create the new ConfigRule
            attemptCreateConfigRule(rule, ruleNumber);
        }
    }

    /**
     *
     * @param rule A Map<?, ?> with unknown keys/values that we must validate and use to build a ConfigRule
     * @param ruleNumber The index of the rule in the list + 1 (used for logging potential errors)
     */
    private void attemptCreateConfigRule(Map<?, ?> rule, Integer ruleNumber) {
        try {
            String worldNamesString = (String) rule.get(ConfigKey.WORLDS.toString());
            Set<World> ruleWorlds = getWorldsFromWorldsString(ruleNumber, worldNamesString);
            if (ruleWorlds == null || ruleWorlds.size() == 0) {
                loggingService.logMissingWorldsWarning(ruleNumber);
                return;
            }

            for (World world : worldConfigRules.keySet()) {
                if (ruleWorlds.contains(world)) {
                    loggingService.logMultipleWorldsInstancesWarning(ruleNumber, world.getName());
                    return;
                }
            }

            // Get void damage settings for this rule
            Double voidDamageAboveY = (Double) rule.get(ConfigKey.VOID_DAMAGE_ABOVE.toString());
            Double voidDamageBelowY = (Double) rule.get(ConfigKey.VOID_DAMAGE_BELOW.toString());

            // Create configRule
            ConfigRule configRule = new ConfigRule(voidDamageAboveY, voidDamageBelowY);

            // Track which worlds we've already made rules for
            for (World world : ruleWorlds) {
                worldConfigRules.put(world, configRule);
            }

        } catch (ClassCastException e) {
            e.printStackTrace();
            loggingService.logGeneralRuleWarning(ruleNumber);
        }
    }

    /**
     *
     * @param ruleNumber The number of the rule we're getting the worlds for (used for logging)
     * @param worldsString The string we're decoding world names from
     * @return A Set of World objects; either by decoding the string, or by getting all Bukkit worlds, depending
     * on if the worldsString is "ALL"
     */
    private Set<World> getWorldsFromWorldsString(Integer ruleNumber, String worldsString) {
        if (!worldsString.equals("ALL")) {
            return getWorldsFromCommaSeparatedString(ruleNumber, worldsString);
        }
        return Bukkit.getWorlds().stream().collect(Collectors.toSet());
    }

    /**
     *
     * @param ruleNumber The index of the rule in the list + 1 (used for logging potential errors)
     * @param worldsString A string of comma-separated world names
     * @return A set of valid World objects
     */
    private Set<World> getWorldsFromCommaSeparatedString(Integer ruleNumber, String worldsString) {
        // Instantiate worlds HashSet for return later
        Set<World> worlds = new HashSet<>();
        // Transform the comma-separated string of world names into a List of Strings, accounting for whitespace
        String[] worldsNamesList = worldsString.split("\\s*,\\s*");
        for (String worldName : worldsNamesList) {
            // Get the world by name
            World world = Bukkit.getWorld(worldName);
            // If world is null, it must not exist, or they entered the name incorrectly
            if (world == null) {
                // Log warning and continue
                loggingService.logRuleWorldWarning(ruleNumber, worldName);
                continue;
            }
            // Add the world to the HashSet
            worlds.add(world);
        }
        return worlds;
    }

    /**
     *
     * @param rule A Map<?,?>, which we are checking to see if it contains the 3 required keys every rule should have
     * @return A boolean, true if all the correct keys are there, false if one or more is missing
     */
    private Boolean ruleContainsRequiredKeys(Map<?, ?> rule) {
        if (!rule.containsKey(ConfigKey.WORLDS.toString())) return false;
        if (!rule.containsKey(ConfigKey.VOID_DAMAGE_ABOVE.toString())) return false;
        return rule.containsKey(ConfigKey.VOID_DAMAGE_BELOW.toString());
    }
}
