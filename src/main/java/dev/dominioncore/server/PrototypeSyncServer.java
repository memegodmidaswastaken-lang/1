package dev.dominioncore.server;

import dev.dominioncore.sync.PlayerStateSnapshot;
import dev.dominioncore.sync.PlayerStateSnapshotCodec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket-based prototype server so the client and server can be exercised as separate processes.
 */
public final class PrototypeSyncServer implements AutoCloseable {
    private final PrototypeServerApi server;
    private final ServerSocket serverSocket;
    private volatile boolean running;

    public PrototypeSyncServer(PrototypeServerApi server, int port) {
        try {
            this.server = server;
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to start prototype sync server on port " + port, e);
        }
    }

    public int port() {
        return serverSocket.getLocalPort();
    }

    public void serveForever() {
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(() -> handleClient(socket), "prototype-sync-client");
                clientThread.setDaemon(true);
                clientThread.start();
            } catch (IOException e) {
                if (running) {
                    throw new UncheckedIOException("Failed while accepting prototype sync client", e);
                }
            }
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to close prototype sync server", e);
        }
    }

    private void handleClient(Socket socket) {
        try (socket;
             BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(handleCommand(line));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Prototype sync client connection failed", e);
        }
    }

    private String handleCommand(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) {
            return "ERR invalid command";
        }

        String command = parts[0];
        String playerId = parts[1];

        try {
            return switch (command) {
                case "CONNECT" -> ok(server.connect(playerId));
                case "SYNC" -> ok(server.sync(playerId));
                case "GRANT_BLOOD" -> ok(server.grantBlood(playerId, amount(parts)));
                case "RECORD_KILL" -> ok(server.recordKill(playerId, amount(parts)));
                case "DISCONNECT" -> {
                    server.saveAndDisconnect(playerId);
                    yield "OK DISCONNECTED";
                }
                default -> "ERR unknown command";
            };
        } catch (RuntimeException e) {
            return "ERR " + e.getMessage();
        }
    }

    private int amount(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException("missing amount");
        }
        return Integer.parseInt(parts[2]);
    }

    private String ok(PlayerStateSnapshot snapshot) {
        return "OK " + PlayerStateSnapshotCodec.encode(snapshot);
    }
}
