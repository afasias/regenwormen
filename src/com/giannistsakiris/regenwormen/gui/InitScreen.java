package com.giannistsakiris.regenwormen.gui;

import com.giannistsakiris.regenwormen.Game;
import static com.giannistsakiris.regenwormen.gui.GameScreen.TILE_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;

public class InitScreen extends Screen {

    private int playerCount = 2;
    
    private Font smallFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.5));
    private Font bigFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 1.25));
    
    JTextField[] playerTextFields;

    public InitScreen() {
        setLayout(null);
        setPreferredSize(new Dimension(1024,800));
        
        JButton plusButton = new JButton("+");
        plusButton.setFont(smallFont);
        plusButton.setBounds(500, 3 * TILE_WIDTH -15, 75, 50);
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerCount < 7) {
                    playerTextFields[playerCount].setVisible(true);
                    playerCount++;
                    repaint();
                }
            }
        }) ;
        add(plusButton);
        
        JButton minusButton = new JButton("-");
        minusButton.setFont(smallFont);
        minusButton.setBounds(600, 3 * TILE_WIDTH -15, 75, 50);
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerCount > 2) {
                    playerCount--;
                    playerTextFields[playerCount].setVisible(false);
                    repaint();
                }
            }
        }) ;
        add(minusButton);
        
        playerTextFields = new JTextField[7];
        for (int i = 0; i < playerTextFields.length; i++) {
            JTextField textField = playerTextFields[i] = new JTextField();
            textField.setBackground(new Color(0x006060));
            textField.setForeground(new Color(0xffffff));
            textField.setFont(smallFont);
            textField.setBounds(TILE_WIDTH, (5+i) * 70 - 40, 400, 60);
            textField.setText("Player "+(i+1));
            textField.setVisible(i < playerCount);
            add(textField);
        }
        
        JButton startButton = new JButton("Start");
        startButton.setFont(smallFont);
        startButton.setBounds(getPreferredSize().width-200-TILE_WIDTH, getPreferredSize().height-100-TILE_WIDTH, 200, 100);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] playerNames = new String[playerCount];
                for (int i = 0; i < playerCount; i++) {
                    playerNames[i] = playerTextFields[i].getText().trim();
                }
                Frame.instance().startGame(new Game(playerCount,playerNames));
            }
        }) ;
        add(startButton);
    }

    @Override
    protected void paintScreen() {

        g2.setFont(bigFont);
        drawBorderedString("RegenWormen", TILE_WIDTH, 2 * TILE_WIDTH, Color.YELLOW, Color.RED);

        g2.setFont(smallFont);
        drawBorderedString("Number of Players: " + playerCount, TILE_WIDTH, 4 * TILE_WIDTH - 50, Color.CYAN, Color.BLUE);


    }
}
