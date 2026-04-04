package dev.dominioncore.economy;

public final class FactionEconomyService {

    public int computeTickTaxIncome(int members, int baseTaxPerMember, int treasuryLevel) {
        int multiplier = Math.max(1, treasuryLevel);
        return Math.max(0, members) * Math.max(0, baseTaxPerMember) * multiplier;
    }

    public int applyWarUpkeep(int treasury, int activeWarCount) {
        int upkeep = Math.max(0, activeWarCount) * 50;
        return Math.max(0, treasury - upkeep);
    }

    public int deposit(int treasury, int amount) {
        return Math.max(0, treasury + Math.max(0, amount));
    }

    public int withdraw(int treasury, int amount) {
        return Math.max(0, treasury - Math.max(0, amount));
    }
}
