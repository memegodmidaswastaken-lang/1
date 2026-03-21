package dev.dominioncore.client;

import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.server.PrototypeServerGateway;
import dev.dominioncore.server.PrototypeServerSessionService;
import dev.dominioncore.server.PrototypeSyncServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class PrototypeRemoteServerGatewayTest {
    private PrototypeRemoteServerGatewayTest() {
    }

    public static void runAll() {
        Path saveRoot = tempDir("remote-client-server-test");
        Thread serverThread = null;
        try {
            PrototypeServerSessionService sessions = new PrototypeServerSessionService(
                    new DominionRuntime(),
                    saveRoot
            );
            PrototypeServerGateway gateway = new PrototypeServerGateway(sessions);

            try (PrototypeSyncServer syncServer = new PrototypeSyncServer(gateway, 0)) {
                serverThread = new Thread(syncServer::serveForever, "prototype-sync-server-test");
                serverThread.setDaemon(true);
                serverThread.start();

                PrototypeRemoteServerGateway remote = new PrototypeRemoteServerGateway("127.0.0.1", syncServer.port());
                PrototypeClientSession client = new PrototypeClientSession("remote-player");

                client.connect(remote);
                check(client.snapshot() != null, "Remote client should connect");
                check(client.snapshot().blood() == 0, "Fresh remote player should start at zero blood");

                client.requestGrantBlood(remote, 10);
                check(client.snapshot().blood() == 10, "Remote blood grant should update snapshot");

                client.notifyKill(remote, 20);
                check(client.snapshot().blood() >= 30, "Remote kill sync should update snapshot");

                remote.saveAndDisconnect("remote-player");
                client.connect(remote);
                check(client.snapshot().blood() >= 30, "Remote reconnect should restore saved blood");
            }
            if (serverThread != null) {
                serverThread.join(2_000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while stopping prototype sync server thread", e);
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
