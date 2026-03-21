package dev.dominioncore.server;

import dev.dominioncore.sync.PlayerStateSnapshot;

/**
 * Shared contract for local and remote prototype server integrations.
 */
public interface PrototypeServerApi {
    PlayerStateSnapshot connect(String playerId);

    PlayerStateSnapshot sync(String playerId);

    PlayerStateSnapshot grantBlood(String playerId, int amount);

    PlayerStateSnapshot recordKill(String playerId, int amount);

    void saveAndDisconnect(String playerId);
}
