package com.giannistsakiris.regenwormen.gui2;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

public class OutlinedLabel extends JLabel {
    
    private Color outlineColor = Color.BLACK;
    
    public OutlinedLabel( String text ) {
        super(text);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        System.out.println("PAINT COMPONENT");
        Color color = getForeground();
        setForeground(getOutlineColor());
        g.translate(-1,-1);
        super.paintComponent(g);
//        g.translate(1,0);
//        super.paintComponent(g);
//        g.translate(1,0);
//        super.paintComponent(g);
//        g.translate(0,1);
//        super.paintComponent(g);
//        g.translate(0,1);
//        super.paintComponent(g);
//        g.translate(-1,0);
//        super.paintComponent(g);
//        g.translate(-1,0);
//        super.paintComponent(g);
//        g.translate(0,-1);
//        super.paintComponent(g);
//        g.translate(1,0);
//        setForeground(color);
//        super.paintComponent(g);
//        System.exit(0);
    }
//    @Override
//    public void paintComponents(Graphics g) {
//        System.out.println("PAINT COMPONENTS");
//        Color color = getForeground();
//        setForeground(getOutlineColor());
//        g.translate(-1,-1);
//        super.paintComponents(g);
//        g.translate(1,0);
//        super.paintComponents(g);
//        g.translate(1,0);
//        super.paintComponents(g);
//        g.translate(0,1);
//        super.paintComponents(g);
//        g.translate(0,1);
//        super.paintComponents(g);
//        g.translate(-1,0);
//        super.paintComponents(g);
//        g.translate(-1,0);
//        super.paintComponents(g);
//        g.translate(0,-1);
//        super.paintComponents(g);
//        g.translate(1,0);
//        setForeground(color);
//        super.paintComponents(g);
////        System.exit(0);
//    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
}
