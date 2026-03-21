package dev.dominioncore.mod;

public final class ForgeReadinessReportTest {
    private ForgeReadinessReportTest() {
    }

    public static void runAll() {
        ForgeReadinessReport report = new ForgeReadinessReport();

        check(report.completeCount() == 6, "Expected six complete Forge readiness items");
        check(report.pendingCount() == 0, "Expected zero pending Forge readiness items");
        check(report.summarize().contains("forge_mod_entrypoint"), "Summary should include entrypoint item");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
