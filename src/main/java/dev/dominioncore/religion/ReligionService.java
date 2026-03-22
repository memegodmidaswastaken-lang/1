package dev.dominioncore.religion;

import dev.dominioncore.core.ScalingFormula;

import java.util.Map;

public final class ReligionService {
    private static final ScalingFormula FAITH_FORMULA = new ScalingFormula("Followers * 1 + Temples * 10 + Prayers * 2 + Rituals * 8");

    public int computeFaith(Religion religion) {
        return (int) FAITH_FORMULA.evaluate(Map.of(
                "Followers", (double) religion.followers(),
                "Temples", (double) religion.temples(),
                "Prayers", (double) religion.activePrayers(),
                "Rituals", (double) religion.ritualCompletions()
        ));
    }

    public String unlockedMiracleTier(int faith) {
        if (faith >= 500) {
            return "Ascendant";
        }
        if (faith >= 250) {
            return "Major";
        }
        if (faith >= 100) {
            return "Minor";
        }
        return "None";
    }
}
