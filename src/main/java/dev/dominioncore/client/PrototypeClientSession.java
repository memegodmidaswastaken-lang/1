package dev.dominioncore.client;

import dev.dominioncore.server.PrototypeServerGateway;
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

    public void connect(PrototypeServerGateway server) {
        snapshot = server.connect(playerId);
    }

    public void sync(PrototypeServerGateway server) {
        snapshot = server.sync(playerId);
    }

    public void requestGrantBlood(PrototypeServerGateway server, int amount) {
        snapshot = server.grantBlood(playerId, amount);
    }

    public void notifyKill(PrototypeServerGateway server, int amount) {
        snapshot = server.recordKill(playerId, amount);
    }

    public PlayerStateSnapshot snapshot() {
        return snapshot;
    }
}
