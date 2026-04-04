package dev.dominioncore.mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks Forge integration readiness tasks for the prototype.
 */
public final class ForgeReadinessReport {
    public record ChecklistItem(String key, boolean complete) {
    }

    public List<ChecklistItem> checklist() {
        List<ChecklistItem> items = new ArrayList<>();
        items.add(new ChecklistItem("forge_metadata", true));
        items.add(new ChecklistItem("forge_loader_target", true));
        items.add(new ChecklistItem("forge_mod_entrypoint", true));
        items.add(new ChecklistItem("forge_event_bus_hooks", true));
        items.add(new ChecklistItem("forge_network_packets", true));
        items.add(new ChecklistItem("forge_server_persistence_hooks", true));
        return items;
    }

    public long completeCount() {
        return checklist().stream().filter(ChecklistItem::complete).count();
    }

    public long pendingCount() {
        return checklist().size() - completeCount();
    }

    public String summarize() {
        return "ForgeReadiness{" +
                "complete=" + completeCount() +
                ", pending=" + pendingCount() +
                ", items=" + checklist() +
                '}';
    }
}
