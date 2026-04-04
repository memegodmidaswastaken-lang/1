package dev.dominioncore.mod;

/**
 * Prototype-safe placeholder for future Forge-backed persistence lifecycle hooks.
 */
public final class ForgeServerPersistenceHooksStub {
    public String persistencePlan() {
        return "ForgeServerPersistenceHooksStub[player_load, player_save, player_logout, server_stop]";
    }

    public boolean documentsSaveLifecycle() {
        return persistencePlan().contains("player_save")
                && persistencePlan().contains("server_stop");
    }
}
