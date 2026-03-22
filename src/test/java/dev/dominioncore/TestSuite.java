package dev.dominioncore;

import dev.dominioncore.achievement.AchievementServiceTest;
import dev.dominioncore.app.PrototypeCommandsTest;
import dev.dominioncore.client.PrototypeClientSessionTest;
import dev.dominioncore.client.PrototypeRemoteServerGatewayTest;
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
import dev.dominioncore.integration.SimplifiedModIntegrationPlanTest;
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
import dev.dominioncore.server.PrototypeServerSessionServiceTest;
import dev.dominioncore.war.FactionWarServiceTest;
import dev.dominioncore.world.WorldEventServiceTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestSuite {
    private static PrintWriter reportWriter;

    private TestSuite() {
    }

    public static void main(String[] args) {
        try {
            reportWriter = openReportWriter();
            log("Writing TestSuite diagnostics to " + Path.of("build", "reports", "testsuite.log").toAbsolutePath());

            run("ScalingFormulaTest", ScalingFormulaTest::runAll);
            run("AchievementServiceTest", AchievementServiceTest::runAll);
            run("PrototypeCommandsTest", PrototypeCommandsTest::runAll);
            run("PrototypeClientSessionTest", PrototypeClientSessionTest::runAll);
            run("PrototypeRemoteServerGatewayTest", PrototypeRemoteServerGatewayTest::runAll);
            run("PvpScalingServiceTest", PvpScalingServiceTest::runAll);
            run("TerritoryCombatServiceTest", TerritoryCombatServiceTest::runAll);
            run("RuntimeConfigLoaderTest", RuntimeConfigLoaderTest::runAll);
            run("DominionAcquisitionServiceTest", DominionAcquisitionServiceTest::runAll);
            run("DominionOwnershipServiceTest", DominionOwnershipServiceTest::runAll);
            run("FactionEconomyServiceTest", FactionEconomyServiceTest::runAll);
            run("FactionRankServiceTest", FactionRankServiceTest::runAll);
        run("BloodlineFilterTest", BloodlineFilterTest::runAll);
        run("TreeProgressionRulesTest", TreeProgressionRulesTest::runAll);
        run("SimplifiedModIntegrationPlanTest", SimplifiedModIntegrationPlanTest::runAll);
        run("LeaderboardServiceTest", LeaderboardServiceTest::runAll);
            run("ForgeEventBusHooksStubTest", ForgeEventBusHooksStubTest::runAll);
            run("ForgeModEntrypointStubTest", ForgeModEntrypointStubTest::runAll);
            run("ForgeNetworkPacketStubTest", ForgeNetworkPacketStubTest::runAll);
            run("ForgeReadinessReportTest", ForgeReadinessReportTest::runAll);
            run("ForgeServerPersistenceHooksStubTest", ForgeServerPersistenceHooksStubTest::runAll);
            run("PlayerProgressionStoreTest", PlayerProgressionStoreTest::runAll);
            run("BloodlineProgressionServiceTest", BloodlineProgressionServiceTest::runAll);
            run("ReligionProgressionServiceTest", ReligionProgressionServiceTest::runAll);
            run("BalanceCapBehaviorTest", BalanceCapBehaviorTest::runAll);
            run("DominionRuntimeTest", DominionRuntimeTest::runAll);
            run("RuntimeStatusReportTest", RuntimeStatusReportTest::runAll);
            run("PrototypeServerSessionServiceTest", PrototypeServerSessionServiceTest::runAll);
            run("FactionWarServiceTest", FactionWarServiceTest::runAll);
            run("WorldEventServiceTest", WorldEventServiceTest::runAll);
            log("All tests passed.");
        } catch (Throwable t) {
            log("TEST SUITE FAILED: " + t);
            log(stackTrace(t));
            throw t;
        } finally {
            if (reportWriter != null) {
                reportWriter.flush();
                reportWriter.close();
            }
        }
    }

    private static void run(String name, Runnable test) {
        log("Running " + name + "...");
        try {
            test.run();
            log("Passed " + name + ".");
        } catch (Throwable t) {
            log("FAILED " + name + ": " + t);
            log(stackTrace(t));
            throw t;
        }
    }

    private static PrintWriter openReportWriter() {
        try {
            Path report = Path.of("build", "reports", "testsuite.log");
            Files.createDirectories(report.getParent());
            return new PrintWriter(Files.newBufferedWriter(report));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to open TestSuite report writer", e);
        }
    }

    private static void log(String message) {
        System.out.println(message);
        if (reportWriter != null) {
            reportWriter.println(message);
            reportWriter.flush();
        }
    }

    private static String stackTrace(Throwable t) {
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        t.printStackTrace(writer);
        writer.flush();
        return buffer.toString();
    }
}
