package dev.dominioncore.progression;

import dev.dominioncore.gui.BloodlineNode;
import dev.dominioncore.gui.logic.TreeProgressionRules;

import java.util.Map;
import java.util.Optional;

public final class BloodlineProgressionService {

    public boolean unlockNode(PlayerProgression player, Map<String, BloodlineNode> treeById, String nodeId, String resourceKey) {
        BloodlineNode node = Optional.ofNullable(treeById.get(nodeId))
                .orElseThrow(() -> new IllegalArgumentException("Unknown node: " + nodeId));

        if (player.unlockedNodes().contains(nodeId)) {
            return false;
        }
        if (!TreeProgressionRules.canUnlock(node, player.unlockedNodes())) {
            return false;
        }

        int available = player.resources().getOrDefault(resourceKey, 0);
        if (available < node.cost()) {
            return false;
        }

        player.resources().put(resourceKey, available - node.cost());
        player.unlockedNodes().add(nodeId);
        return true;
    }
}
