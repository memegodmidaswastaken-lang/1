package dev.dominioncore.persistence;

import dev.dominioncore.progression.PlayerProgression;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Lightweight file persistence for prototype player progression snapshots.
 */
public final class PlayerProgressionStore {

    public void save(Path path, PlayerProgression player) {
        String json = toJson(player);
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save progression to " + path, e);
        }
    }

    public PlayerProgression load(Path path) {
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            return fromJson(json);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load progression from " + path, e);
        }
    }

    private String toJson(PlayerProgression p) {
        StringJoiner nodes = new StringJoiner(",");
        for (String node : p.unlockedNodes()) {
            nodes.add(quote(node));
        }
        StringJoiner dominions = new StringJoiner(",");
        for (String d : p.unlockedDominions()) {
            dominions.add(quote(d));
        }
        StringJoiner resources = new StringJoiner(",");
        for (var e : p.resources().entrySet()) {
            resources.add(quote(e.getKey()) + ":" + e.getValue());
        }

        return "{" +
                "\"playerId\":" + quote(p.playerId()) + "," +
                "\"activeBloodlineId\":" + quoteOrNull(p.activeBloodlineId()) + "," +
                "\"primaryDominionId\":" + quoteOrNull(p.primaryDominionId()) + "," +
                "\"secondaryDominionId\":" + quoteOrNull(p.secondaryDominionId()) + "," +
                "\"nextDominionSwitchAtEpoch\":" + p.nextDominionSwitchAtEpoch() + "," +
                "\"unlockedNodes\":[" + nodes + "]," +
                "\"unlockedDominions\":[" + dominions + "]," +
                "\"resources\":{" + resources + "}" +
                "}";
    }

    private PlayerProgression fromJson(String json) {
        String playerId = readString(json, "playerId");
        PlayerProgression p = new PlayerProgression(playerId);
        p.setActiveBloodlineId(readNullableString(json, "activeBloodlineId"));
        p.setPrimaryDominionId(readNullableString(json, "primaryDominionId"));
        p.setSecondaryDominionId(readNullableString(json, "secondaryDominionId"));
        p.setNextDominionSwitchAtEpoch(readLong(json, "nextDominionSwitchAtEpoch"));

        for (String node : readStringArray(json, "unlockedNodes")) {
            p.unlockedNodes().add(node);
        }
        for (String d : readStringArray(json, "unlockedDominions")) {
            p.unlockedDominions().add(d);
        }

        for (String entry : readObjectEntries(json, "resources")) {
            String[] kv = entry.split(":", 2);
            if (kv.length == 2) {
                String key = unquote(kv[0].trim());
                int value = Integer.parseInt(kv[1].trim());
                p.resources().put(key, value);
            }
        }
        return p;
    }

    private String readString(String json, String key) {
        String token = quote(key);
        int ks = json.indexOf(token);
        int colon = json.indexOf(':', ks);
        int q1 = json.indexOf('"', colon + 1);
        int q2 = json.indexOf('"', q1 + 1);
        return json.substring(q1 + 1, q2);
    }

    private String readNullableString(String json, String key) {
        String token = quote(key);
        int ks = json.indexOf(token);
        int colon = json.indexOf(':', ks);
        String tail = json.substring(colon + 1).trim();
        if (tail.startsWith("null")) {
            return null;
        }
        int q1 = json.indexOf('"', colon + 1);
        int q2 = json.indexOf('"', q1 + 1);
        return json.substring(q1 + 1, q2);
    }

    private long readLong(String json, String key) {
        String token = quote(key);
        int ks = json.indexOf(token);
        int colon = json.indexOf(':', ks);
        int end = json.indexOf(',', colon + 1);
        if (end < 0) {
            end = json.indexOf('}', colon + 1);
        }
        return Long.parseLong(json.substring(colon + 1, end).trim());
    }

    private List<String> readStringArray(String json, String key) {
        String token = quote(key);
        int ks = json.indexOf(token);
        int lb = json.indexOf('[', ks);
        int rb = json.indexOf(']', lb);
        String content = json.substring(lb + 1, rb).trim();
        if (content.isEmpty()) {
            return List.of();
        }
        String[] parts = content.split(",");
        List<String> out = new ArrayList<>();
        for (String part : parts) {
            out.add(unquote(part.trim()));
        }
        return out;
    }

    private List<String> readObjectEntries(String json, String key) {
        String token = quote(key);
        int ks = json.indexOf(token);
        int lb = json.indexOf('{', ks);
        int rb = json.indexOf('}', lb);
        String content = json.substring(lb + 1, rb).trim();
        if (content.isEmpty()) {
            return List.of();
        }
        String[] parts = content.split(",");
        List<String> out = new ArrayList<>();
        for (String part : parts) {
            out.add(part.trim());
        }
        return out;
    }

    private String quote(String value) {
        return "\"" + value + "\"";
    }

    private String quoteOrNull(String value) {
        return value == null ? "null" : quote(value);
    }

    private String unquote(String text) {
        String t = text.trim();
        if (t.startsWith("\"") && t.endsWith("\"")) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }
}
