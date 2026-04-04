package dev.dominioncore.config;

import java.nio.file.Files;
import java.nio.file.Path;

public final class RuntimeConfigLoaderTest {
    private RuntimeConfigLoaderTest() {}

    public static void runAll() {
        RuntimeConfigLoader loader = new RuntimeConfigLoader();
        try {
            Path tmp = Files.createTempFile("dominioncore-balance", ".properties");
            Files.writeString(tmp, """
                    dominionsEnabled=false
                    hardcoreMode=true
                    maxDominionPower=1234.5
                    maxWeaponMultiplier=1.25
                    maxHighValueKillBonus=33
                    """);
            RuntimeBalanceConfig cfg = loader.load(tmp);

            check(!cfg.dominionsEnabled(), "dominionsEnabled should be false");
            check(cfg.hardcoreMode(), "hardcoreMode should be true");
            check(cfg.maxDominionPower() == 1234.5, "maxDominionPower mismatch");
            check(cfg.maxWeaponMultiplier() == 1.25, "maxWeaponMultiplier mismatch");
            check(cfg.maxHighValueKillBonus() == 33, "maxHighValueKillBonus mismatch");
            Files.deleteIfExists(tmp);
        } catch (Exception e) {
            throw new IllegalStateException("RuntimeConfigLoaderTest failed", e);
        }
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
