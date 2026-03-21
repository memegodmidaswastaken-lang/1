package dev.dominioncore.client;

import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.server.PrototypeServerGateway;
import dev.dominioncore.server.PrototypeServerSessionService;

import java.nio.file.Path;

public final class PrototypeClientSessionTest {
    private PrototypeClientSessionTest() {
    }

    public static void runAll() {
        PrototypeServerSessionService sessions = new PrototypeServerSessionService(
                new DominionRuntime(),
                Path.of("runtime", "client-server-test")
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
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
