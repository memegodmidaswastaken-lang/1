package dev.dominioncore.faction;

public record Faction(
        String id,
        String name,
        int members,
        int claimedChunks,
        int onlineMembers,
        int structuresBuilt
) {
}
