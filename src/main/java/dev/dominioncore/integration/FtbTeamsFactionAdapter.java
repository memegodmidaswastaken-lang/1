package dev.dominioncore.integration;

/**
 * Treats FTB Teams identifiers as DominionCore faction identifiers.
 */
public final class FtbTeamsFactionAdapter {
    private FtbTeamsFactionAdapter() {
    }

    public static boolean isTeamBackedFaction(String factionId) {
        return factionId != null && factionId.startsWith("ftbteams:");
    }

    public static String factionIdForTeam(String shortName) {
        String value = shortName == null ? "" : shortName.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("shortName must not be blank");
        }
        return "ftbteams:" + value;
    }
}
