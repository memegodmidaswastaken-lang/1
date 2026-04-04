package dev.dominioncore.runtime;

import dev.dominioncore.progression.PlayerProgression;

/**
 * Easy diagnostics helper for quick prototype visibility.
 */
public final class RuntimeStatusReport {
    private RuntimeStatusReport() {
    }

    public static String summarize(PlayerProgression player, int faith, String miracleTier, String blessingTier) {
        return "StatusReport{" +
                "player='" + player.playerId() + '\'' +
                ", activeBloodline='" + player.activeBloodlineId() + '\'' +
                ", primaryDominion='" + player.primaryDominionId() + '\'' +
                ", secondaryDominion='" + player.secondaryDominionId() + '\'' +
                ", unlockedNodes=" + player.unlockedNodes().size() +
                ", blood=" + player.resources().getOrDefault("blood", 0) +
                ", faith=" + faith +
                ", miracleTier='" + miracleTier + '\'' +
                ", blessingTier='" + blessingTier + '\'' +
                '}';
    }
}
