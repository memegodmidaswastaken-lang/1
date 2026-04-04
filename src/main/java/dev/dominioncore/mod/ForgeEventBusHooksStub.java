package dev.dominioncore.mod;

/**
 * Prototype-safe placeholder for future Forge event bus registration.
 *
 * This deliberately avoids direct Forge APIs until the project adds the
 * real JavaFML dependency and runtime bootstrap.
 */
public final class ForgeEventBusHooksStub {
    public String registrationPlan() {
        return "ForgeEventBusHooksStub[player_login, player_logout, server_tick, living_death]";
    }

    public boolean documentsCoreHooks() {
        return registrationPlan().contains("server_tick")
                && registrationPlan().contains("living_death");
    }
}
