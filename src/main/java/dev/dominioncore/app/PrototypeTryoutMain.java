package dev.dominioncore.app;

import java.nio.file.Path;

/**
 * CLI entrypoint for a scripted end-to-end prototype tryout.
 */
public final class PrototypeTryoutMain {
    private PrototypeTryoutMain() {
    }

    public static void main(String[] args) {
        String playerId = args.length > 0 ? args[0] : "player-one";
        Path saveRoot = args.length > 1 ? Path.of(args[1]) : Path.of("runtime", "tryout");

        System.out.println("DominionCore scripted tryout for player '" + playerId + "'");
        System.out.println("This starts a local socket server, runs a few client commands, and persists the result.");

        for (String line : PrototypeTryout.run(saveRoot, playerId)) {
            System.out.println(line);
        }

        System.out.println("Tryout complete. You can also run `gradle runPrototypeServer` and `gradle runPrototypeClient` for manual testing.");
    }
}
