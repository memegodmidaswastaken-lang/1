package dev.dominioncore.world;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks prototype world events (ascension, boss spawn, weather shift, etc.).
 */
public final class WorldEventService {
    private final List<String> eventLog = new ArrayList<>();

    public void trigger(String eventType, String source, String details) {
        String line = Instant.now().toString() + " | " + eventType + " | " + source + " | " + details;
        eventLog.add(line);
    }

    public List<String> recent(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        int from = Math.max(0, eventLog.size() - limit);
        return Collections.unmodifiableList(eventLog.subList(from, eventLog.size()));
    }

    public int totalEvents() {
        return eventLog.size();
    }
}
