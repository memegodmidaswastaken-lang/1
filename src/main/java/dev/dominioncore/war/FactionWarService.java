package dev.dominioncore.war;

import java.util.HashSet;
import java.util.Set;

public final class FactionWarService {
    private final Set<String> activeWars = new HashSet<>();

    public void declareWar(String factionA, String factionB) {
        activeWars.add(key(factionA, factionB));
    }

    public void endWar(String factionA, String factionB) {
        activeWars.remove(key(factionA, factionB));
    }

    public boolean isAtWar(String factionA, String factionB) {
        return activeWars.contains(key(factionA, factionB));
    }

    private String key(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "::" + b : b + "::" + a;
    }
}
