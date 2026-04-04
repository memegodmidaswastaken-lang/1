package dev.dominioncore.core;

import java.util.Map;

public final class ScalingFormulaTest {
    private ScalingFormulaTest() {
    }

    public static void runAll() {
        evaluatesLinearExpression();
        supportsConstantTerms();
        rejectsUnsupportedTerms();
    }

    private static void evaluatesLinearExpression() {
        ScalingFormula formula = new ScalingFormula("Members * 2 + Chunks * 3 + Online * 5");
        double result = formula.evaluate(Map.of("Members", 10.0, "Chunks", 4.0, "Online", 2.0));
        check(result == 42.0, "Expected 42.0 but got " + result);
    }

    private static void supportsConstantTerms() {
        ScalingFormula formula = new ScalingFormula("Members * 2 + 10");
        double value = formula.evaluate(Map.of("Members", 3.0));
        check(value == 16.0, "Expected 16.0 but got " + value);
    }

    private static void rejectsUnsupportedTerms() {
        ScalingFormula formula = new ScalingFormula("Members / 2");
        boolean threw = false;
        try {
            formula.evaluate(Map.of("Members", 10.0));
        } catch (IllegalArgumentException ignored) {
            threw = true;
        }
        check(threw, "Expected IllegalArgumentException for unsupported expression");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
