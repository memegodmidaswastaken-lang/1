package dev.dominioncore;

import dev.dominioncore.app.FunctionalCheck;
import dev.dominioncore.app.PrototypeCommands;
import dev.dominioncore.app.SeedData;
import dev.dominioncore.bloodline.Bloodline;
import dev.dominioncore.config.RuntimeConfigLoader;
import dev.dominioncore.core.ScalingFormula;
import dev.dominioncore.dominion.Dominion;
import dev.dominioncore.dominion.DominionType;
import dev.dominioncore.dominion.acquisition.AcquisitionContext;
import dev.dominioncore.dominion.acquisition.AcquisitionPath;
import dev.dominioncore.faction.Faction;
import dev.dominioncore.faction.FactionRank;
import dev.dominioncore.gui.BloodlineNode;
import dev.dominioncore.gui.BloodlineOption;
import dev.dominioncore.gui.BloodlineSelectionFrame;
import dev.dominioncore.mod.ModLoaderTarget;
import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.religion.Religion;
import dev.dominioncore.runtime.DominionRuntime;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Launcher for DominionCore prototype with integrated functional systems.
 */
public final class DominionCoreApp {
    private DominionCoreApp() {
    }

    public static void main(String[] args) {
        List<BloodlineOption> options = loadOptions();
        FunctionalCheck.runOrThrow(options);

        DominionRuntime runtime = new DominionRuntime();
        runtime.setBalanceConfig(new RuntimeConfigLoader().load(java.nio.file.Paths.get("runtime", "balance.properties")));
        runtime.setPreferredLoader(ModLoaderTarget.FORGE);
        runSystemSimulation(runtime, options);

        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Headless environment detected. Functional checks passed; skipping Swing launch.");
            return;
        }

        BloodlineSelectionFrame.launch(options);
    }

    private static void runSystemSimulation(DominionRuntime runtime, List<BloodlineOption> options) {
        PlayerProgression player = new PlayerProgression("player-one");
        player.setActiveBloodlineId(options.get(0).bloodline().id());

        runtime.onKill(player, 60);
        runtime.onKill(player, 30);

        Map<String, BloodlineNode> treeById = new LinkedHashMap<>();
        for (BloodlineNode node : options.get(0).nodes()) {
            treeById.put(node.id(), node);
        }

        boolean unlocked = runtime.unlockNode(player, treeById, options.get(0).nodes().get(0).id());

        Dominion authorityDominion = new Dominion(
                "authority",
                "Authority",
                DominionType.LEADERSHIP,
                new ScalingFormula("Members * 2 + Chunks * 3 + Online * 5"),
                "Faction leader"
        );

        double authorityPower = runtime.computeDominionPower(authorityDominion, Map.of(
                "Members", 12.0,
                "Chunks", 28.0,
                "Online", 7.0
        ));

        Dominion bloodDominion = new Dominion(
                "blood_god",
                "Blood God",
                DominionType.BLOOD,
                new ScalingFormula("Kills * 3 + Survived * 1"),
                "Win PvP battles"
        );

        AcquisitionContext context = new AcquisitionContext(15, 9, 20, true, FactionRank.LEADER, false);
        boolean acquiredAuthority = runtime.acquireDominion(player, authorityDominion, context, AcquisitionPath.ACHIEVEMENT_UNLOCK);
        boolean acquiredBlood = runtime.acquireDominion(player, bloodDominion, context, AcquisitionPath.RITUAL);
        boolean switched = runtime.switchPrimaryDominion(player, 1_000);

        runtime.declareWar("night_court", "sun_tribe");
        boolean warActive = runtime.isAtWar("sun_tribe", "night_court");

        double weaponMult = runtime.pvpWeaponMultiplier(35);
        double armorRes = runtime.pvpArmorResistance(4);
        double antiOneShot = runtime.antiOneShotFactor(40, 20);
        int killBonus = runtime.highValueKillBonus(160);
        double territoryAtk = runtime.territoryAttackBonus(true, false);
        double territoryDef = runtime.territoryDefenseBonus(true, true);

        double factionAuthority = runtime.authority(new Faction("f1", "Night Court", 15, 42, 8, 10));
        int faith = runtime.faith(new Religion("r1", "Nox", 48, 3, 19, 5));
        int taxIncome = runtime.factionTaxIncome(15, 3, 2);
        int treasuryAfterWar = runtime.factionTreasuryAfterWarUpkeep(500, 2);
        var blessingTier = runtime.blessingTier(faith);
        boolean smite = runtime.canSmite(faith);
        boolean resurrect = runtime.canResurrect(faith);

        String cmd1 = PrototypeCommands.execute(runtime, player, "grantblood 15");
        String cmd2 = PrototypeCommands.execute(runtime, player, "grantdominion void_lord");
        String cmd3 = PrototypeCommands.execute(runtime, player, "achievements");
        String cmd4 = PrototypeCommands.execute(runtime, player, "forgeready");

        java.nio.file.Path savePath = java.nio.file.Paths.get("runtime", "player-one.json");
        runtime.savePlayerProgression(savePath, player);
        PlayerProgression loaded = runtime.loadPlayerProgression(savePath);

        runtime.triggerWorldEvent("ASCENSION", player.playerId(), "Unlocked dual dominion state");
        runtime.triggerWorldEvent("WEATHER_SHIFT", "night_court", "Forced crimson storm over territory");
        int worldEvents = runtime.totalWorldEvents();

        String topFaction = runtime.topAuthority(1).isEmpty() ? "none" : runtime.topAuthority(1).get(0).id();
        String topReligion = runtime.topFaith(1).isEmpty() ? "none" : runtime.topFaith(1).get(0).id();
        String topKiller = runtime.topKills(1).isEmpty() ? "none" : runtime.topKills(1).get(0).id();


        System.out.println("Simulation => blood=" + player.resources().getOrDefault("blood", 0)
                + ", firstNodeUnlocked=" + unlocked
                + ", dominionPower=" + authorityPower
                + ", acquiredAuthority=" + acquiredAuthority
                + ", acquiredBlood=" + acquiredBlood
                + ", switchedPrimary=" + switched
                + ", primary=" + player.primaryDominionId()
                + ", secondary=" + player.secondaryDominionId()
                + ", warActive=" + warActive
                + ", weaponMult=" + weaponMult
                + ", armorRes=" + armorRes
                + ", antiOneShot=" + antiOneShot
                + ", killBonus=" + killBonus
                + ", territoryAtk=" + territoryAtk
                + ", territoryDef=" + territoryDef
                + ", factionAuthority=" + factionAuthority
                + ", taxIncome=" + taxIncome
                + ", treasuryAfterWar=" + treasuryAfterWar
                + ", faith=" + faith
                + ", miracleTier=" + runtime.miracleTier(faith)
                + ", blessingTier=" + blessingTier
                + ", canSmite=" + smite
                + ", canResurrect=" + resurrect
                + ", cmd1='" + cmd1 + "'"
                + ", cmd2='" + cmd2 + "'"
                + ", cmd3='" + cmd3 + "'"
                + ", cmd4='" + cmd4 + "'"
                + ", loadedBlood=" + loaded.resources().getOrDefault("blood", 0)
                + ", loadedPrimary='" + loaded.primaryDominionId() + "'"
                + ", maxPowerCap=" + runtime.balanceConfig().maxDominionPower()
                + ", maxWeaponCap=" + runtime.balanceConfig().maxWeaponMultiplier()
                + ", hardcoreMode=" + runtime.balanceConfig().hardcoreMode()
                + ", worldEvents=" + worldEvents
                + ", topFaction='" + topFaction + "'"
                + ", topReligion='" + topReligion + "'"
                + ", topKiller='" + topKiller + "'"
                + ", loader='" + runtime.preferredLoader() + "'");
    }

    private static List<BloodlineOption> loadOptions() {
        BloodlineOption bloodborn = SeedData.loadBloodbornOption();

        Bloodline eclipse = new Bloodline(
                "eclipse",
                "Eclipse Warden",
                "Thrives at night with burst mobility and corruption pressure in hostile territory.",
                List.of("Nightcloak"),
                List.of("Shadow Leap"),
                "scales_at_night",
                "corruption"
        );

        BloodlineOption eclipseOption = new BloodlineOption(
                eclipse,
                "Night burst and zone pressure",
                "Daylight sustain penalty",
                4,
                false,
                List.of(
                        new BloodlineNode("shadow_leap", new Point(120, 150), 30, List.of(), "Short-range blink"),
                        new BloodlineNode("gloom_armor", new Point(300, 200), 65, List.of("shadow_leap"), "Temporary defense"),
                        new BloodlineNode("voidstep", new Point(500, 150), 95, List.of("gloom_armor"), "Phasing movement")
                )
        );

        return List.of(bloodborn, eclipseOption);
    }
}
