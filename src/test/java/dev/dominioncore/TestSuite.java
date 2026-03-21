package dev.dominioncore;

import dev.dominioncore.achievement.AchievementServiceTest;
import dev.dominioncore.app.PrototypeCommandsTest;
import dev.dominioncore.combat.PvpScalingServiceTest;
import dev.dominioncore.combat.TerritoryCombatServiceTest;
import dev.dominioncore.config.RuntimeConfigLoaderTest;
import dev.dominioncore.core.ScalingFormulaTest;
import dev.dominioncore.dominion.DominionAcquisitionServiceTest;
import dev.dominioncore.dominion.DominionOwnershipServiceTest;
import dev.dominioncore.economy.FactionEconomyServiceTest;
import dev.dominioncore.faction.FactionRankServiceTest;
import dev.dominioncore.gui.logic.BloodlineFilterTest;
import dev.dominioncore.gui.logic.TreeProgressionRulesTest;
import dev.dominioncore.leaderboard.LeaderboardServiceTest;
import dev.dominioncore.mod.ForgeEventBusHooksStubTest;
import dev.dominioncore.mod.ForgeModEntrypointStubTest;
import dev.dominioncore.mod.ForgeNetworkPacketStubTest;
import dev.dominioncore.mod.ForgeReadinessReportTest;
import dev.dominioncore.mod.ForgeServerPersistenceHooksStubTest;
import dev.dominioncore.persistence.PlayerProgressionStoreTest;
import dev.dominioncore.progression.BloodlineProgressionServiceTest;
import dev.dominioncore.religion.ReligionProgressionServiceTest;
import dev.dominioncore.runtime.BalanceCapBehaviorTest;
import dev.dominioncore.runtime.DominionRuntimeTest;
import dev.dominioncore.runtime.RuntimeStatusReportTest;
import dev.dominioncore.war.FactionWarServiceTest;
import dev.dominioncore.world.WorldEventServiceTest;

public final class TestSuite {
    private TestSuite() {
    }

    public static void main(String[] args) {
        ScalingFormulaTest.runAll();
        AchievementServiceTest.runAll();
        PrototypeCommandsTest.runAll();
        PvpScalingServiceTest.runAll();
        TerritoryCombatServiceTest.runAll();
        RuntimeConfigLoaderTest.runAll();
        DominionAcquisitionServiceTest.runAll();
        DominionOwnershipServiceTest.runAll();
        FactionEconomyServiceTest.runAll();
        FactionRankServiceTest.runAll();
        BloodlineFilterTest.runAll();
        TreeProgressionRulesTest.runAll();
        LeaderboardServiceTest.runAll();
        ForgeEventBusHooksStubTest.runAll();
        ForgeModEntrypointStubTest.runAll();
        ForgeNetworkPacketStubTest.runAll();
        ForgeReadinessReportTest.runAll();
        ForgeServerPersistenceHooksStubTest.runAll();
        PlayerProgressionStoreTest.runAll();
        BloodlineProgressionServiceTest.runAll();
        ReligionProgressionServiceTest.runAll();
        BalanceCapBehaviorTest.runAll();
        DominionRuntimeTest.runAll();
        RuntimeStatusReportTest.runAll();
        FactionWarServiceTest.runAll();
        WorldEventServiceTest.runAll();
        System.out.println("All tests passed.");
    }
}
