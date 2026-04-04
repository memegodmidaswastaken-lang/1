package dev.dominioncore.app;

import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.runtime.DominionRuntime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class PrototypeCommandsTest {
    private PrototypeCommandsTest() {}

    public static void runAll() {
        Path saveRoot = tempDir("prototype-commands-test");
        Path savePath = saveRoot.resolve("test-player.json");
        try {
            DominionRuntime runtime = new DominionRuntime();
            PlayerProgression player = new PlayerProgression("p1");

            String blood = PrototypeCommands.execute(runtime, player, "grantblood 20");
            String dom = PrototypeCommands.execute(runtime, player, "grantdominion authority");
            String achievementsBefore = PrototypeCommands.execute(runtime, player, "achievements");
            runtime.triggerWorldEvent("ASCENSION", player.playerId(), "Milestone reached");
            String achievementsAfter = PrototypeCommands.execute(runtime, player, "achievements");
            String loader = PrototypeCommands.execute(runtime, player, "loader");
            String forgeReady = PrototypeCommands.execute(runtime, player, "forgeready");
            String save = PrototypeCommands.execute(runtime, player, "savestate " + savePath);
            player.resources().clear();
            String load = PrototypeCommands.execute(runtime, player, "loadstate " + savePath);
            String unknown = PrototypeCommands.execute(runtime, player, "wat");

            check(blood.startsWith("OK blood="), "grantblood should succeed");
            check(dom.startsWith("OK dominions="), "grantdominion should succeed");
            check(achievementsBefore.startsWith("OK achievements="), "achievements should return status");
            check(achievementsAfter.contains("WORLD_WITNESS"), "world witness achievement should appear");
            check(loader.equals("OK loader=FORGE"), "loader should default to FORGE");
            check(forgeReady.startsWith("OK ForgeReadiness{"), "forgeready should return readiness summary");
            check(save.startsWith("OK saved="), "savestate should persist player state");
            check(load.startsWith("OK loaded="), "loadstate should restore player state");
            check(player.resources().getOrDefault("blood", 0) == 20, "loadstate should restore blood resource");
            check(unknown.startsWith("ERR"), "unknown command should fail");
        } finally {
            deleteRecursively(saveRoot);
        }
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }

    private static Path tempDir(String prefix) {
        try {
            return Files.createTempDirectory(prefix);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create temp directory", e);
        }
    }

    private static void deleteRecursively(Path root) {
        try {
            if (root == null || !Files.exists(root)) {
                return;
            }
            try (var paths = Files.walk(root)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed to delete temp path " + path, e);
                    }
                });
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to clean temp directory " + root, e);
        }
    }
}
