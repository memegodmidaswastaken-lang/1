package dev.dominioncore.io;

import dev.dominioncore.app.SeedData;
import dev.dominioncore.gui.BloodlineOption;

/**
 * Backward-compatible facade for older imports.
 *
 * Kept intentionally minimal and free of test/JUnit references.
 */
@Deprecated(forRemoval = false)
public final class JsonSeedLoader {
    private JsonSeedLoader() {
    }

    public static BloodlineOption loadBloodbornOption() {
        return SeedData.loadBloodbornOption();
    }
}
