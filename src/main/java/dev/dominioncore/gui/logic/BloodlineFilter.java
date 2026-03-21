package dev.dominioncore.gui.logic;

import dev.dominioncore.gui.BloodlineOption;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class BloodlineFilter {
    private BloodlineFilter() {
    }

    public static List<BloodlineOption> apply(List<BloodlineOption> options, String query, boolean unlockedOnly) {
        String normalized = Objects.requireNonNullElse(query, "").toLowerCase(Locale.ROOT).trim();
        return options.stream()
                .filter(o -> !unlockedOnly || o.unlocked())
                .filter(o -> normalized.isEmpty()
                        || o.bloodline().displayName().toLowerCase(Locale.ROOT).contains(normalized)
                        || o.bloodline().description().toLowerCase(Locale.ROOT).contains(normalized))
                .toList();
    }
}
