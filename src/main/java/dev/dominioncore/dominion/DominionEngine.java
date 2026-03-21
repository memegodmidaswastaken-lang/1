package dev.dominioncore.dominion;

import java.util.Map;

/**
 * Server-side stateless power calculation service.
 */
public final class DominionEngine {

    public double computePower(Dominion dominion, Map<String, Double> context) {
        return dominion.formula().evaluate(context);
    }
}
