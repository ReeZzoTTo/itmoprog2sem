package com.andreysankov.itmoprog2sem.client.gui;

import com.andreysankov.itmoprog2sem.common.models.LabWork;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizationPanel extends JPanel {
    private final List<LabWork> labWorks = new ArrayList<>();
    private final Map<Long, Rectangle> objectBounds = new HashMap<>();
    private final Map<Long, Double> animationProgress = new HashMap<>();

    private final LocalizationManager localization;

    private final String currentUser;
    private final ObjectEditListener editListener;

    private final Timer animationTimer;

    public VisualizationPanel(
        String currentUser,
        ObjectEditListener editListener,
        LocalizationManager localization
    ) {
        this.currentUser = currentUser;
        this.editListener = editListener;
        this.localization = localization;

        setPreferredSize(new Dimension(500, 350));
        setBackground(Color.WHITE);

        animationTimer = new Timer(35, e -> updateAnimation());
        animationTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }
        });
    }

    public void setLabWorks(List<LabWork> newLabWorks) {
        labWorks.clear();

        if (newLabWorks != null) {
            for (LabWork labWork : newLabWorks) {
                labWorks.add(labWork);

                animationProgress.putIfAbsent(labWork.getId(), 0.0);
            }
        }

        animationProgress.keySet().removeIf(id ->
                labWorks.stream().noneMatch(labWork -> labWork.getId() == id)
        );

        repaint();
    }

    private void updateAnimation() {
        boolean changed = false;

        for (LabWork labWork : labWorks) {
            double progress = animationProgress.getOrDefault(labWork.getId(), 1.0);

            if (progress < 1.0) {
                animationProgress.put(labWork.getId(), Math.min(1.0, progress + 0.05));
                changed = true;
            }
        }

        if (changed) {
            repaint();
        }
    }

    private void handleClick(MouseEvent e) {
        for (LabWork labWork : labWorks) {
            Rectangle rectangle = objectBounds.get(labWork.getId());

            if (rectangle != null && rectangle.contains(e.getPoint())) {
                if (e.getClickCount() == 2 && currentUser.equals(labWork.getOwnerLogin())) {
                    editListener.edit(labWork);
                } else {
                    showInfo(labWork);
                }

                return;
            }
        }
    }

    private void showInfo(LabWork labWork) {
        JOptionPane.showMessageDialog(
                this,
                """
                ID: %s
                %s: %s
                %s: %s
                X: %s
                Y: %s
                Minimal point: %s
                Max qualities: %s
                Difficulty: %s
                """.formatted(
                        labWork.getId(),
                        localization.get("table.name"),
                        labWork.getName(),
                        localization.get("visualization.owner"),
                        labWork.getOwnerLogin(),
                        labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getX(),
                        labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getY(),
                        labWork.getMinimalPoint(),
                        labWork.getPersonalQualitiesMaximum(),
                        labWork.getDifficulty()
                ),
                localization.get("visualization.info_title"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        objectBounds.clear();

        drawGrid(g2);

        for (LabWork labWork : labWorks) {
            drawLabWork(g2, labWork);
        }
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(235, 235, 235));

        int step = 50;

        for (int x = 0; x < getWidth(); x += step) {
            g2.drawLine(x, 0, x, getHeight());
        }

        for (int y = 0; y < getHeight(); y += step) {
            g2.drawLine(0, y, getWidth(), y);
        }

        g2.setColor(Color.GRAY);
        g2.drawString(localization.get("visualization.title"), 10, 20);
    }

    private void drawLabWork(Graphics2D g2, LabWork labWork) {
        if (labWork.getCoordinates() == null) {
            return;
        }

        int baseX = Math.toIntExact(labWork.getCoordinates().getX());
        int baseY = labWork.getCoordinates().getY();

        int x = Math.floorMod(baseX, Math.max(1, getWidth() - 80)) + 20;
        int y = Math.floorMod(baseY, Math.max(1, getHeight() - 80)) + 30;

        int size = calculateSize(labWork);

        double progress = animationProgress.getOrDefault(labWork.getId(), 1.0);
        int animatedSize = Math.max(8, (int) (size * progress));

        int drawX = x - animatedSize / 2;
        int drawY = y - animatedSize / 2;

        Color color = getColorForOwner(labWork.getOwnerLogin());

        g2.setColor(color);
        g2.fillOval(drawX, drawY, animatedSize, animatedSize);

        if (currentUser.equals(labWork.getOwnerLogin())) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(drawX, drawY, animatedSize, animatedSize);
        }

        g2.setColor(Color.BLACK);
        FontMetrics metrics = g2.getFontMetrics();
        String text = String.valueOf(labWork.getId());
        int textX = drawX + animatedSize / 2 - metrics.stringWidth(text) / 2;
        int textY = drawY + animatedSize / 2 + metrics.getAscent() / 2 - 2;
        g2.drawString(text, textX, textY);

        objectBounds.put(
                labWork.getId(),
                new Rectangle(drawX, drawY, animatedSize, animatedSize)
        );
    }

    private int calculateSize(LabWork labWork) {
        Double maxQualities = labWork.getPersonalQualitiesMaximum();

        if (maxQualities != null) {
            return Math.max(25, Math.min(90, maxQualities.intValue()));
        }

        return Math.max(25, Math.min(90, labWork.getMinimalPoint() * 3));
    }

    private Color getColorForOwner(String owner) {
        int hash = owner == null ? 0 : Math.abs(owner.hashCode());

        int red = 80 + hash % 140;
        int green = 80 + hash / 7 % 140;
        int blue = 80 + hash / 13 % 140;

        return new Color(red, green, blue);
    }

    public interface ObjectEditListener {
        void edit(LabWork labWork);
    }
}