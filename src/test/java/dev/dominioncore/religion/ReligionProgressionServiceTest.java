package dev.dominioncore.religion;

import dev.dominioncore.religion.blessing.BlessingTier;

public final class ReligionProgressionServiceTest {
    private ReligionProgressionServiceTest() {}

    public static void runAll() {
        ReligionProgressionService service = new ReligionProgressionService();
        check(service.convertFollowers(10, 5) == 15, "Follower conversion failed");
        check(service.blessingTierForFaith(40) == BlessingTier.NONE, "Blessing tier NONE expected");
        check(service.blessingTierForFaith(200) == BlessingTier.DEVOUT, "Blessing tier DEVOUT expected");
        check(service.canSmite(130), "Smite should unlock at 130 faith");
        check(!service.canResurrect(399), "Resurrect should require 400+");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
