package dev.dominioncore.gui;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import dev.dominioncore.gui.logic.BloodlineFilter;


/**
 * Full-screen inspired bloodline menu with filtering, details and an interactive tree preview.
 */
public final class BloodlineSelectionFrame extends JFrame {
    private final List<BloodlineOption> options;
    private final DefaultListModel<BloodlineOption> visibleModel = new DefaultListModel<>();

    private final JLabel title = new JLabel("Bloodline", SwingConstants.LEFT);
    private final JTextArea description = new JTextArea();
    private final JTextArea traits = new JTextArea();
    private final JLabel difficulty = new JLabel();
    private final JLabel scaling = new JLabel();
    private final JLabel resource = new JLabel();
    private final JPanel previewPanel;

    private final JPanel treeContainer = new JPanel(new BorderLayout());
    private AbilityTreePanel treePanel;

    private float phase = 0f;

    public BloodlineSelectionFrame(List<BloodlineOption> options) {
        super("DominionCore - Bloodline Selection");
        this.options = options;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1320, 760));
        setLocationByPlatform(true);

        JPanel root = new AnimatedBackgroundPanel();
        root.setLayout(new BorderLayout(16, 16));
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(new Color(238, 245, 255));

        JTextField searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search bloodline...");

        JButton unlockedOnly = new JButton("Unlocked Only: Off");
        unlockedOnly.putClientProperty("filterUnlocked", Boolean.FALSE);
        unlockedOnly.addActionListener(e -> {
            boolean current = (boolean) unlockedOnly.getClientProperty("filterUnlocked");
            unlockedOnly.putClientProperty("filterUnlocked", !current);
            unlockedOnly.setText(!current ? "Unlocked Only: On" : "Unlocked Only: Off");
            applyFilter(searchField.getText(), !current);
        });

        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(unlockedOnly, BorderLayout.EAST);

        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setOpaque(false);
        left.add(searchPanel, BorderLayout.NORTH);

        JList<BloodlineOption> list = new JList<>(visibleModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new BloodlineListRenderer());
        list.setBorder(BorderFactory.createTitledBorder("Bloodlines"));
        left.add(new JScrollPane(list), BorderLayout.CENTER);
        left.setPreferredSize(new Dimension(360, 200));

        JPanel overviewTab = new JPanel(new BorderLayout(10, 10));
        overviewTab.setOpaque(false);

        description.setEditable(false);
        description.setOpaque(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setForeground(new Color(210, 224, 245));

        traits.setEditable(false);
        traits.setOpaque(false);
        traits.setLineWrap(true);
        traits.setWrapStyleWord(true);
        traits.setForeground(new Color(182, 203, 230));

        JPanel statRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        statRow.setOpaque(false);
        difficulty.setForeground(new Color(255, 220, 170));
        difficulty.setFont(difficulty.getFont().deriveFont(Font.BOLD, 15f));
        scaling.setForeground(new Color(162, 224, 206));
        resource.setForeground(new Color(197, 216, 255));
        statRow.add(difficulty);
        statRow.add(scaling);
        statRow.add(resource);

        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int body = 60;
                int orbit = 92;

                g2.setColor(new Color(15, 26, 39, 220));
                g2.fillOval(cx - 122, cy - 122, 244, 244);
                g2.setColor(new Color(114, 177, 245));
                g2.drawOval(cx - orbit, cy - orbit, orbit * 2, orbit * 2);

                int orbX = (int) (cx + Math.cos(phase) * orbit);
                int orbY = (int) (cy + Math.sin(phase) * orbit);
                g2.setColor(new Color(190, 95, 220));
                g2.fillOval(orbX - 10, orbY - 10, 20, 20);

                g2.setColor(new Color(230, 240, 252));
                g2.fillRoundRect(cx - body / 2, cy - body / 2, body, body, 11, 11);
                g2.dispose();
            }
        };
        previewPanel.setOpaque(false);
        previewPanel.setBorder(BorderFactory.createTitledBorder("Character Preview (placeholder)"));
        previewPanel.setPreferredSize(new Dimension(640, 280));

        JPanel textBlock = new JPanel(new BorderLayout(8, 8));
        textBlock.setOpaque(false);
        textBlock.add(description, BorderLayout.NORTH);
        textBlock.add(traits, BorderLayout.CENTER);
        textBlock.add(statRow, BorderLayout.SOUTH);

        JButton chooseButton = new JButton("Choose Bloodline");
        chooseButton.addActionListener(e -> {
            BloodlineOption selected = list.getSelectedValue();
            if (selected == null) {
                chooseButton.setText("Choose Bloodline");
                return;
            }
            chooseButton.setText(selected.unlocked() ? "Selected: " + selected.bloodline().displayName() : "Locked");
        });

        overviewTab.add(textBlock, BorderLayout.NORTH);
        overviewTab.add(previewPanel, BorderLayout.CENTER);
        overviewTab.add(chooseButton, BorderLayout.SOUTH);

        treeContainer.setOpaque(false);
        treeContainer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea treeHint = new JTextArea("Left-click node: select\nHover node: preview requirements\nRight-click keeps hover focus for quick comparison.");
        treeHint.setEditable(false);
        treeHint.setOpaque(false);
        treeHint.setForeground(new Color(187, 206, 230));

        JPanel treeTab = new JPanel(new BorderLayout(8, 8));
        treeTab.setOpaque(false);
        treeTab.add(treeHint, BorderLayout.NORTH);
        treeTab.add(treeContainer, BorderLayout.CENTER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Overview", overviewTab);
        tabs.addTab("Ability Tree", treeTab);

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setOpaque(false);
        right.add(title, BorderLayout.NORTH);
        right.add(tabs, BorderLayout.CENTER);

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);

        applyFilter("", false);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                BloodlineOption selected = list.getSelectedValue();
                if (selected != null) {
                    applySelected(selected);
                }
            }
        });

        if (!visibleModel.isEmpty()) {
            list.setSelectedIndex(0);
        }

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearch();
            }

            private void applySearch() {
                boolean unlocked = (boolean) unlockedOnly.getClientProperty("filterUnlocked");
                applyFilter(searchField.getText(), unlocked);
                if (!visibleModel.isEmpty() && list.getSelectedIndex() < 0) {
                    list.setSelectedIndex(0);
                }
            }
        });

        Timer timer = new Timer(33, e -> {
            phase += 0.04f;
            previewPanel.repaint();
            root.repaint();
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void applyFilter(String query, boolean unlockedOnly) {
        visibleModel.clear();
        for (BloodlineOption option : BloodlineFilter.apply(options, query, unlockedOnly)) {
            visibleModel.addElement(option);
        }
    }

    private void applySelected(BloodlineOption selected) {
        title.setText(selected.bloodline().displayName());
        description.setText(selected.bloodline().description());
        traits.setText("Strengths: " + selected.strengths() + "\nWeaknesses: " + selected.weaknesses());
        difficulty.setText("Difficulty: " + selected.difficulty() + "/5");
        scaling.setText("Scaling: " + selected.bloodline().scalingCondition());
        resource.setText("Resource: " + selected.bloodline().resourceId());

        treeContainer.removeAll();
        treePanel = new AbilityTreePanel(selected.nodes());
        treePanel.setPreferredSize(new Dimension(760, 420));
        treePanel.setBorder(BorderFactory.createTitledBorder("Bloodline Upgrade Tree"));
        treeContainer.add(treePanel, BorderLayout.CENTER);
        treeContainer.revalidate();
        treeContainer.repaint();
    }

    private static final class BloodlineListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus
        ) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            BloodlineOption option = (BloodlineOption) value;
            label.setText((option.unlocked() ? "🧬 " : "🔒 ") + option.bloodline().displayName());
            label.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));

            if (isSelected) {
                label.setBackground(new Color(27, 49, 72));
                label.setForeground(new Color(230, 240, 255));
            } else {
                label.setBackground(new Color(15, 23, 34));
                label.setForeground(option.unlocked() ? new Color(207, 220, 240) : new Color(120, 132, 148));
            }
            return label;
        }
    }

    private final class AnimatedBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint paint = new GradientPaint(
                    0, 0, new Color(7, 14, 24),
                    getWidth(), getHeight(), new Color(16, 33, 54)
            );
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int pulse = (int) (20 * (1 + Math.sin(phase * 0.8)));
            g2.setColor(new Color(55, 88, 130, 55 + pulse));
            g2.fillOval(getWidth() - 360, -120, 420, 420);
            g2.setColor(new Color(90, 30, 120, 45 + pulse));
            g2.fillOval(-150, getHeight() - 320, 420, 420);
            g2.dispose();
        }
    }

    public static void launch(List<BloodlineOption> options) {
        SwingUtilities.invokeLater(() -> new BloodlineSelectionFrame(options).setVisible(true));
    }
}
