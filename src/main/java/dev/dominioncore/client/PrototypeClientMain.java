package dev.dominioncore.client;

import dev.dominioncore.server.PrototypeServerApi;
import dev.dominioncore.sync.PlayerStateSnapshot;

import java.util.Scanner;

/**
 * CLI client for talking to {@link dev.dominioncore.server.PrototypeServerMain}.
 */
public final class PrototypeClientMain {
    private PrototypeClientMain() {
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5050;
        String playerId = args.length > 2 ? args[2] : "player-one";

        PrototypeServerApi server = new PrototypeRemoteServerGateway(host, port);
        PrototypeClientSession client = new PrototypeClientSession(playerId);

        if (args.length > 3) {
            runCommand(client, server, joinCommands(args, 3));
            return;
        }

        System.out.println("Connected to prototype server " + host + ":" + port + " as " + playerId);
        System.out.println("Client commands: connect, sync, grantblood <amount>, kill <amount>, disconnect, status, exit");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String raw = scanner.nextLine().trim();
                if (raw.isEmpty()) {
                    continue;
                }
                if ("exit".equalsIgnoreCase(raw)) {
                    break;
                }
                runCommand(client, server, raw);
            }
        }
    }

    private static String joinCommands(String[] args, int start) {
        StringBuilder out = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i > start) {
                out.append(' ');
            }
            out.append(args[i]);
        }
        return out.toString();
    }

    private static void runCommand(PrototypeClientSession client, PrototypeServerApi server, String raw) {
        String[] parts = raw.split("\\s+");
        switch (parts[0]) {
            case "connect" -> {
                client.connect(server);
                printSnapshot(client.snapshot());
            }
            case "sync" -> {
                client.sync(server);
                printSnapshot(client.snapshot());
            }
            case "grantblood" -> {
                client.requestGrantBlood(server, amount(parts));
                printSnapshot(client.snapshot());
            }
            case "kill" -> {
                client.notifyKill(server, amount(parts));
                printSnapshot(client.snapshot());
            }
            case "disconnect" -> {
                server.saveAndDisconnect(client.playerId());
                System.out.println("Disconnected " + client.playerId());
            }
            case "status" -> printSnapshot(client.snapshot());
            default -> System.out.println("ERR unknown client command");
        }
    }

    private static int amount(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("missing amount");
        }
        return Integer.parseInt(parts[1]);
    }

    private static void printSnapshot(PlayerStateSnapshot snapshot) {
        if (snapshot == null) {
            System.out.println("No snapshot cached yet. Run connect first.");
            return;
        }
        System.out.println("Snapshot{playerId='" + snapshot.playerId()
                + "', blood=" + snapshot.blood()
                + ", activeBloodlineId='" + snapshot.activeBloodlineId()
                + "', primaryDominionId='" + snapshot.primaryDominionId()
                + "', secondaryDominionId='" + snapshot.secondaryDominionId()
                + "', unlockedDominions=" + snapshot.unlockedDominions()
                + "}");
    }
}
