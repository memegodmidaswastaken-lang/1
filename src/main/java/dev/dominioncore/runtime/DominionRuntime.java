package dev.dominioncore.runtime;

import dev.dominioncore.achievement.AchievementService;
import dev.dominioncore.combat.PvpScalingService;
import dev.dominioncore.config.RuntimeBalanceConfig;
import dev.dominioncore.combat.TerritoryCombatService;
import dev.dominioncore.dominion.Dominion;
import dev.dominioncore.dominion.DominionEngine;
import dev.dominioncore.dominion.DominionOwnershipService;
import dev.dominioncore.dominion.acquisition.AcquisitionContext;
import dev.dominioncore.dominion.acquisition.AcquisitionPath;
import dev.dominioncore.dominion.acquisition.DominionAcquisitionService;
import dev.dominioncore.economy.FactionEconomyService;
import dev.dominioncore.faction.Faction;
import dev.dominioncore.faction.FactionService;
import dev.dominioncore.leaderboard.LeaderboardEntry;
import dev.dominioncore.leaderboard.LeaderboardService;
import dev.dominioncore.mod.ForgeReadinessReport;
import dev.dominioncore.mod.ModLoaderTarget;
import dev.dominioncore.persistence.PlayerProgressionStore;
import dev.dominioncore.progression.BloodlineProgressionService;
import dev.dominioncore.progression.PlayerProgression;
import dev.dominioncore.religion.Religion;
import dev.dominioncore.religion.ReligionProgressionService;
import dev.dominioncore.religion.ReligionService;
import dev.dominioncore.religion.blessing.BlessingTier;
import dev.dominioncore.script.SimpleScriptEngine;
import dev.dominioncore.war.FactionWarService;
import dev.dominioncore.world.WorldEventService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Coordinates major gameplay systems in one place for prototype execution.
 */
public final class DominionRuntime {
    private final DominionEngine dominionEngine = new DominionEngine();
    private final BloodlineProgressionService progressionService = new BloodlineProgressionService();
    private final DominionAcquisitionService acquisitionService = new DominionAcquisitionService();
    private final DominionOwnershipService ownershipService = new DominionOwnershipService();
    private final FactionService factionService = new FactionService();
    private final FactionEconomyService factionEconomyService = new FactionEconomyService();
    private final ReligionService religionService = new ReligionService();
    private final ReligionProgressionService religionProgressionService = new ReligionProgressionService();
    private final PvpScalingService pvpScalingService = new PvpScalingService();
    private final TerritoryCombatService territoryCombatService = new TerritoryCombatService();
    private final FactionWarService warService = new FactionWarService();
    private final SimpleScriptEngine scriptEngine = new SimpleScriptEngine();
    private final PlayerProgressionStore progressionStore = new PlayerProgressionStore();
    private final LeaderboardService leaderboardService = new LeaderboardService();
    private final WorldEventService worldEventService = new WorldEventService();
    private final AchievementService achievementService = new AchievementService();
    private final Map<String, Integer> killCountByPlayer = new ConcurrentHashMap<>();
    private RuntimeBalanceConfig balanceConfig = RuntimeBalanceConfig.defaults();
    private ModLoaderTarget preferredLoader = ModLoaderTarget.FORGE;
    private final ForgeReadinessReport forgeReadinessReport = new ForgeReadinessReport();

    public DominionRuntime() {
        scriptEngine.register("on_kill_add_blood", (player, amount) ->
                player.resources().merge("blood", amount, Integer::sum));
    }

    public double computeDominionPower(Dominion dominion, Map<String, Double> context) {
        if (!balanceConfig.dominionsEnabled()) {
            return 0.0;
        }
        double raw = dominionEngine.computePower(dominion, context);
        return Math.min(raw, balanceConfig.maxDominionPower());
    }

    public boolean unlockNode(PlayerProgression player, Map<String, dev.dominioncore.gui.BloodlineNode> treeById, String nodeId) {
        return progressionService.unlockNode(player, treeById, nodeId, "blood");
    }

    public Set<AcquisitionPath> availableAcquisitionPaths(Dominion dominion, AcquisitionContext context) {
        return acquisitionService.availablePaths(dominion, context);
    }

    public boolean acquireDominion(PlayerProgression player, Dominion dominion, AcquisitionContext context, AcquisitionPath path) {
        if (!acquisitionService.canAcquireVia(dominion, context, path)) {
            return false;
        }
        ownershipService.unlock(player, dominion.id());
        achievementService.recordDominionMilestone(player.playerId(), player.unlockedDominions().size());
        return true;
    }

    public boolean switchPrimaryDominion(PlayerProgression player, long nowEpochSeconds) {
        return ownershipService.switchPrimary(player, nowEpochSeconds);
    }

    public boolean validateDominionOwnership(PlayerProgression player) {
        return ownershipService.validateOwnership(player);
    }

    public double authority(Faction faction) {
        double value = factionService.computeAuthority(faction);
        leaderboardService.recordAuthority(faction.id(), value);
        return value;
    }

    public int factionTaxIncome(int members, int baseTaxPerMember, int treasuryLevel) {
        return factionEconomyService.computeTickTaxIncome(members, baseTaxPerMember, treasuryLevel);
    }

    public int factionTreasuryAfterWarUpkeep(int treasury, int activeWarCount) {
        return factionEconomyService.applyWarUpkeep(treasury, activeWarCount);
    }

    public int faith(Religion religion) {
        int value = religionService.computeFaith(religion);
        leaderboardService.recordFaith(religion.id(), value);
        return value;
    }

    public String miracleTier(int faith) {
        return religionService.unlockedMiracleTier(faith);
    }

    public BlessingTier blessingTier(int faith) {
        return religionProgressionService.blessingTierForFaith(faith);
    }

    public boolean canSmite(int faith) {
        return religionProgressionService.canSmite(faith);
    }

    public boolean canResurrect(int faith) {
        return religionProgressionService.canResurrect(faith);
    }

    public double pvpWeaponMultiplier(int weaponKills) {
        return Math.min(pvpScalingService.weaponMultiplier(weaponKills), balanceConfig.maxWeaponMultiplier());
    }

    public double pvpArmorResistance(int armorEvolutions) {
        return pvpScalingService.armorResistance(armorEvolutions);
    }

    public double antiOneShotFactor(double incomingDamage, double maxHealth) {
        return pvpScalingService.antiOneShotFactor(incomingDamage, maxHealth);
    }

    public int highValueKillBonus(int victimPowerLevel) {
        return Math.min(pvpScalingService.highValueKillBonus(victimPowerLevel), balanceConfig.maxHighValueKillBonus());
    }

    public double territoryAttackBonus(boolean inOwnedTerritory, boolean inHolyLand) {
        return territoryCombatService.territoryAttackBonus(inOwnedTerritory, inHolyLand);
    }

    public double territoryDefenseBonus(boolean inOwnedTerritory, boolean inHolyLand) {
        return territoryCombatService.territoryDefenseBonus(inOwnedTerritory, inHolyLand);
    }

    public void declareWar(String factionA, String factionB) {
        warService.declareWar(factionA, factionB);
    }

    public void endWar(String factionA, String factionB) {
        warService.endWar(factionA, factionB);
    }

    public boolean isAtWar(String factionA, String factionB) {
        return warService.isAtWar(factionA, factionB);
    }


    public void setBalanceConfig(RuntimeBalanceConfig balanceConfig) {
        this.balanceConfig = balanceConfig;
    }

    public RuntimeBalanceConfig balanceConfig() {
        return balanceConfig;
    }



    public void setPreferredLoader(ModLoaderTarget preferredLoader) {
        if (preferredLoader != null) {
            this.preferredLoader = preferredLoader;
        }
    }

    public ModLoaderTarget preferredLoader() {
        return preferredLoader;
    }

    public String forgeReadinessSummary() {
        return forgeReadinessReport.summarize();
    }

    public long forgeReadinessPending() {
        return forgeReadinessReport.pendingCount();
    }
    public void triggerWorldEvent(String eventType, String source, String details) {
        worldEventService.trigger(eventType, source, details);
        achievementService.recordWorldEventMilestone(source, worldEventService.totalEvents());
    }

    public java.util.List<String> recentWorldEvents(int limit) {
        return worldEventService.recent(limit);
    }

    public int totalWorldEvents() {
        return worldEventService.totalEvents();
    }

    public java.util.List<LeaderboardEntry> topAuthority(int limit) {
        return leaderboardService.topAuthority(limit);
    }

    public java.util.List<LeaderboardEntry> topFaith(int limit) {
        return leaderboardService.topFaith(limit);
    }

    public java.util.List<LeaderboardEntry> topKills(int limit) {
        return leaderboardService.topKills(limit);
    }

    public void savePlayerProgression(java.nio.file.Path path, PlayerProgression player) {
        progressionStore.save(path, player);
    }

    public PlayerProgression loadPlayerProgression(java.nio.file.Path path) {
        return progressionStore.load(path);
    }

    public void onKill(PlayerProgression player, int amount) {
        scriptEngine.emit("on_kill_add_blood", player, amount);
        int earnedKills = Math.max(1, amount / 10);
        leaderboardService.addKills(player.playerId(), earnedKills);
        int totalKills = killCountByPlayer.merge(player.playerId(), earnedKills, Integer::sum);
        achievementService.recordKillMilestone(player.playerId(), totalKills);
    }

    public java.util.List<String> achievements(String playerId) {
        return achievementService.listAchievements(playerId);
    }

    public boolean hasAchievement(String playerId, String achievementId) {
        return achievementService.hasAchievement(playerId, achievementId);
    }
}
