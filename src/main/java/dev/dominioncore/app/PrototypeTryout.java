package dev.dominioncore.app;

import dev.dominioncore.client.PrototypeClientSession;
import dev.dominioncore.client.PrototypeRemoteServerGateway;
import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.server.PrototypeServerGateway;
import dev.dominioncore.server.PrototypeServerSessionService;
import dev.dominioncore.server.PrototypeSyncServer;
import dev.dominioncore.sync.PlayerStateSnapshot;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * One-command local tryout flow for the prototype socket server and client.
 */
public final class PrototypeTryout {
    private PrototypeTryout() {
    }

    public static List<String> run(Path saveRoot, String playerId) {
        DominionRuntime runtime = new DominionRuntime();
        PrototypeServerSessionService sessions = new PrototypeServerSessionService(runtime, saveRoot);
        PrototypeServerGateway gateway = new PrototypeServerGateway(sessions);

        try (PrototypeSyncServer server = new PrototypeSyncServer(gateway, 0)) {
            Thread serverThread = new Thread(server::serveForever, "prototype-tryout-server");
            serverThread.setDaemon(true);
            serverThread.start();

            PrototypeClientSession client = new PrototypeClientSession(playerId);
            PrototypeRemoteServerGateway remote = new PrototypeRemoteServerGateway("127.0.0.1", server.port());

            List<String> transcript = new ArrayList<>();
            transcript.add("Started local prototype server on 127.0.0.1:" + server.port());

            client.connect(remote);
            transcript.add("connect => " + formatSnapshot(client.snapshot()));

            client.requestGrantBlood(remote, 25);
            transcript.add("grantblood 25 => " + formatSnapshot(client.snapshot()));

            client.notifyKill(remote, 40);
            transcript.add("kill 40 => " + formatSnapshot(client.snapshot()));

            client.sync(remote);
            transcript.add("sync => " + formatSnapshot(client.snapshot()));

            remote.saveAndDisconnect(playerId);
            transcript.add("disconnect => saved " + playerId + " under " + saveRoot.toAbsolutePath());

            return transcript;
        }
    }

    public static String formatSnapshot(PlayerStateSnapshot snapshot) {
        if (snapshot == null) {
            return "No snapshot cached yet. Run connect first.";
        }
        return "Snapshot{playerId='" + snapshot.playerId()
                + "', blood=" + snapshot.blood()
                + ", activeBloodlineId='" + snapshot.activeBloodlineId()
                + "', primaryDominionId='" + snapshot.primaryDominionId()
                + "', secondaryDominionId='" + snapshot.secondaryDominionId()
                + "', unlockedDominions=" + snapshot.unlockedDominions()
                + "}";
    }
}
