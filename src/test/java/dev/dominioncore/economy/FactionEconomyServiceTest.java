package dev.dominioncore.economy;

public final class FactionEconomyServiceTest {
    private FactionEconomyServiceTest() {}

    public static void runAll() {
        FactionEconomyService service = new FactionEconomyService();
        int income = service.computeTickTaxIncome(10, 2, 3);
        check(income == 60, "Tax income should be 60");

        int afterDeposit = service.deposit(100, 40);
        check(afterDeposit == 140, "Deposit failed");

        int afterUpkeep = service.applyWarUpkeep(140, 2);
        check(afterUpkeep == 40, "War upkeep mismatch");

        int afterWithdraw = service.withdraw(40, 15);
        check(afterWithdraw == 25, "Withdraw mismatch");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
