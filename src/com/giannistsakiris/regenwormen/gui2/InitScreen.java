package com.giannistsakiris.regenwormen.gui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InitScreen extends JPanel {

    private int playerCount = 2;
    
//    private Font smallFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.5));
//    private Font bigFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 1.25));
//    
//    JTextField[] playerTextFields;

    public InitScreen() {
        
//        setLayout(new FlowLayout(FlowLayout.RIGHT));
//        setPreferredSize(new Dimension(500,300));
        
//        JPanel panel = (JPanel) ProgramFrame.instance().getContentPane();
        
//        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
//        panel.setLayout(new FlowLayout());
//        setPreferredSize(new Dimension(1024,400));
//        panel.setLayout(null);
        
        Font comic50Font = new Font("Comic Sans MS", 0, 50);
        Font comic25Font = new Font("Comic Sans MS", 0, 25);
        
//        setBackground(new Color(0x006600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new OutlinedLabel("Regenwormen");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(comic50Font);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100,100,0,0));
        add(titleLabel);
        
        JPanel playerCountPanel = new JPanel();
        playerCountPanel.setLayout(new BoxLayout(playerCountPanel,BoxLayout.X_AXIS));
        add(playerCountPanel);
        
//        JLabel playerCountLabel = new OutlinedLabel("Number of players:");
//        playerCountLabel.setForeground(Color.YELLOW);
//        playerCountLabel.setFont(comic25Font);
//        playerCountLabel.setBorder(BorderFactory.createEmptyBorder(100,100,0,0));
//        playerCountPanel.add(playerCountLabel);
        
//        JTextField playerCountTextField = new JTextField();
//        playerCountTextField.setForeground(Color.BLACK);
//        playerCountTextField.setBackground(Color.WHITE);
//        playerCountTextField.setFont(comic25Font);
//        playerCountPanel.add(playerCountTextField);
        
//        panel.add(Box.createRigidArea(new Dimension(0,5)));
        
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
//        for (int i = 0; i < 3; i++) {
//            JButton button = new JButton("Button "+(i+1));
//            buttonPanel.add(button);
//        }
//        panel.add(buttonPanel);
//        
//        JTextField textField = new JTextField();
////        textField.setSize(200,200);
//        panel.add(textField);
//        
//        add(panel);
//        
//        JButton plusButton = new JButton("+");
//        plusButton.setFont(smallFont);
//        plusButton.setBounds(500, 3 * TILE_WIDTH -15, 75, 50);
//        plusButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (playerCount < 7) {
//                    playerTextFields[playerCount].setVisible(true);
//                    playerCount++;
//                    repaint();
//                }
//            }
//        }) ;
//        add(plusButton);
//        
//        JButton minusButton = new JButton("-");
//        minusButton.setFont(smallFont);
//        minusButton.setBounds(600, 3 * TILE_WIDTH -15, 75, 50);
//        minusButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (playerCount > 2) {
//                    playerCount--;
//                    playerTextFields[playerCount].setVisible(false);
//                    repaint();
//                }
//            }
//        }) ;
//        add(minusButton);
//        
//        playerTextFields = new JTextField[7];
//        for (int i = 0; i < playerTextFields.length; i++) {
//            JTextField textField = playerTextFields[i] = new JTextField();
//            textField.setBackground(new Color(0x006060));
//            textField.setForeground(new Color(0xffffff));
//            textField.setFont(smallFont);
//            textField.setBounds(TILE_WIDTH, (5+i) * 70 - 40, 400, 60);
//            textField.setText("Player "+(i+1));
//            textField.setVisible(i < playerCount);
//            add(textField);
//        }
//        
//        JButton startButton = new JButton("Start");
//        startButton.setFont(smallFont);
//        startButton.setBounds(getPreferredSize().width-200-TILE_WIDTH, getPreferredSize().height-100-TILE_WIDTH, 200, 100);
//        startButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String[] playerNames = new String[playerCount];
//                for (int i = 0; i < playerCount; i++) {
//                    playerNames[i] = playerTextFields[i].getText().trim();
//                }
//                Frame.instance().startGame(new Game(playerCount,playerNames));
//            }
//        }) ;
//        add(startButton);
    }

    //    @Override
    //    protected void paintScreen() {
    //
    ////        g2.setFont(bigFont);
    ////        drawBorderedString("RegenWormen", TILE_WIDTH, 2 * TILE_WIDTH, Color.YELLOW, Color.RED);
    ////
    ////        g2.setFont(smallFont);
    ////        drawBorderedString("Number of Players: " + playerCount, TILE_WIDTH, 4 * TILE_WIDTH - 50, Color.CYAN, Color.BLUE);
    //
    //
    //    }
    int count = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        System.out.println("PAINT : "+(++count));
    }
    
    
}
