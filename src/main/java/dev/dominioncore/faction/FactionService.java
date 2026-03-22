package dev.dominioncore.faction;

import dev.dominioncore.core.ScalingFormula;

import java.util.Map;

public final class FactionService {
    private static final ScalingFormula AUTHORITY_FORMULA = new ScalingFormula("Members * 2 + Chunks * 3 + Online * 5 + Structures * 1");

    public double computeAuthority(Faction faction) {
        return AUTHORITY_FORMULA.evaluate(Map.of(
                "Members", (double) faction.members(),
                "Chunks", (double) faction.claimedChunks(),
                "Online", (double) faction.onlineMembers(),
                "Structures", (double) faction.structuresBuilt()
        ));
    }
}
