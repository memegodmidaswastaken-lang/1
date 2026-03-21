package dev.dominioncore.sync;

public record PlayerStateSnapshot(
        String playerId,
        int blood,
        String activeBloodlineId,
        String primaryDominionId,
        String secondaryDominionId,
        int unlockedDominions
) {
}
