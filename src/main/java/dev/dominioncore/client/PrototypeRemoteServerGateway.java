package dev.dominioncore.client;

import dev.dominioncore.server.PrototypeServerApi;
import dev.dominioncore.sync.PlayerStateSnapshot;
import dev.dominioncore.sync.PlayerStateSnapshotCodec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Socket-backed gateway for talking to a separately running prototype server.
 */
public final class PrototypeRemoteServerGateway implements PrototypeServerApi {
    private final String host;
    private final int port;

    public PrototypeRemoteServerGateway(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public PlayerStateSnapshot connect(String playerId) {
        return sendForSnapshot("CONNECT " + playerId);
    }

    @Override
    public PlayerStateSnapshot sync(String playerId) {
        return sendForSnapshot("SYNC " + playerId);
    }

    @Override
    public PlayerStateSnapshot grantBlood(String playerId, int amount) {
        return sendForSnapshot("GRANT_BLOOD " + playerId + " " + amount);
    }

    @Override
    public PlayerStateSnapshot recordKill(String playerId, int amount) {
        return sendForSnapshot("RECORD_KILL " + playerId + " " + amount);
    }

    @Override
    public void saveAndDisconnect(String playerId) {
        send("DISCONNECT " + playerId);
    }

    private PlayerStateSnapshot sendForSnapshot(String command) {
        String response = send(command);
        if (!response.startsWith("OK ")) {
            throw new IllegalStateException(response);
        }
        return PlayerStateSnapshotCodec.decode(response.substring(3));
    }

    private String send(String command) {
        try (Socket socket = new Socket(host, port);
             BufferedWriter writer = new BufferedWriter(new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            writer.write(command);
            writer.newLine();
            writer.flush();
            String response = reader.readLine();
            if (response == null) {
                throw new IllegalStateException("No response from prototype server");
            }
            return response;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to talk to prototype server at " + host + ":" + port, e);
        }
    }
}
