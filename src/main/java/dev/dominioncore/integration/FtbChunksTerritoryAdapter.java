package dev.dominioncore.integration;

/**
 * Simple adapter for territory/claim identities sourced from FTB Chunks.
 */
public final class FtbChunksTerritoryAdapter {
    private FtbChunksTerritoryAdapter() {
    }

    public static String territoryId(String dimensionId, int chunkX, int chunkZ) {
        String dimension = dimensionId == null || dimensionId.isBlank() ? "minecraft:overworld" : dimensionId.trim();
        return "ftbchunks:" + dimension + ":" + chunkX + ":" + chunkZ;
    }

    public static boolean isChunkClaimBacked(String territoryId) {
        return territoryId != null && territoryId.startsWith("ftbchunks:");
    }
}
