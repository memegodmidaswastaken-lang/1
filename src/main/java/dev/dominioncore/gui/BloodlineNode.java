package dev.dominioncore.gui;

import java.awt.Point;
import java.util.List;

public record BloodlineNode(
        String id,
        Point position,
        int cost,
        List<String> requires,
        String description
) {
}
