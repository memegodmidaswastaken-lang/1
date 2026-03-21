package dev.dominioncore.persistence;

import dev.dominioncore.progression.PlayerProgression;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PlayerProgressionStoreTest {
    private PlayerProgressionStoreTest() {}

    public static void runAll() {
        PlayerProgressionStore store = new PlayerProgressionStore();
        PlayerProgression player = new PlayerProgression("p1");
        player.setActiveBloodlineId("bloodborn");
        player.setPrimaryDominionId("authority");
        player.setSecondaryDominionId("blood_god");
        player.setNextDominionSwitchAtEpoch(1234);
        player.unlockedNodes().add("root");
        player.unlockedDominions().add("authority");
        player.resources().put("blood", 77);

        try {
            Path tmp = Files.createTempFile("dominioncore-player", ".json");
            store.save(tmp, player);
            PlayerProgression loaded = store.load(tmp);

            check("p1".equals(loaded.playerId()), "player id mismatch");
            check("bloodborn".equals(loaded.activeBloodlineId()), "bloodline mismatch");
            check("authority".equals(loaded.primaryDominionId()), "primary dominion mismatch");
            check(loaded.resources().getOrDefault("blood", 0) == 77, "blood mismatch");
            Files.deleteIfExists(tmp);
        } catch (Exception e) {
            throw new IllegalStateException("Persistence test failed", e);
        }
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
