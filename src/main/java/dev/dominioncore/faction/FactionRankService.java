package dev.dominioncore.faction;

/**
 * Encapsulates faction permission checks for prototype workflows.
 */
public final class FactionRankService {
    public boolean canUsePromotionAcquisition(FactionRank rank) {
        return rank == FactionRank.OFFICER || rank == FactionRank.LEADER;
    }
}
