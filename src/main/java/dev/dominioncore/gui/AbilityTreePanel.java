package dev.dominioncore.gui;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Renders an RPG-like ability tree and supports hover + click selection.
 */
public final class AbilityTreePanel extends JPanel {
    private static final int NODE_RADIUS = 32;

    private final List<BloodlineNode> nodes;
    private final Map<String, BloodlineNode> byId;

    private String selectedNodeId;
    private String hoveredNodeId;

    public AbilityTreePanel(List<BloodlineNode> nodes) {
        this.nodes = nodes;
        this.byId = new HashMap<>();
        for (BloodlineNode node : nodes) {
            byId.put(node.id(), node);
        }
        setOpaque(false);
        setToolTipText("ability-tree");
        ToolTipManager.sharedInstance().registerComponent(this);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoveredNodeId = findNodeAt(e.getPoint()).map(BloodlineNode::id).orElse(null);
                setCursor(hoveredNodeId == null ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    selectedNodeId = findNodeAt(e.getPoint()).map(BloodlineNode::id).orElse(null);
                    repaint();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    hoveredNodeId = findNodeAt(e.getPoint()).map(BloodlineNode::id).orElse(null);
                    repaint();
                }
            }
        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        Optional<BloodlineNode> node = findNodeAt(event.getPoint());
        if (node.isEmpty()) {
            return null;
        }
        BloodlineNode value = node.get();
        return "<html><b>" + value.id() + "</b><br/>" + value.description() + "<br/>Cost: " + value.cost() + "<br/>Requires: "
                + (value.requires().isEmpty() ? "None" : String.join(", ", value.requires())) + "</html>";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(65, 95, 130));
        g2.setStroke(new BasicStroke(3f));
        for (BloodlineNode node : nodes) {
            for (String dependency : node.requires()) {
                BloodlineNode dep = byId.get(dependency);
                if (dep != null) {
                    Point a = dep.position();
                    Point b = node.position();
                    g2.drawLine(a.x, a.y, b.x, b.y);
                }
            }
        }

        for (BloodlineNode node : nodes) {
            Point p = node.position();
            boolean hovered = node.id().equals(hoveredNodeId);
            boolean selected = node.id().equals(selectedNodeId);

            g2.setColor(selected ? new Color(48, 108, 63) : new Color(18, 30, 44));
            g2.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            g2.setColor(selected ? new Color(159, 233, 174) : hovered ? new Color(176, 220, 255) : new Color(111, 168, 220));
            g2.setStroke(new BasicStroke(hovered ? 3.5f : 2.5f));
            g2.drawOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

            g2.setColor(new Color(220, 230, 245));
            g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
            g2.drawString(node.id(), p.x - NODE_RADIUS, p.y + NODE_RADIUS + 16);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 11f));
            g2.drawString("Cost: " + node.cost(), p.x - NODE_RADIUS, p.y + NODE_RADIUS + 30);
        }

        if (selectedNodeId != null) {
            g2.setColor(new Color(205, 225, 250));
            g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
            g2.drawString("Selected node: " + selectedNodeId + "  (left-click another node to change)", 18, getHeight() - 16);
        }

        g2.dispose();
    }

    private Optional<BloodlineNode> findNodeAt(Point point) {
        for (BloodlineNode node : nodes) {
            Point nodePoint = node.position();
            int dx = point.x - nodePoint.x;
            int dy = point.y - nodePoint.y;
            if ((dx * dx) + (dy * dy) <= NODE_RADIUS * NODE_RADIUS) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }
}
