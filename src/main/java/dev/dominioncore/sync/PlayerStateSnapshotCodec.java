package dev.dominioncore.sync;

/**
 * Tiny line codec for prototype socket communication.
 */
public final class PlayerStateSnapshotCodec {
    private static final String NULL_TOKEN = "-";

    private PlayerStateSnapshotCodec() {
    }

    public static String encode(PlayerStateSnapshot snapshot) {
        return String.join("\t",
                safe(snapshot.playerId()),
                Integer.toString(snapshot.blood()),
                safe(snapshot.activeBloodlineId()),
                safe(snapshot.primaryDominionId()),
                safe(snapshot.secondaryDominionId()),
                Integer.toString(snapshot.unlockedDominions())
        );
    }

    public static PlayerStateSnapshot decode(String encoded) {
        String[] parts = encoded.split("\t", -1);
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid snapshot payload: " + encoded);
        }
        return new PlayerStateSnapshot(
                read(parts[0]),
                Integer.parseInt(parts[1]),
                read(parts[2]),
                read(parts[3]),
                read(parts[4]),
                Integer.parseInt(parts[5])
        );
    }

    private static String safe(String value) {
        return value == null ? NULL_TOKEN : value.replace('\t', ' ');
    }

    private static String read(String value) {
        return NULL_TOKEN.equals(value) ? null : value;
    }
}
