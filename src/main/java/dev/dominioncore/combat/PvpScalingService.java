package dev.dominioncore.combat;

/**
 * Computes PvP multipliers from player progression stats.
 */
public final class PvpScalingService {

    public double weaponMultiplier(int weaponKills) {
        return 1.0 + Math.min(0.50, weaponKills * 0.005);
    }

    public double armorResistance(int armorEvolutions) {
        return Math.min(0.35, armorEvolutions * 0.05);
    }

    public double antiOneShotFactor(double incomingDamage, double maxHealth) {
        double threshold = maxHealth * 0.85;
        if (incomingDamage <= threshold) {
            return 1.0;
        }
        return threshold / incomingDamage;
    }

    public int highValueKillBonus(int victimPowerLevel) {
        return Math.max(0, victimPowerLevel / 10);
    }
}
