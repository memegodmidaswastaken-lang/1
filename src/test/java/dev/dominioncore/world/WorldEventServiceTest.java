package dev.dominioncore.world;

public final class WorldEventServiceTest {
    private WorldEventServiceTest() {}

    public static void runAll() {
        WorldEventService events = new WorldEventService();
        events.trigger("ASCENSION", "player-one", "Reached level threshold");
        events.trigger("WEATHER_SHIFT", "faction-night-court", "Forced blood moon");

        check(events.totalEvents() == 2, "Expected 2 events");
        check(events.recent(1).size() == 1, "Expected one recent event");
        check(events.recent(10).size() == 2, "Expected two recent events");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
