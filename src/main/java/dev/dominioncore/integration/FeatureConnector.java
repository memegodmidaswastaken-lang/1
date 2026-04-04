package dev.dominioncore.integration;

/**
 * Maps a DominionCore feature area to the external mod that should own it.
 */
public record FeatureConnector(
        String feature,
        String providerModId,
        String providerName,
        String dominionCoreRole
) {
}
