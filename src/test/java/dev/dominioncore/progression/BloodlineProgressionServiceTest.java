package dev.dominioncore.progression;

import dev.dominioncore.gui.BloodlineNode;

import java.awt.Point;
import java.util.Map;

public final class BloodlineProgressionServiceTest {
    private BloodlineProgressionServiceTest() {
    }

    public static void runAll() {
        BloodlineProgressionService service = new BloodlineProgressionService();
        PlayerProgression player = new PlayerProgression("p1");
        player.resources().put("blood", 100);

        BloodlineNode root = new BloodlineNode("root", new Point(0, 0), 25, java.util.List.of(), "Root node");
        boolean unlocked = service.unlockNode(player, Map.of("root", root), "root", "blood");

        check(unlocked, "Node should unlock");
        check(player.unlockedNodes().contains("root"), "Node not recorded unlocked");
        check(player.resources().get("blood") == 75, "Resource cost not applied");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
