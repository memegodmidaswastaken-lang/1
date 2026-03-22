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
                new ExternalModDependency("kubejs", "KubeJS", "Scripted progression hooks, events, and datapack-friendly tuning", false)
        );
    }

    public static String summary() {
        return String.join(System.lineSeparator(),
                "Simplified DominionCore integration plan:",
                "- Origins/Apoli handle bloodline/origin identity and powers.",
                "- FTB Teams can provide faction/team membership and group state.",
                "- KubeJS can provide scripting/event extension instead of custom script plumbing.",
                "- DominionCore should focus on dominions, progression glue, balance, UI, and cross-mod orchestration."
        );
    }
}
