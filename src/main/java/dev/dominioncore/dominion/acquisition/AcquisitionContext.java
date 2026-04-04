package dev.dominioncore.dominion.acquisition;

import dev.dominioncore.faction.FactionRank;

public record AcquisitionContext(
        int pvpKills,
        int daysSurvived,
        int chunksClaimed,
        boolean ritualCompleted,
        FactionRank factionRank,
        boolean divineChosen
) {
}
