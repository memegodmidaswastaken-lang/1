package dev.dominioncore.integration;

public final class ConnectorAdapterTest {
    private ConnectorAdapterTest() {
    }

    public static void runAll() {
        check("ftbteams:night_court".equals(FtbTeamsFactionAdapter.factionIdForTeam("night_court")),
                "FTB Teams adapter should prefix faction ids");
        check(FtbTeamsFactionAdapter.isTeamBackedFaction("ftbteams:night_court"),
                "FTB Teams-backed faction id should be recognized");

        String territoryId = FtbChunksTerritoryAdapter.territoryId("minecraft:overworld", 12, -4);
        check("ftbchunks:minecraft:overworld:12:-4".equals(territoryId),
                "FTB Chunks adapter should format territory ids");
        check(FtbChunksTerritoryAdapter.isChunkClaimBacked(territoryId),
                "FTB Chunks-backed territory id should be recognized");

        check("dominioncore.bloodlines.selected".equals(KubeJsEventAdapter.eventId("bloodlines", "selected")),
                "KubeJS event adapter should normalize hook ids");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
