package dev.dominioncore.server;

import dev.dominioncore.runtime.DominionRuntime;

import java.nio.file.Path;

/**
 * Launches a simple socket-based prototype server for local client/server testing.
 */
public final class PrototypeServerMain {
    private PrototypeServerMain() {
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5050;
        Path saveRoot = args.length > 1 ? Path.of(args[1]) : Path.of("runtime", "socket-server");

        DominionRuntime runtime = new DominionRuntime();
        PrototypeServerSessionService sessions = new PrototypeServerSessionService(runtime, saveRoot);
        PrototypeServerGateway gateway = new PrototypeServerGateway(sessions);

        try (PrototypeSyncServer server = new PrototypeSyncServer(gateway, port)) {
            System.out.println("Prototype server listening on port " + server.port() + " with saves at " + saveRoot.toAbsolutePath());
            System.out.println("Commands: CONNECT <player>, SYNC <player>, GRANT_BLOOD <player> <amount>, RECORD_KILL <player> <amount>, DISCONNECT <player>");
            server.serveForever();
        }
    }
}
