package dev.dominioncore.combat;

public final class TerritoryCombatServiceTest {
    private TerritoryCombatServiceTest() {}

    public static void runAll() {
        TerritoryCombatService service = new TerritoryCombatService();
        check(service.territoryAttackBonus(true, false) > 1.0, "Owned territory attack bonus expected");
        check(service.territoryDefenseBonus(true, true) > service.territoryDefenseBonus(false, false), "Holy/owned defense should be higher");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
