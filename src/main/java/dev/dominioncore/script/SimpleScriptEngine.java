package dev.dominioncore.script;

import dev.dominioncore.progression.PlayerProgression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Extremely small event script bridge for prototype usage.
 */
public final class SimpleScriptEngine {
    private final Map<String, BiConsumer<PlayerProgression, Integer>> handlers = new HashMap<>();

    public void register(String eventId, BiConsumer<PlayerProgression, Integer> handler) {
        handlers.put(eventId, handler);
    }

    public void emit(String eventId, PlayerProgression player, int value) {
        BiConsumer<PlayerProgression, Integer> handler = handlers.get(eventId);
        if (handler != null) {
            handler.accept(player, value);
        }
    }
}
