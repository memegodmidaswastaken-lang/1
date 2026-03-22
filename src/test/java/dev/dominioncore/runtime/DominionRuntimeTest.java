package dev.dominioncore.runtime;

import dev.dominioncore.core.ScalingFormula;
import dev.dominioncore.dominion.Dominion;
import dev.dominioncore.dominion.DominionType;
import dev.dominioncore.dominion.acquisition.AcquisitionContext;
import dev.dominioncore.dominion.acquisition.AcquisitionPath;
import dev.dominioncore.faction.Faction;
import dev.dominioncore.faction.FactionRank;
import dev.dominioncore.mod.ModLoaderTarget;
import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.religion.Religion;
import dev.dominioncore.religion.blessing.BlessingTier;

import java.util.Map;

public final class DominionRuntimeTest {
    private DominionRuntimeTest() {
    }

    public static void runAll() {
        DominionRuntime runtime = new DominionRuntime();
        PlayerProgression player = new PlayerProgression("p1");

        runtime.onKill(player, 20);
        runtime.onKill(player, 5);

        double authority = runtime.authority(new Faction("f", "A", 5, 7, 3, 2));
        int faith = runtime.faith(new Religion("r", "D", 50, 4, 10, 5));

        Dominion d = new Dominion("authority", "Authority", DominionType.LEADERSHIP,
                new ScalingFormula("Members * 2 + Chunks * 3 + Online * 5"), "leader");
        AcquisitionContext context = new AcquisitionContext(12, 8, 20, true, FactionRank.LEADER, false);
        check(runtime.acquireDominion(player, d, context, AcquisitionPath.ACHIEVEMENT_UNLOCK), "Acquisition should succeed");

        check(player.resources().getOrDefault("blood", 0) >= 25, "Kill event did not add blood");
        check(authority > 0, "Authority should be positive");
        check(faith > 0, "Faith should be positive");
        check(runtime.computeDominionPower(d, Map.of("Members", 3.0, "Chunks", 4.0, "Online", 1.0)) > 0, "Dominion power should compute");
        runtime.triggerWorldEvent("ASCENSION", "p1", "Reached dual dominion");
        check(runtime.totalWorldEvents() == 1, "World event should be tracked");
        check(!runtime.topKills(1).isEmpty(), "Top kills should be tracked");
        check(!runtime.topAuthority(1).isEmpty(), "Top authority should be tracked");
        check(!runtime.topFaith(1).isEmpty(), "Top faith should be tracked");
        check(runtime.factionTaxIncome(5, 2, 2) == 20, "Faction tax should be 20");
        check(runtime.factionTreasuryAfterWarUpkeep(300, 3) == 150, "War upkeep should reduce treasury");
        check(runtime.blessingTier(faith) == BlessingTier.DEVOUT, "Blessing tier should be DEVOUT");
        check(runtime.canSmite(faith), "Smite should be unlocked");
        check(!runtime.canResurrect(faith), "Resurrect should not be unlocked yet");
        check(runtime.hasAchievement(player.playerId(), "WORLD_WITNESS"), "World witness achievement should unlock");
        check(runtime.hasAchievement(player.playerId(), "BLOOD_HUNTER"), "Kill achievement should unlock");
        check(!runtime.achievements(player.playerId()).isEmpty(), "Achievements list should not be empty");
        check(runtime.preferredLoader() == ModLoaderTarget.FORGE, "Default loader should be FORGE");
        runtime.setPreferredLoader(ModLoaderTarget.FABRIC);
        check(runtime.preferredLoader() == ModLoaderTarget.FABRIC, "Loader setter should update target");
        check(runtime.forgeReadinessPending() == 0, "Forge readiness pending count should be 0");
        check(runtime.forgeReadinessSummary().contains("forge_network_packets"), "Forge readiness should list packet wiring");
        check(runtime.forgeReadinessSummary().contains("forge_server_persistence_hooks"), "Forge readiness should include persistence milestone");
        check(runtime.forgeReadinessSummary().contains("forge_event_bus_hooks, complete=true") || runtime.forgeReadinessSummary().contains("forge_event_bus_hooks"), "Forge readiness should include event bus milestone");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
