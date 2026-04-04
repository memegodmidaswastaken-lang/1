package dev.dominioncore.dominion;

import dev.dominioncore.core.ScalingFormula;
import dev.dominioncore.dominion.acquisition.AcquisitionContext;
import dev.dominioncore.dominion.acquisition.AcquisitionPath;
import dev.dominioncore.dominion.acquisition.DominionAcquisitionService;
import dev.dominioncore.faction.FactionRank;

public final class DominionAcquisitionServiceTest {
    private DominionAcquisitionServiceTest() {
    }

    public static void runAll() {
        Dominion dominion = new Dominion("authority", "Authority", DominionType.LEADERSHIP,
                new ScalingFormula("Members * 2"), "leader");

        DominionAcquisitionService service = new DominionAcquisitionService();
        AcquisitionContext context = new AcquisitionContext(12, 8, 20, true, FactionRank.OFFICER, false);

        check(service.canAcquireVia(dominion, context, AcquisitionPath.ACHIEVEMENT_UNLOCK), "Achievement path should be available");
        check(service.canAcquireVia(dominion, context, AcquisitionPath.RITUAL), "Ritual path should be available");
        check(service.canAcquireVia(dominion, context, AcquisitionPath.FACTION_PROMOTION), "Faction promotion path should be available for officer");
        check(!service.canAcquireVia(dominion, context, AcquisitionPath.DIVINE_BLESSING), "Divine path should not be available");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
