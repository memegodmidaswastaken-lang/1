package dev.dominioncore.app;

import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.runtime.DominionRuntime;

public final class PrototypeCommandsTest {
    private PrototypeCommandsTest() {}

    public static void runAll() {
        DominionRuntime runtime = new DominionRuntime();
        PlayerProgression player = new PlayerProgression("p1");

        String blood = PrototypeCommands.execute(runtime, player, "grantblood 20");
        String dom = PrototypeCommands.execute(runtime, player, "grantdominion authority");
        String achievementsBefore = PrototypeCommands.execute(runtime, player, "achievements");
        runtime.triggerWorldEvent("ASCENSION", player.playerId(), "Milestone reached");
        String achievementsAfter = PrototypeCommands.execute(runtime, player, "achievements");
        String loader = PrototypeCommands.execute(runtime, player, "loader");
        String forgeReady = PrototypeCommands.execute(runtime, player, "forgeready");
        String save = PrototypeCommands.execute(runtime, player, "savestate runtime/test-player.json");
        player.resources().clear();
        String load = PrototypeCommands.execute(runtime, player, "loadstate runtime/test-player.json");
        String unknown = PrototypeCommands.execute(runtime, player, "wat");

        check(blood.startsWith("OK blood="), "grantblood should succeed");
        check(dom.startsWith("OK dominions="), "grantdominion should succeed");
        check(achievementsBefore.startsWith("OK achievements="), "achievements should return status");
        check(achievementsAfter.contains("WORLD_WITNESS"), "world witness achievement should appear");
        check(loader.equals("OK loader=FORGE"), "loader should default to FORGE");
        check(forgeReady.startsWith("OK ForgeReadiness{"), "forgeready should return readiness summary");
        check(save.startsWith("OK saved="), "savestate should persist player state");
        check(load.startsWith("OK loaded="), "loadstate should restore player state");
        check(player.resources().getOrDefault("blood", 0) == 20, "loadstate should restore blood resource");
        check(unknown.startsWith("ERR"), "unknown command should fail");
    }

    private static void check(boolean condition, String msg) {
        if (!condition) throw new IllegalStateException(msg);
    }
}
