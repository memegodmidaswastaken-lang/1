package dev.dominioncore.server;

import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.sync.PlayerStateSnapshot;

/**
 * Minimal in-memory gateway that simulates server-side handling for prototype clients.
 */
public final class PrototypeServerGateway implements PrototypeServerApi {
    private final PrototypeServerSessionService sessions;

    public PrototypeServerGateway(PrototypeServerSessionService sessions) {
        this.sessions = sessions;
    }

    @Override
    public PlayerStateSnapshot connect(String playerId) {
        return snapshot(sessions.login(playerId));
    }

    @Override
    public PlayerStateSnapshot sync(String playerId) {
        return snapshot(sessions.login(playerId));
    }

    @Override
    public PlayerStateSnapshot grantBlood(String playerId, int amount) {
        PlayerProgression player = sessions.login(playerId);
        player.resources().merge("blood", Math.max(0, amount), Integer::sum);
        return snapshot(player);
    }

    @Override
    public PlayerStateSnapshot recordKill(String playerId, int amount) {
        sessions.recordKill(playerId, amount);
        return snapshot(sessions.login(playerId));
    }

    @Override
    public void saveAndDisconnect(String playerId) {
        sessions.logout(playerId);
    }

    private PlayerStateSnapshot snapshot(PlayerProgression player) {
        return new PlayerStateSnapshot(
                player.playerId(),
                player.resources().getOrDefault("blood", 0),
                player.activeBloodlineId(),
                player.primaryDominionId(),
                player.secondaryDominionId(),
                player.unlockedDominions().size()
        );
    }
}
