package dev.dominioncore.dominion;

import dev.dominioncore.progression.PlayerProgression;

public final class DominionOwnershipServiceTest {
    private DominionOwnershipServiceTest() {
    }

    public static void runAll() {
        DominionOwnershipService service = new DominionOwnershipService();
        PlayerProgression player = new PlayerProgression("p1");

        service.unlock(player, "authority");
        service.unlock(player, "blood_god");

        check("authority".equals(player.primaryDominionId()), "Primary should be authority");
        check("blood_god".equals(player.secondaryDominionId()), "Secondary should be blood_god");

        boolean switched = service.switchPrimary(player, 1000);
        check(switched, "Switch should succeed");
        check("blood_god".equals(player.primaryDominionId()), "Primary should swap to blood_god");
        check(!service.switchPrimary(player, 1001), "Immediate second switch should fail due to cooldown");
        check(service.validateOwnership(player), "Ownership should be valid");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
