package dev.dominioncore.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lightweight in-memory leaderboard aggregation for prototype diagnostics.
 */
public final class LeaderboardService {
    private final Map<String, Double> authorityScores = new HashMap<>();
    private final Map<String, Double> faithScores = new HashMap<>();
    private final Map<String, Double> killScores = new HashMap<>();

    public void recordAuthority(String factionId, double value) {
        authorityScores.put(factionId, value);
    }

    public void recordFaith(String religionId, double value) {
        faithScores.put(religionId, value);
    }

    public void addKills(String playerId, int kills) {
        killScores.merge(playerId, (double) Math.max(0, kills), Double::sum);
    }

    public List<LeaderboardEntry> topAuthority(int limit) {
        return top(authorityScores, limit);
    }

    public List<LeaderboardEntry> topFaith(int limit) {
        return top(faithScores, limit);
    }

    public List<LeaderboardEntry> topKills(int limit) {
        return top(killScores, limit);
    }

    private List<LeaderboardEntry> top(Map<String, Double> values, int limit) {
        List<LeaderboardEntry> list = new ArrayList<>();
        for (var e : values.entrySet()) {
            list.add(new LeaderboardEntry(e.getKey(), e.getValue()));
        }
        list.sort(Comparator.comparingDouble(LeaderboardEntry::score).reversed());
        if (limit < list.size()) {
            return List.copyOf(list.subList(0, Math.max(0, limit)));
        }
        return List.copyOf(list);
    }
}
