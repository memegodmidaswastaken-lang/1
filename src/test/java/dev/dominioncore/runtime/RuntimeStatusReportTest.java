package dev.dominioncore.runtime;

import dev.dominioncore.progression.PlayerProgression;

public final class RuntimeStatusReportTest {
    private RuntimeStatusReportTest() {}

    public static void runAll() {
        PlayerProgression player = new PlayerProgression("p1");
        player.setActiveBloodlineId("bloodborn");
        player.setPrimaryDominionId("authority");
        player.resources().put("blood", 55);

        String report = RuntimeStatusReport.summarize(player, 123, "Minor", "INITIATE");
        check(report.contains("bloodborn"), "Report should include bloodline");
        check(report.contains("authority"), "Report should include dominion");
        check(report.contains("faith=123"), "Report should include faith");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
