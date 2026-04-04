package dev.dominioncore.core;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight linear formula evaluator for expressions like:
 * Members * 2 + Chunks * 3 + Online * 5
 */
public final class ScalingFormula {
    private static final Pattern TERM = Pattern.compile("([A-Za-z_][A-Za-z0-9_]*)\\s*\\*\\s*(-?\\d+(?:\\.\\d+)?)");

    private final String expression;

    public ScalingFormula(String expression) {
        this.expression = expression;
    }

    public String expression() {
        return expression;
    }

    public double evaluate(Map<String, Double> variables) {
        String[] terms = expression.split("\\+");
        double sum = 0;
        for (String raw : terms) {
            String term = raw.trim();
            Matcher matcher = TERM.matcher(term);
            if (matcher.matches()) {
                String variable = matcher.group(1);
                double coefficient = Double.parseDouble(matcher.group(2));
                sum += variables.getOrDefault(variable, 0.0) * coefficient;
                continue;
            }
            if (term.matches("-?\\d+(?:\\.\\d+)?")) {
                sum += Double.parseDouble(term);
                continue;
            }
            throw new IllegalArgumentException("Unsupported term in expression: '" + term + "'");
        }
        return sum;
    }
}
