package dev.dominioncore.config;

/**
 * Runtime tuning knobs for prototype balancing and hardening.
 */
public final class RuntimeBalanceConfig {
    private final boolean dominionsEnabled;
    private final boolean hardcoreMode;
    private final double maxDominionPower;
    private final double maxWeaponMultiplier;
    private final int maxHighValueKillBonus;

    public RuntimeBalanceConfig(
            boolean dominionsEnabled,
            boolean hardcoreMode,
            double maxDominionPower,
            double maxWeaponMultiplier,
            int maxHighValueKillBonus
    ) {
        this.dominionsEnabled = dominionsEnabled;
        this.hardcoreMode = hardcoreMode;
        this.maxDominionPower = maxDominionPower;
        this.maxWeaponMultiplier = maxWeaponMultiplier;
        this.maxHighValueKillBonus = maxHighValueKillBonus;
    }

    public static RuntimeBalanceConfig defaults() {
        return new RuntimeBalanceConfig(true, false, 5000.0, 1.50, 100);
    }

    public boolean dominionsEnabled() {
        return dominionsEnabled;
    }

    public boolean hardcoreMode() {
        return hardcoreMode;
    }

    public double maxDominionPower() {
        return maxDominionPower;
    }

    public double maxWeaponMultiplier() {
        return maxWeaponMultiplier;
    }

    public int maxHighValueKillBonus() {
        return maxHighValueKillBonus;
    }
}
