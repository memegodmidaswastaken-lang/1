package dev.dominioncore.mod;

public final class ForgeModEntrypointStubTest {
    private ForgeModEntrypointStubTest() {
    }

    public static void runAll() {
        ForgeModEntrypointStub stub = new ForgeModEntrypointStub();

        check(stub.modId().equals("dominioncore"), "Mod ID should be dominioncore");
        check(stub.lifecycleStatus().contains("ForgeEntrypointStub"), "Lifecycle status should include stub marker");
        check(stub.lifecycleStatus().contains("loader='FORGE'"), "Loader should default to FORGE");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
