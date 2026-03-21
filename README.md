# DominionCore (UI Prototype Slice)

DominionCore is planned as a modular Minecraft progression mod with bloodlines, dominions, factions, religion, PvP scaling, modern GUIs, and scriptable expansion.

## IntelliJ IDEA setup

1. Install **JDK 21** (Temurin recommended).
2. Open this project in IntelliJ as a **Gradle** project.
3. Set **Project SDK** and **Gradle JVM** to Java 21.
4. Run `DominionCoreApp.main()` to execute functional checks and launch the UI when a display is available.

## Implemented now: richer UI slice

- Full-window **Bloodline Selection** frame with animated backdrop.
- Search + unlocked-only filtering for bloodline list.
- Overview tab with:
  - description
  - strengths/weaknesses
  - difficulty
  - scaling condition + resource labels
  - choose/select action
  - animated character-preview placeholder
- Ability Tree tab with:
  - branching node connections
  - node cost labels
  - hover tooltips
  - click-to-select node state

## Included domain foundations

- `Bloodline`
- `Dominion`
- `DominionEngine`
- `ScalingFormula`

## Recommended next steps

1. Replace in-memory sample options with JSON loading + validation.
2. Move this UI structure into Minecraft screens while keeping panel separation.
3. Add unlock requirements evaluation + left-click unlock flow.
4. Add right-click preview action and keybind legend.
5. Add dominion manager + faction/religion UI tabs using the same style system.


## Functional confirmation behavior

- App startup performs functional checks (filtering + tree dependency validation).
- In headless environments, startup exits gracefully after checks instead of crashing on Swing window creation.


## Troubleshooting compile errors (important)

If you see errors like `package org.junit.jupiter.api does not exist` coming from `src/main/java/...`, then test code was accidentally pasted into a main source file.

Expected layout:
- Main class: `src/main/java/dev/dominioncore/core/ScalingFormula.java` (no JUnit imports)
- Test class: `src/test/java/dev/dominioncore/core/ScalingFormulaTest.java` (contains `@Test` and JUnit assertions)

Quick checks:
- `./scripts/verify_layout.sh`
- `javac -d out $(find src/main/java -name '*.java')`

If your IDE still shows stale errors, refresh Gradle project and invalidate caches/restart.


- Prototype seed loading now uses `src/main/java/dev/dominioncore/app/SeedData.java` (no `dev.dominioncore.io` dependency in app startup).

- Compatibility shim: `src/main/java/dev/dominioncore/io/JsonSeedLoader.java` now delegates to `SeedData` to support older imports safely.


## Integrated prototype systems now included

- Bloodline progression unlock service with resource cost checks.
- Dominion runtime orchestration for bloodline, dominion, faction authority, religion faith, and script events.
- Faction authority formula service and religion faith/miracle-tier service.
- Script-like event bridge (`on_kill_add_blood`) for extensible progression triggers.

- Dominion acquisition paths now modeled: starter, achievement, ritual, faction promotion, divine blessing.
- Dominion ownership now supports primary/secondary slots and switch cooldown behavior.

- PvP scaling service now models weapon growth, armor resistance growth, anti-one-shot clamping, and high-value kill bonus.
- Territory combat bonuses now apply distinct attack/defense multipliers for owned and holy land.
- Faction war state service now supports declare/end/isAtWar checks.
- Faction rank permissions now gate promotion-based dominion acquisition (member/officer/leader).

- Faction economy service now supports tax income ticks, treasury deposit/withdraw, and war upkeep drains.
- Religion progression now supports follower conversion, blessing tiers, and smite/resurrection unlock checks.

- Prototype command helpers now support quick local actions (`grantblood`, `grantdominion`, `switchdominion`, `status`, `loader`, `forgeready`, `savestate`, `loadstate`).
- Runtime status report helper now summarizes player progression/dominion/faith state for diagnostics.
- Player progression persistence now supports save/load snapshots via `PlayerProgressionStore` for prototype restart continuity.
- Runtime balance config now supports toggles/caps (dominions enabled, hardcore mode, power caps, PvP cap values) via `RuntimeConfigLoader`.
- World event service now supports triggering/logging recent global events (e.g., ascension and weather shifts).
- Leaderboard service now tracks top authority/faith/kill rankings for runtime diagnostics.
- Forge mod entrypoint stub now exists to anchor upcoming JavaFML event-bus and packet wiring work.
- Forge event-bus hook stub now documents planned login/logout, tick, and death integration points.
- Forge network packet stub now documents unlock/switch/sync packet flow for upcoming multiplayer wiring.
- Forge server persistence hook stub now documents load/save/logout/stop lifecycle for player progression storage.
- Forge readiness report now tracks pending integration tasks (entrypoint, event bus hooks, packets, persistence hooks).
- Prototype commands now support save/load snapshots so local sessions can round-trip progression state quickly.
- Prototype server session service now supports login/load, logout/save, save-all, and kill routing over persisted player sessions.
- Achievement tracking now unlocks milestone badges for kills, dual-dominion progress, and world-event participation.


## Mod packaging metadata (launcher compatibility)

This repository now includes loader metadata so built jars are recognized as mods by common launchers:
- **Primary target: Forge** metadata in `src/main/resources/META-INF/mods.toml`
- Fabric metadata (`src/main/resources/fabric.mod.json`) retained as compatibility placeholder while Forge is the active target
- Resource pack metadata: `src/main/resources/pack.mcmeta`

> Note: this is still a prototype codebase (not yet wired to a real Minecraft loader runtime API), but loader targeting in runtime defaults to Forge and metadata reflects that direction.
