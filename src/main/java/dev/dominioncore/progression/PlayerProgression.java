package dev.dominioncore.progression;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PlayerProgression {
    private final String playerId;
    private String activeBloodlineId;
    private final Set<String> unlockedNodes = new HashSet<>();
    private final Map<String, Integer> resources = new HashMap<>();

    private final Set<String> unlockedDominions = new HashSet<>();
    private String primaryDominionId;
    private String secondaryDominionId;
    private long nextDominionSwitchAtEpoch;

    public PlayerProgression(String playerId) {
        this.playerId = playerId;
    }

    public String playerId() {
        return playerId;
    }

    public String activeBloodlineId() {
        return activeBloodlineId;
    }

    public void setActiveBloodlineId(String activeBloodlineId) {
        this.activeBloodlineId = activeBloodlineId;
    }

    public Set<String> unlockedNodes() {
        return unlockedNodes;
    }

    public Map<String, Integer> resources() {
        return resources;
    }

    public Set<String> unlockedDominions() {
        return unlockedDominions;
    }

    public String primaryDominionId() {
        return primaryDominionId;
    }

    public void setPrimaryDominionId(String primaryDominionId) {
        this.primaryDominionId = primaryDominionId;
    }

    public String secondaryDominionId() {
        return secondaryDominionId;
    }

    public void setSecondaryDominionId(String secondaryDominionId) {
        this.secondaryDominionId = secondaryDominionId;
    }

    public long nextDominionSwitchAtEpoch() {
        return nextDominionSwitchAtEpoch;
    }

    public void setNextDominionSwitchAtEpoch(long nextDominionSwitchAtEpoch) {
        this.nextDominionSwitchAtEpoch = nextDominionSwitchAtEpoch;
    }
}
