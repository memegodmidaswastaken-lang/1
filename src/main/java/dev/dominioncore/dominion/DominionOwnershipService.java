package dev.dominioncore.dominion;

import dev.dominioncore.progression.PlayerProgression;

import java.util.Set;

/**
 * Handles owned dominions, primary/secondary assignment, and switch cooldown.
 */
public final class DominionOwnershipService {
    private static final long SWITCH_COOLDOWN_SECONDS = 60;

    public void unlock(PlayerProgression player, String dominionId) {
        player.unlockedDominions().add(dominionId);
        if (player.primaryDominionId() == null) {
            player.setPrimaryDominionId(dominionId);
        } else if (player.secondaryDominionId() == null && !player.primaryDominionId().equals(dominionId)) {
            player.setSecondaryDominionId(dominionId);
        }
    }

    public boolean switchPrimary(PlayerProgression player, long nowEpochSeconds) {
        String primary = player.primaryDominionId();
        String secondary = player.secondaryDominionId();

        if (primary == null || secondary == null) {
            return false;
        }
        if (nowEpochSeconds < player.nextDominionSwitchAtEpoch()) {
            return false;
        }

        player.setPrimaryDominionId(secondary);
        player.setSecondaryDominionId(primary);
        player.setNextDominionSwitchAtEpoch(nowEpochSeconds + SWITCH_COOLDOWN_SECONDS);
        return true;
    }

    public boolean validateOwnership(PlayerProgression player) {
        Set<String> owned = player.unlockedDominions();
        String primary = player.primaryDominionId();
        String secondary = player.secondaryDominionId();

        if (primary != null && !owned.contains(primary)) {
            return false;
        }
        return secondary == null || owned.contains(secondary);
    }
}
