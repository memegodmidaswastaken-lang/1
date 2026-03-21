package dev.dominioncore.mod;

public final class ForgeEventBusHooksStubTest {
    private ForgeEventBusHooksStubTest() {
    }

    public static void runAll() {
        ForgeEventBusHooksStub stub = new ForgeEventBusHooksStub();

        check(stub.registrationPlan().contains("player_login"), "Registration plan should mention player login");
        check(stub.documentsCoreHooks(), "Stub should document core hooks");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
