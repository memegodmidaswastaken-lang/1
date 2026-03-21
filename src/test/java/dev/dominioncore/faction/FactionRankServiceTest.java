package dev.dominioncore.faction;

public final class FactionRankServiceTest {
    private FactionRankServiceTest() {
    }

    public static void runAll() {
        FactionRankService service = new FactionRankService();

        check(!service.canUsePromotionAcquisition(FactionRank.MEMBER), "Member should not be eligible");
        check(service.canUsePromotionAcquisition(FactionRank.OFFICER), "Officer should be eligible");
        check(service.canUsePromotionAcquisition(FactionRank.LEADER), "Leader should be eligible");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
