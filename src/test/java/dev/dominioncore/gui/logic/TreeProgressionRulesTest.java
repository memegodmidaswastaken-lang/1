package dev.dominioncore.gui.logic;

import dev.dominioncore.gui.BloodlineNode;

import java.awt.Point;
import java.util.List;
import java.util.Set;

public final class TreeProgressionRulesTest {
    private TreeProgressionRulesTest() {
    }

    public static void runAll() {
        BloodlineNode root = new BloodlineNode("root", new Point(0, 0), 1, List.of(), "root");
        BloodlineNode child = new BloodlineNode("child", new Point(1, 1), 1, List.of("root"), "child");

        check(TreeProgressionRules.graphIsValid(List.of(root, child)), "Graph should be valid");
        check(TreeProgressionRules.canUnlock(root, Set.of()), "Root should unlock");
        check(!TreeProgressionRules.canUnlock(child, Set.of()), "Child should not unlock without root");
        check(TreeProgressionRules.canUnlock(child, Set.of("root")), "Child should unlock with root");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
