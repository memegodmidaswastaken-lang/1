package dev.dominioncore.client;

import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.server.PrototypeServerGateway;
import dev.dominioncore.server.PrototypeServerSessionService;
import dev.dominioncore.server.PrototypeSyncServer;

import java.nio.file.Path;

public final class PrototypeRemoteServerGatewayTest {
    private PrototypeRemoteServerGatewayTest() {
    }

    public static void runAll() {
        PrototypeServerSessionService sessions = new PrototypeServerSessionService(
                new DominionRuntime(),
                Path.of("runtime", "remote-client-server-test")
        );
        PrototypeServerGateway gateway = new PrototypeServerGateway(sessions);

        try (PrototypeSyncServer syncServer = new PrototypeSyncServer(gateway, 0)) {
            Thread serverThread = new Thread(syncServer::serveForever, "prototype-sync-server-test");
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
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
