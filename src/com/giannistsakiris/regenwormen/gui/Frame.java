package com.giannistsakiris.regenwormen.gui;

import com.giannistsakiris.regenwormen.Game;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Frame extends JFrame {
    
    private static Frame instance;
    
    private Screen initScreen;
    private Screen gameScreen;
    
    public static String WINDOW_TITLE = "RegenWormen -- programmed by Giannis Tsakiris";

    public Frame() {
        setTitle(WINDOW_TITLE);
//        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280,768);
        
        add(initScreen = new InitScreen());
        pack();
//        add(gameScreen = new GameScreen(new Game(2,new String[0])));
    }

    public void startGame( final Game game ) {
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
    
    public static Frame instance() {
        if (instance == null) {
            instance = new Frame();
        }
        return instance;
    }
}