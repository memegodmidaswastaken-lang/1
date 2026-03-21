package dev.dominioncore.app;

import dev.dominioncore.bloodline.Bloodline;
import dev.dominioncore.gui.BloodlineNode;
import dev.dominioncore.gui.BloodlineOption;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource-backed seed data loader used by the prototype app.
 */
public final class SeedData {
    private SeedData() {
    }

    public static BloodlineOption loadBloodbornOption() {
        String json = readResource("/dominioncore/bloodlines/bloodborn.json");
        String id = readString(json, "id");
        String name = readString(json, "displayName");
        String description = readString(json, "description");
        String scalingCondition = readString(json, "scalingCondition");
        String resource = readString(json, "resource");

        List<String> passives = readStringArray(json, "passives");
        List<String> actives = readStringArray(json, "actives");

        List<BloodlineNode> nodes = List.of(
                new BloodlineNode("blood_surge", new Point(100, 140), 25, List.of(), "Burst melee power"),
                new BloodlineNode("hematic_regeneration_1", new Point(290, 140), 50, List.of("blood_surge"), "Out-of-combat regen"),
                new BloodlineNode("scarlet_aura", new Point(470, 220), 85, List.of("hematic_regeneration_1"), "AoE weaken"),
                new BloodlineNode("vampiric_overdrive", new Point(650, 140), 130, List.of("scarlet_aura"), "Late-game lifesteal")
        );

        Bloodline bloodline = new Bloodline(id, name, description, passives, actives, scalingCondition, resource);
        return new BloodlineOption(
                bloodline,
                "Strong sustain and kill-conversion scaling",
                "Vulnerable before first unlocks",
                3,
                true,
                nodes
        );
    }

    private static String readResource(String path) {
        try (InputStream stream = SeedData.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Missing resource: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read resource: " + path, e);
        }
    }

    private static String readString(String json, String key) {
        String token = "\"" + key + "\"";
        int keyStart = json.indexOf(token);
        int colon = json.indexOf(':', keyStart);
        int quoteStart = json.indexOf('"', colon + 1);
        int quoteEnd = json.indexOf('"', quoteStart + 1);
        return json.substring(quoteStart + 1, quoteEnd);
    }

    private static List<String> readStringArray(String json, String key) {
        String token = "\"" + key + "\"";
        int keyStart = json.indexOf(token);
        int arrayStart = json.indexOf('[', keyStart);
        int arrayEnd = json.indexOf(']', arrayStart);
        String content = json.substring(arrayStart + 1, arrayEnd).trim();
        if (content.isEmpty()) {
            return List.of();
        }
        String[] parts = content.split(",");
        List<String> values = new ArrayList<>();
        for (String part : parts) {
            String p = part.trim();
            int first = p.indexOf('"');
            int second = p.lastIndexOf('"');
            values.add(p.substring(first + 1, second));
        }
        return values;
    }
}
