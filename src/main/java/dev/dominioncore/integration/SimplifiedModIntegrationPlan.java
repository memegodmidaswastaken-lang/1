package dev.dominioncore.integration;

import java.util.List;

/**
 * Keeps the prototype pointed toward "addon-style" integration with established mods.
 */
public final class SimplifiedModIntegrationPlan {
    private SimplifiedModIntegrationPlan() {
    }

    public static List<ExternalModDependency> recommendedDependencies() {
        return List.of(
                new ExternalModDependency("origins", "Origins", "Bloodlines / origin-style starting identities and powers", true),
                new ExternalModDependency("apoli", "Apoli", "Power framework used by Origins-style bloodline abilities", true),
                new ExternalModDependency("ftbteams", "FTB Teams", "Faction/team membership, invites, and shared group state", false),
                new ExternalModDependency("ftbchunks", "FTB Chunks", "Territory claims, chunk ownership, and land-control hooks", false),
                new ExternalModDependency("kubejs", "KubeJS", "Scripted progression hooks, events, and datapack-friendly tuning", false)
        );
    }

    public static List<FeatureConnector> connectorMatrix() {
        return List.of(
                new FeatureConnector("bloodlines", "origins", "Origins + Apoli", "Map Origins to DominionCore bloodline identity and UI glue"),
                new FeatureConnector("factions", "ftbteams", "FTB Teams", "Treat teams as factions and consume team membership/state"),
                new FeatureConnector("territory", "ftbchunks", "FTB Chunks", "Use chunk claims as territory control input"),
                new FeatureConnector("scripting", "kubejs", "KubeJS", "Expose DominionCore hooks/events instead of maintaining a custom script runtime"),
                new FeatureConnector("custom", "dominioncore", "DominionCore", "Keep dominions, progression glue, balance, UI, and cross-mod orchestration here")
        );
    }

    public static String summary() {
        return String.join(System.lineSeparator(),
                "Simplified DominionCore integration plan:",
                "- Origins/Apoli handle bloodline/origin identity and powers.",
                "- FTB Teams can provide faction/team membership and group state.",
                "- FTB Chunks can provide territory claims and land ownership input.",
                "- KubeJS can provide scripting/event extension instead of custom script plumbing.",
                "- DominionCore should focus on dominions, progression glue, balance, UI, and cross-mod orchestration."
        );
    }
}
