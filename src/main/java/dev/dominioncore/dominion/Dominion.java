package dev.dominioncore.dominion;

import dev.dominioncore.core.ScalingFormula;

/**
 * Defines a dominion and how it computes power.
 */
public record Dominion(
        String id,
        String displayName,
        DominionType type,
        ScalingFormula formula,
        String requirement
) {
}
