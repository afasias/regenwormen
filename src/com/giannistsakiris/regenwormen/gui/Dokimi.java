package com.giannistsakiris.regenwormen.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Dokimi extends JFrame {
    
    class RedComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(0,0,getWidth(),getHeight());
        }
    }
    class BlueComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(0,0,getWidth(),getHeight());
        }
    }
    
    
    public Dokimi() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,400);
        BlueComponent blueComponent = new BlueComponent();
        RedComponent redComponent = new RedComponent();

        add(blueComponent);
        add(redComponent);
    }
    
    public static void main( String[] args ) {
        new Dokimi().setVisible(true);
    }
    
}
