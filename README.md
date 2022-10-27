# VoidControl
A Spigot plugin that allows you to better control at which Y levels players should take void damage on your Minecraft Server

**NOTICE: This plugin is free, so [please consider leaving a review in payment](https://www.spigotmc.org/resources/voidcontrol.105944/). Leave a comment or message me directly if you have any issues and I will fix them ASAP.**

## Description:

VoidControl is a plugin that allows you to control where players take Void Damage. Vanilla Minecraft deals void damage to a player who is below y-128 in the Overworld and y-64 in The Nether/The End. By default, this plugin does the same.

You can configure the plugin to deal void damage at more or less any Y coordinates you want. However, this plugin does NOT remove the Vanilla void damage (this would potentially cause crashes, as a player going too far down in the void is not intended, so I did not include this functionality in the plugin).

## Permissions:

**Note that if you are OP and trying to test this plugin, you will bypass the void damage. So de-op yourself before trying to test, or negate the permission.**

```yml
# voidcontrol.admin - allows you to use the admin /voidcontrol reload command
# voidcontrol.bypass - allows a player to bypass the void damage caused by this plugin
```

## Commands:

```
/voidcontrol reload - Reload the plugin config
```

## Configuration:

```yml
# VoidControl by sleepylessons
#
# Allow players to bypass void damage caused by plugin with the permission: voidcontrol.bypass (normal void damage
# is not affected by or changed by this plugin).
# This plugin is pre-configured to match Vanilla behavior by default,
# so it is safe to install without configuring.
#
global:
  # how often should we check player's location (in ticks; there are 20 ticks per second) / how often should
  # we damage players that are considered in the void?
  interval: 20
  # how much damage should we deal to players considered in the void per interval? Vanilla void damage is
  # 4.0 damage per 20 ticks (4 damage per second)
  damage-per-interval: 4.0

# Each rule has 3 values
#
# worlds: a comma-seperated list of worlds that you want this rule to apply to; use ALL to have it apply to all worlds
#
# void-damage-below: the Y level below which a player will take void damage; set to 0.0 to disable (if you actually need
#                    to use 0, simply use 0.01)
#
# void-damage-below: the Y level below which a player will take void damage; set to 0.0 to disable (if you actually need
#                    to use 0, simply use 0.01)
#
# By default, these are set to the vanilla rules, in which there is no void damage anywhere above the player
# and normal void damage Below y128 in OW and Below y-64 in the Nether/The End
rules:
- worlds: world
  void-damage-above: 0.0
  void-damage-below: -128.0
- worlds: world_nether, world_the_end
  void-damage-above: 0.0
  void-damage-below: -64.0
  
```

As you can see, you can set rules per-world. You cannot set multiple rules for the same world, the plugin will not allow you to (one rule per world), but multiple worlds can share the same rule as the world_nether and world_the_end do in the config above.

I created this plugin because I wanted to deal Void Damage to players just underneath the Bedrock (y-64 in the OW, y0 in the Nether) to prevent them from going underneath it.

However, you could also use it to deal void damage to players above the Nether Roof, or even above a certain Y level in the OW/End if you feel like it.
