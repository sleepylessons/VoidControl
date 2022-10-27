package fun.sleepy.voidcontrol.config;

public class ConfigRule {
    Double damageAboveY;
    Double damageBelowY;

    public ConfigRule(Double damageAboveY, Double damageBelowY) {
        this.damageAboveY = damageAboveY;
        this.damageBelowY = damageBelowY;
    }

    public Double getDamageAboveY() {
        return damageAboveY;
    }

    public Double getDamageBelowY() {
        return damageBelowY;
    }
}
