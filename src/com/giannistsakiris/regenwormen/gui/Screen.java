package com.giannistsakiris.regenwormen.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class Screen extends JPanel {

    protected Graphics2D g2;
    private ImageIcon bgIcon;

    public Screen() {
        bgIcon = new ImageIcon(this.getClass().getResource("/images/wood.jpg"));
    }

    @Override
    final protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g2 = (Graphics2D) g;
        drawBackground();
        paintScreen();
    }

    protected abstract void paintScreen();

    private void drawBackground() {
        g2.setColor(new Color(0x008000));
        g2.fillRect(0,0,getWidth(),getHeight());
//        g2.drawImage(bgIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    protected void drawBorderedString(String text, int x, int y, Color fgColor, Color bgColor) {
        drawBorderedString(text, x, y, fgColor, bgColor, false);
    }

    protected void drawBorderedString(String text, int x, int y, Color fgColor, Color bgColor, boolean centered) {
        if (centered) {
            int width = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x -= width / 2;
        }
        g2.setColor(bgColor);
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                g2.drawString(text, x + dx, y + dy);
            }
        }
        g2.setColor(fgColor);
        g2.drawString(text, x, y);
    }

    protected void delay(double seconds) {
        try {
            Thread.sleep((int) (seconds * 1000));
        } catch (InterruptedException ex) {
            // . . .
        }
    }
}
