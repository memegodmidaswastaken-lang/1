package dev.dominioncore.server;

import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.runtime.DominionRuntime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight prototype server session manager backed by runtime persistence.
 */
public final class PrototypeServerSessionService {
    private final DominionRuntime runtime;
    private final Path saveRoot;
    private final Map<String, PlayerProgression> activePlayers = new ConcurrentHashMap<>();

    public PrototypeServerSessionService(DominionRuntime runtime, Path saveRoot) {
        this.runtime = runtime;
        this.saveRoot = saveRoot;
    }

    public PlayerProgression login(String playerId) {
        return activePlayers.computeIfAbsent(playerId, this::loadOrCreate);
    }

    public void logout(String playerId) {
        PlayerProgression player = activePlayers.remove(playerId);
        if (player != null) {
            runtime.savePlayerProgression(savePath(playerId), player);
        }
    }

    public void saveAll() {
        for (PlayerProgression player : activePlayers.values()) {
            runtime.savePlayerProgression(savePath(player.playerId()), player);
        }
    }

    public void recordKill(String playerId, int amount) {
        runtime.onKill(login(playerId), amount);
    }

    public PlayerProgression activePlayer(String playerId) {
        return activePlayers.get(playerId);
    }

    public int activeCount() {
        return activePlayers.size();
    }

    public Collection<PlayerProgression> activePlayers() {
        return activePlayers.values();
    }

    private PlayerProgression loadOrCreate(String playerId) {
        Path path = savePath(playerId);
        if (Files.exists(path)) {
            return runtime.loadPlayerProgression(path);
        }
        return new PlayerProgression(playerId);
    }

    private Path savePath(String playerId) {
        return saveRoot.resolve(playerId + ".json");
    }
}
