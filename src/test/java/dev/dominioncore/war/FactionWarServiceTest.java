package dev.dominioncore.war;

public final class FactionWarServiceTest {
    private FactionWarServiceTest() {}

    public static void runAll() {
        FactionWarService wars = new FactionWarService();
        wars.declareWar("night_court", "sun_tribe");
        check(wars.isAtWar("sun_tribe", "night_court"), "War state should be symmetric");
        wars.endWar("night_court", "sun_tribe");
        check(!wars.isAtWar("night_court", "sun_tribe"), "War state should end");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
