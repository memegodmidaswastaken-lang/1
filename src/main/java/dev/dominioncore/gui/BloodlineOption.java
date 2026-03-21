package dev.dominioncore.gui;

import dev.dominioncore.bloodline.Bloodline;

import java.util.List;

public record BloodlineOption(
        Bloodline bloodline,
        String strengths,
        String weaknesses,
        int difficulty,
        boolean unlocked,
        List<BloodlineNode> nodes
) {
}
