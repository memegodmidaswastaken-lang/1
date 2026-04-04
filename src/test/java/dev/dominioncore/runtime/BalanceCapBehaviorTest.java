package dev.dominioncore.runtime;

import dev.dominioncore.config.RuntimeBalanceConfig;
import dev.dominioncore.core.ScalingFormula;
import dev.dominioncore.dominion.Dominion;
import dev.dominioncore.dominion.DominionType;

import java.util.Map;

public final class BalanceCapBehaviorTest {
    private BalanceCapBehaviorTest() {}

    public static void runAll() {
        DominionRuntime runtime = new DominionRuntime();
        runtime.setBalanceConfig(new RuntimeBalanceConfig(true, false, 100.0, 1.10, 5));

        Dominion d = new Dominion("authority", "Authority", DominionType.LEADERSHIP,
                new ScalingFormula("Members * 2 + Chunks * 3 + Online * 5"), "leader");

        double cappedPower = runtime.computeDominionPower(d, Map.of("Members", 50.0, "Chunks", 50.0, "Online", 50.0));
        double cappedWeapon = runtime.pvpWeaponMultiplier(100);
        int cappedBonus = runtime.highValueKillBonus(999);

        check(cappedPower == 100.0, "Dominion power should be capped");
        check(cappedWeapon == 1.10, "Weapon multiplier should be capped");
        check(cappedBonus == 5, "Kill bonus should be capped");

        runtime.setBalanceConfig(new RuntimeBalanceConfig(false, false, 100.0, 1.10, 5));
        check(runtime.computeDominionPower(d, Map.of("Members", 10.0, "Chunks", 10.0, "Online", 10.0)) == 0.0,
                "Dominion power should be disabled when dominionsEnabled=false");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
