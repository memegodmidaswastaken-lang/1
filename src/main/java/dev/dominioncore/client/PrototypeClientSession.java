package dev.dominioncore.client;

import dev.dominioncore.server.PrototypeServerApi;
import dev.dominioncore.sync.PlayerStateSnapshot;

/**
 * Lightweight client-side cache for prototype sync tests.
 */
public final class PrototypeClientSession {
    private final String playerId;
    private PlayerStateSnapshot snapshot;

    public PrototypeClientSession(String playerId) {
        this.playerId = playerId;
    }

    public String playerId() {
        return playerId;
    }

    public void connect(PrototypeServerApi server) {
        snapshot = server.connect(playerId);
    }

    public void sync(PrototypeServerApi server) {
        snapshot = server.sync(playerId);
    }

    public void requestGrantBlood(PrototypeServerApi server, int amount) {
        snapshot = server.grantBlood(playerId, amount);
    }

    public void notifyKill(PrototypeServerApi server, int amount) {
        snapshot = server.recordKill(playerId, amount);
    }

    public PlayerStateSnapshot snapshot() {
        return snapshot;
    }
}
