package dev.dominioncore.integration;

public final class SimplifiedModIntegrationPlanTest {
    private SimplifiedModIntegrationPlanTest() {
    }

    public static void runAll() {
        var deps = SimplifiedModIntegrationPlan.recommendedDependencies();

        check(!deps.isEmpty(), "Dependency plan should not be empty");
        check(deps.stream().anyMatch(dep -> dep.modId().equals("origins")), "Origins should be recommended");
        check(deps.stream().anyMatch(dep -> dep.modId().equals("apoli")), "Apoli should be recommended");
        check(deps.stream().anyMatch(dep -> dep.modId().equals("ftbchunks")), "FTB Chunks should be recommended");
        check(SimplifiedModIntegrationPlan.summary().contains("Origins/Apoli"), "Summary should mention Origins/Apoli");
        check(SimplifiedModIntegrationPlan.connectorMatrix().stream().anyMatch(connector -> connector.feature().equals("territory")),
                "Connector matrix should include territory");
        check(OriginsBloodlineAdapter.isOriginsBacked("origins:avian"), "Origins-backed id should be recognized");
        check("origins:avian".equals(OriginsBloodlineAdapter.bloodlineIdForOrigin("origins", "avian")),
                "Origins adapter should format ids consistently");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
