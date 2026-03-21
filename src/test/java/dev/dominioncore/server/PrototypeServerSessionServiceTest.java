package dev.dominioncore.server;

import dev.dominioncore.runtime.DominionRuntime;

import java.nio.file.Path;

public final class PrototypeServerSessionServiceTest {
    private PrototypeServerSessionServiceTest() {
    }

    public static void runAll() {
        PrototypeServerSessionService service = new PrototypeServerSessionService(
                new DominionRuntime(),
                Path.of("runtime", "sessions-test")
        );

        var player = service.login("p1");
        player.resources().put("blood", 25);
        service.recordKill("p1", 20);
        check(service.activeCount() == 1, "Expected one active player");

        service.saveAll();
        service.logout("p1");
        check(service.activeCount() == 0, "Expected zero active players after logout");

        var reloaded = service.login("p1");
        check(reloaded.resources().getOrDefault("blood", 0) >= 25, "Reloaded player should keep saved blood");
        check(service.activePlayer("p1") != null, "Active player lookup should succeed");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
