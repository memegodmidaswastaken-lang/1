package dev.dominioncore.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lightweight key=value config loader for prototype runtime balancing.
 */
public final class RuntimeConfigLoader {

    public RuntimeBalanceConfig load(Path path) {
        RuntimeBalanceConfig defaults = RuntimeBalanceConfig.defaults();

        if (!Files.exists(path)) {
            return defaults;
        }

        boolean dominionsEnabled = defaults.dominionsEnabled();
        boolean hardcoreMode = defaults.hardcoreMode();
        double maxDominionPower = defaults.maxDominionPower();
        double maxWeaponMultiplier = defaults.maxWeaponMultiplier();
        int maxHighValueKillBonus = defaults.maxHighValueKillBonus();

        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }
                String[] kv = trimmed.split("=", 2);
                String key = kv[0].trim();
                String value = kv[1].trim();

                switch (key) {
                    case "dominionsEnabled" -> dominionsEnabled = Boolean.parseBoolean(value);
                    case "hardcoreMode" -> hardcoreMode = Boolean.parseBoolean(value);
                    case "maxDominionPower" -> maxDominionPower = Double.parseDouble(value);
                    case "maxWeaponMultiplier" -> maxWeaponMultiplier = Double.parseDouble(value);
                    case "maxHighValueKillBonus" -> maxHighValueKillBonus = Integer.parseInt(value);
                    default -> {
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config: " + path, e);
        }

        return new RuntimeBalanceConfig(
                dominionsEnabled,
                hardcoreMode,
                maxDominionPower,
                maxWeaponMultiplier,
                maxHighValueKillBonus
        );
    }
}
