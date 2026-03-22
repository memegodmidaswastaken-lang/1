package dev.dominioncore.mod;

public final class ForgeNetworkPacketStubTest {
    private ForgeNetworkPacketStubTest() {
    }

    public static void runAll() {
        ForgeNetworkPacketStub stub = new ForgeNetworkPacketStub();

        check(stub.channelName().equals("dominioncore:main"), "Channel name should match dominioncore namespace");
        check(stub.documentsCorePackets(), "Stub should document core packet flow");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
