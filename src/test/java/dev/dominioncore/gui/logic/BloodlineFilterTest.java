package dev.dominioncore.gui.logic;

import dev.dominioncore.bloodline.Bloodline;
import dev.dominioncore.gui.BloodlineNode;
import dev.dominioncore.gui.BloodlineOption;

import java.awt.Point;
import java.util.List;

public final class BloodlineFilterTest {
    private BloodlineFilterTest() {
    }

    public static void runAll() {
        BloodlineOption unlocked = new BloodlineOption(
                new Bloodline("bloodborn", "Bloodborn", "combat scaling", List.of(), List.of(), "on_kill", "blood"),
                "s", "w", 1, true,
                List.of(new BloodlineNode("start", new Point(10, 10), 1, List.of(), "Start"))
        );
        BloodlineOption locked = new BloodlineOption(
                new Bloodline("eclipse", "Eclipse", "night scaling", List.of(), List.of(), "night", "void"),
                "s", "w", 1, false,
                List.of(new BloodlineNode("start", new Point(10, 10), 1, List.of(), "Start"))
        );

        check(BloodlineFilter.apply(List.of(unlocked, locked), "blood", false).size() == 1, "Query filter failed");
        check(BloodlineFilter.apply(List.of(unlocked, locked), "", true).size() == 1, "Unlocked filter failed");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
