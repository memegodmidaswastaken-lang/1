package dev.dominioncore.integration;

/**
 * Tiny adapter for treating Origins-style identities as DominionCore bloodlines.
 */
public final class OriginsBloodlineAdapter {
    private OriginsBloodlineAdapter() {
    }

    public static boolean isOriginsBacked(String bloodlineId) {
        return bloodlineId != null && bloodlineId.startsWith("origins:");
    }

    public static String bloodlineIdForOrigin(String namespace, String originPath) {
        String ns = (namespace == null || namespace.isBlank()) ? "origins" : namespace.trim();
        String path = originPath == null ? "" : originPath.trim();
        if (path.isEmpty()) {
            throw new IllegalArgumentException("originPath must not be blank");
        }
        return ns + ":" + path;
    }
}
