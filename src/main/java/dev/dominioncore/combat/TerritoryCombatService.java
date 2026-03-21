package dev.dominioncore.combat;

public final class TerritoryCombatService {

    public double territoryAttackBonus(boolean inOwnedTerritory, boolean inHolyLand) {
        double bonus = 1.0;
        if (inOwnedTerritory) {
            bonus += 0.10;
        }
        if (inHolyLand) {
            bonus += 0.08;
        }
        return bonus;
    }

    public double territoryDefenseBonus(boolean inOwnedTerritory, boolean inHolyLand) {
        double bonus = 1.0;
        if (inOwnedTerritory) {
            bonus += 0.12;
        }
        if (inHolyLand) {
            bonus += 0.10;
        }
        return bonus;
    }
}
