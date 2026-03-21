package dev.dominioncore.religion;

import dev.dominioncore.religion.blessing.BlessingTier;

public final class ReligionProgressionService {

    public int convertFollowers(int currentFollowers, int newlyConverted) {
        return Math.max(0, currentFollowers + Math.max(0, newlyConverted));
    }

    public BlessingTier blessingTierForFaith(int faith) {
        if (faith >= 300) {
            return BlessingTier.EXALTED;
        }
        if (faith >= 150) {
            return BlessingTier.DEVOUT;
        }
        if (faith >= 50) {
            return BlessingTier.INITIATE;
        }
        return BlessingTier.NONE;
    }

    public boolean canSmite(int faith) {
        return faith >= 120;
    }

    public boolean canResurrect(int faith) {
        return faith >= 400;
    }
}
