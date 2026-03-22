package dev.dominioncore.server;

import dev.dominioncore.runtime.DominionRuntime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class PrototypeServerSessionServiceTest {
    private PrototypeServerSessionServiceTest() {
    }

    public static void runAll() {
        Path saveRoot = tempDir("sessions-test");
        try {
            PrototypeServerSessionService service = new PrototypeServerSessionService(
                    new DominionRuntime(),
                    saveRoot
            );

            var player = service.login("p1");
            player.resources().put("blood", 25);
            service.recordKill("p1", 20);
            check(service.activeCount() == 1, "Expected one active player");

            service.saveAll();
            service.logout("p1");
            check(service.activeCount() == 0, "Expected zero active players after logout");

            var reloaded = service.login("p1");
            check(reloaded.resources().getOrDefault("blood", 0) >= 25, "Reloaded player should keep saved blood");
            check(service.activePlayer("p1") != null, "Active player lookup should succeed");
        } finally {
            deleteRecursively(saveRoot);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
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
                        // Best-effort cleanup only. The test assertions already ran.
                    }
                });
            }
        } catch (IOException e) {
            // Best-effort cleanup only. The test assertions already ran.
        }
    }
}
