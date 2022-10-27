package fun.sleepy.voidcontrol.config;

public enum ConfigKey {
    GLOBAL("global"),
    DAMAGE_PER_INTERVAL("damage-per-interval"),
    INTERVAL("interval"),
    RULES("rules"),
    WORLDS("worlds"),
    VOID_DAMAGE_ABOVE("void-damage-above"),
    VOID_DAMAGE_BELOW("void-damage-below");

    private final String keyName;

    ConfigKey(final String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return keyName;
    }
}
