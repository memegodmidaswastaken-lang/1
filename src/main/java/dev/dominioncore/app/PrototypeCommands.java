package dev.dominioncore.app;

import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.runtime.DominionRuntime;
import dev.dominioncore.runtime.RuntimeStatusReport;

import java.nio.file.Path;

/**
 * Tiny command-style helpers to speed up local prototyping.
 */
public final class PrototypeCommands {
    private PrototypeCommands() {
    }

    public static String execute(DominionRuntime runtime, PlayerProgression player, String raw) {
        String[] parts = raw.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isBlank()) {
            return "ERR empty command";
        }

        return switch (parts[0]) {
            case "grantblood" -> {
                int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                player.resources().merge("blood", Math.max(0, amount), Integer::sum);
                yield "OK blood=" + player.resources().getOrDefault("blood", 0);
            }
            case "grantdominion" -> {
                if (parts.length < 2) {
                    yield "ERR usage: grantdominion <id>";
                }
                player.unlockedDominions().add(parts[1]);
                if (player.primaryDominionId() == null) {
                    player.setPrimaryDominionId(parts[1]);
                } else if (player.secondaryDominionId() == null && !parts[1].equals(player.primaryDominionId())) {
                    player.setSecondaryDominionId(parts[1]);
                }
                yield "OK dominions=" + player.unlockedDominions().size();
            }
            case "switchdominion" -> "OK switched=" + runtime.switchPrimaryDominion(player, System.currentTimeMillis() / 1000);
            case "status" -> RuntimeStatusReport.summarize(
                    player,
                    0,
                    "N/A",
                    "N/A"
            );
            case "achievements" -> "OK achievements=" + runtime.achievements(player.playerId());
            case "loader" -> "OK loader=" + runtime.preferredLoader();
            case "forgeready" -> "OK " + runtime.forgeReadinessSummary();
            case "savestate" -> {
                Path path = commandPath(parts, 1, player.playerId() + "-command-save.json");
                runtime.savePlayerProgression(path, player);
                yield "OK saved=" + path;
            }
            case "loadstate" -> {
                Path path = commandPath(parts, 1, player.playerId() + "-command-save.json");
                PlayerProgression loaded = runtime.loadPlayerProgression(path);
                copyProgression(loaded, player);
                yield "OK loaded=" + path;
            }
            default -> "ERR unknown command";
        };
    }

    private static Path commandPath(String[] parts, int index, String defaultName) {
        if (parts.length > index && !parts[index].isBlank()) {
            return Path.of(parts[index]);
        }
        return Path.of("runtime", defaultName);
    }

    private static void copyProgression(PlayerProgression source, PlayerProgression target) {
        target.setActiveBloodlineId(source.activeBloodlineId());
        target.unlockedNodes().clear();
        target.unlockedNodes().addAll(source.unlockedNodes());
        target.resources().clear();
        target.resources().putAll(source.resources());
        target.unlockedDominions().clear();
        target.unlockedDominions().addAll(source.unlockedDominions());
        target.setPrimaryDominionId(source.primaryDominionId());
        target.setSecondaryDominionId(source.secondaryDominionId());
        target.setNextDominionSwitchAtEpoch(source.nextDominionSwitchAtEpoch());
    }
}
