package dev.dominioncore.bloodline;

import java.util.List;

/**
 * Starter data model for an origins-style bloodline.
 */
public record Bloodline(
        String id,
        String displayName,
        String description,
        List<String> passiveAbilities,
        List<String> activeAbilities,
        String scalingCondition,
        String resourceId
) {
}
