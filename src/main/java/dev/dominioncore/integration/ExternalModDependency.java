package dev.dominioncore.integration;

/**
 * Describes an external mod DominionCore can build on instead of re-implementing a system itself.
 */
public record ExternalModDependency(
        String modId,
        String displayName,
        String purpose,
        boolean required
) {
}
