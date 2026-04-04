package dev.dominioncore.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public final class PrototypeTryoutTest {
    private PrototypeTryoutTest() {
    }

    public static void runAll() {
        Path saveRoot = tempDir("prototype-tryout-test");
        try {
            List<String> transcript = PrototypeTryout.run(saveRoot, "demo-player");

            check(transcript.size() == 6, "Tryout should emit a six-step transcript");
            check(transcript.get(0).contains("Started local prototype server"), "Tryout should start a local server");
            check(transcript.get(1).contains("connect => Snapshot{playerId='demo-player'"), "Tryout should connect the demo player");
            check(transcript.get(2).contains("blood=25"), "Tryout should grant blood");
            check(transcript.get(3).contains("blood=65"), "Tryout kill flow should increase blood through runtime hooks");
            check(transcript.get(4).contains("sync => Snapshot{playerId='demo-player'"), "Tryout should sync the updated snapshot");
            check(transcript.get(5).contains("disconnect => saved demo-player"), "Tryout should save on disconnect");
            check(Files.exists(saveRoot.resolve("demo-player.json")), "Tryout should persist a save file");
        } finally {
            deleteRecursively(saveRoot);
        }
    }

    private static void check(boolean condition, String msg) {
        if (!condition) {
            throw new IllegalStateException(msg);
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
                        throw new IllegalStateException("Failed to delete temp path " + path, e);
                    }
                });
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to clean temp directory " + root, e);
        }
    }
}
