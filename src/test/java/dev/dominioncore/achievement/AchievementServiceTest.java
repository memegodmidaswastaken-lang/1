package dev.dominioncore.achievement;

import java.util.List;

public final class AchievementServiceTest {
    private AchievementServiceTest() {
    }

    public static void runAll() {
        AchievementService service = new AchievementService();

        check(!service.recordKillMilestone("p1", 2), "Below threshold should not unlock");
        check(service.recordKillMilestone("p1", 3), "Kill milestone should unlock");
        check(!service.recordKillMilestone("p1", 5), "Duplicate unlock should not return true");

        check(service.recordDominionMilestone("p1", 2), "Dominion milestone should unlock");
        check(service.recordWorldEventMilestone("p1", 1), "World event milestone should unlock");

        List<String> achievements = service.listAchievements("p1");
        check(achievements.size() == 3, "Expected three achievements");
        check(service.hasAchievement("p1", "BLOOD_HUNTER"), "Missing BLOOD_HUNTER");

        service.unlock("p2", "BLOOD_HUNTER");
        service.unlock("p2", "DUAL_ASCENDANT");
        check(!service.topCollectors(1).isEmpty(), "Top collectors should not be empty");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
