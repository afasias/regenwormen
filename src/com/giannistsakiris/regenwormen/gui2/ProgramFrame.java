package com.giannistsakiris.regenwormen.gui2;

import com.giannistsakiris.regenwormen.*;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class ProgramFrame extends JFrame {
    
    public static String TITLE = "RegenWormen -- programmed by Giannis Tsakiris";
    
    private static ProgramFrame instance;
    private JComponent initScreen;
    private JComponent gameScreen;

    public ProgramFrame() {
        setTitle(TITLE);
//        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(initScreen = new InitScreen());
        pack();
    }

    void startGame(Game game) {
        remove(initScreen);
        add(gameScreen = new GameScreen(game));
        gameScreen.requestFocusInWindow();
        pack();
    }

    void exitGame() {
        remove(gameScreen);
        add(initScreen);
        pack();
    }

    public static ProgramFrame instance() {
        if (instance == null) {
            instance = new ProgramFrame();
        }
        return instance;
    }
    
    public static void main( String[] args ) {
        ProgramFrame.instance().setVisible(true);
    }

}