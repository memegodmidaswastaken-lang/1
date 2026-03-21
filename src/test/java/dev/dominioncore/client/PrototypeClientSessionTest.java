package dev.dominioncore.client;

import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.server.PrototypeServerGateway;
import dev.dominioncore.server.PrototypeServerSessionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class PrototypeClientSessionTest {
    private PrototypeClientSessionTest() {
    }

    public static void runAll() {
        Path saveRoot = tempDir("client-server-test");
        try {
            PrototypeServerSessionService sessions = new PrototypeServerSessionService(
                    new DominionRuntime(),
                    saveRoot
            );
            PrototypeServerGateway server = new PrototypeServerGateway(sessions);
            PrototypeClientSession client = new PrototypeClientSession("client-one");

            client.connect(server);
            check(client.snapshot() != null, "Client should receive snapshot on connect");
            check(client.snapshot().blood() == 0, "Fresh player should start at zero blood");

            client.requestGrantBlood(server, 15);
            check(client.snapshot().blood() == 15, "Grant blood should update client snapshot");

            client.notifyKill(server, 20);
            check(client.snapshot().blood() >= 35, "Kill event should increase blood on server and sync client");

            server.saveAndDisconnect("client-one");
            client.connect(server);
            check(client.snapshot().blood() >= 35, "Reconnect should restore saved snapshot");
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
