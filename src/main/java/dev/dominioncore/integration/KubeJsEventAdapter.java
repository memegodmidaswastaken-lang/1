package dev.dominioncore.integration;

/**
 * Keeps DominionCore extension points aligned with KubeJS event naming.
 */
public final class KubeJsEventAdapter {
    private KubeJsEventAdapter() {
    }

    public static String eventId(String feature, String action) {
        String left = sanitize(feature);
        String right = sanitize(action);
        if (left.isEmpty() || right.isEmpty()) {
            throw new IllegalArgumentException("feature and action must not be blank");
        }
        return "dominioncore." + left + "." + right;
    }

    private static String sanitize(String value) {
        return value == null ? "" : value.trim().toLowerCase().replace(' ', '_');
    }
}
