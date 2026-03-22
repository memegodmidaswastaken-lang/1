package dev.dominioncore.mod;

/**
 * Prototype-safe placeholder for future Forge packet registration.
 */
public final class ForgeNetworkPacketStub {
    public String channelName() {
        return "dominioncore:main";
    }

    public String packetPlan() {
        return "ForgeNetworkPacketStub[unlock_node, switch_dominion, sync_progression]";
    }

    public boolean documentsCorePackets() {
        return packetPlan().contains("unlock_node")
                && packetPlan().contains("sync_progression");
    }
}
