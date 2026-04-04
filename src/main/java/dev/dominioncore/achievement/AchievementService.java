package dev.dominioncore.achievement;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory achievement tracking for prototype milestones.
 */
public final class AchievementService {
    private final Map<String, Set<String>> unlockedByPlayer = new ConcurrentHashMap<>();

    public boolean recordKillMilestone(String playerId, int totalKills) {
        if (totalKills >= 3) {
            return unlock(playerId, "BLOOD_HUNTER");
        }
        return false;
    }

    public boolean recordDominionMilestone(String playerId, int unlockedDominions) {
        if (unlockedDominions >= 2) {
            return unlock(playerId, "DUAL_ASCENDANT");
        }
        return false;
    }

    public boolean recordWorldEventMilestone(String playerId, int totalWorldEvents) {
        if (totalWorldEvents >= 1) {
            return unlock(playerId, "WORLD_WITNESS");
        }
        return false;
    }

    public boolean unlock(String playerId, String achievementId) {
        Set<String> unlocked = unlockedByPlayer.computeIfAbsent(playerId, ignored -> new LinkedHashSet<>());
        return unlocked.add(achievementId);
    }

    public boolean hasAchievement(String playerId, String achievementId) {
        return unlockedByPlayer.getOrDefault(playerId, Set.of()).contains(achievementId);
    }

    public List<String> listAchievements(String playerId) {
        return unlockedByPlayer.getOrDefault(playerId, Set.of()).stream()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public List<String> topCollectors(int limit) {
        return unlockedByPlayer.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .limit(Math.max(0, limit))
                .map(entry -> entry.getKey() + ":" + entry.getValue().size())
                .toList();
    }

    public int totalUnlocked(String playerId) {
        return unlockedByPlayer.getOrDefault(playerId, Set.of()).size();
    }
}
