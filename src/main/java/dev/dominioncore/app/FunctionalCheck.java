package dev.dominioncore.app;

import dev.dominioncore.gui.BloodlineOption;
import dev.dominioncore.gui.logic.BloodlineFilter;
import dev.dominioncore.gui.logic.TreeProgressionRules;

import java.util.List;
import java.util.Set;

public final class FunctionalCheck {
    private FunctionalCheck() {
    }

    public static void runOrThrow(List<BloodlineOption> options) {
        if (options.isEmpty()) {
            throw new IllegalStateException("No bloodline options loaded");
        }

        List<BloodlineOption> unlocked = BloodlineFilter.apply(options, "", true);
        if (unlocked.isEmpty()) {
            throw new IllegalStateException("Expected at least one unlocked bloodline");
        }

        for (BloodlineOption option : options) {
            if (!TreeProgressionRules.graphIsValid(option.nodes())) {
                throw new IllegalStateException("Invalid tree dependencies for: " + option.bloodline().id());
            }
            if (option.nodes().stream().anyMatch(node -> node.description() == null || node.description().isBlank())) {
                throw new IllegalStateException("Node descriptions must be non-empty for: " + option.bloodline().id());
            }
            if (!option.nodes().isEmpty() && !TreeProgressionRules.canUnlock(option.nodes().get(0), Set.of())) {
                throw new IllegalStateException("First node should be unlockable for: " + option.bloodline().id());
            }
        }
    }
}
