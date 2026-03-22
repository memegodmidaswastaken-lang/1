package dev.dominioncore.dominion.acquisition;

import dev.dominioncore.dominion.Dominion;
import dev.dominioncore.faction.FactionRankService;

import java.util.EnumSet;
import java.util.Set;

/**
 * Evaluates which acquisition paths are currently valid for a dominion.
 */
public final class DominionAcquisitionService {
    private final FactionRankService factionRankService = new FactionRankService();

    public Set<AcquisitionPath> availablePaths(Dominion dominion, AcquisitionContext context) {
        EnumSet<AcquisitionPath> paths = EnumSet.noneOf(AcquisitionPath.class);

        paths.add(AcquisitionPath.STARTER_CHOICE);

        if (context.pvpKills() >= 10 || context.daysSurvived() >= 7 || context.chunksClaimed() >= 16) {
            paths.add(AcquisitionPath.ACHIEVEMENT_UNLOCK);
        }
        if (context.ritualCompleted()) {
            paths.add(AcquisitionPath.RITUAL);
        }
        if (factionRankService.canUsePromotionAcquisition(context.factionRank())) {
            paths.add(AcquisitionPath.FACTION_PROMOTION);
        }
        if (context.divineChosen()) {
            paths.add(AcquisitionPath.DIVINE_BLESSING);
        }

        return paths;
    }

    public boolean canAcquireVia(Dominion dominion, AcquisitionContext context, AcquisitionPath path) {
        return availablePaths(dominion, context).contains(path);
    }
}
