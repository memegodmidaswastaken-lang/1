package dev.dominioncore.combat;

public final class PvpScalingServiceTest {
    private PvpScalingServiceTest() {}

    public static void runAll() {
        PvpScalingService service = new PvpScalingService();
        check(service.weaponMultiplier(10) > 1.0, "Weapon multiplier should increase");
        check(service.armorResistance(3) > 0.0, "Armor resistance should increase");
        check(service.antiOneShotFactor(40, 20) < 1.0, "Anti-one-shot should reduce large incoming damage");
        check(service.highValueKillBonus(120) == 12, "Kill bonus mismatch");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
