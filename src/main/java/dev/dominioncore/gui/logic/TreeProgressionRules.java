package dev.dominioncore.gui.logic;

import dev.dominioncore.gui.BloodlineNode;

import java.util.List;
import java.util.Set;

public final class TreeProgressionRules {
    private TreeProgressionRules() {
    }

    public static boolean canUnlock(BloodlineNode node, Set<String> unlockedNodes) {
        return unlockedNodes.containsAll(node.requires());
    }

    public static boolean graphIsValid(List<BloodlineNode> nodes) {
        Set<String> ids = nodes.stream().map(BloodlineNode::id).collect(java.util.stream.Collectors.toSet());
        return nodes.stream().allMatch(node -> ids.containsAll(node.requires()));
    }
}
