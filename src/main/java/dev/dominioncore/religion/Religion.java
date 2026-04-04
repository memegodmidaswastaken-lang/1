package dev.dominioncore.religion;

public record Religion(
        String id,
        String deityName,
        int followers,
        int temples,
        int activePrayers,
        int ritualCompletions
) {
}
