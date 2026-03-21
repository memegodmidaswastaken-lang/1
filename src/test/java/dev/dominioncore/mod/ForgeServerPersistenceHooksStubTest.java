package dev.dominioncore.mod;

public final class ForgeServerPersistenceHooksStubTest {
    private ForgeServerPersistenceHooksStubTest() {
    }

    public static void runAll() {
        ForgeServerPersistenceHooksStub stub = new ForgeServerPersistenceHooksStub();

        check(stub.persistencePlan().contains("player_load"), "Persistence plan should mention player load");
        check(stub.documentsSaveLifecycle(), "Stub should document save lifecycle");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
