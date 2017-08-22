package com.giannistsakiris.regenwormen.gui;

import com.giannistsakiris.regenwormen.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class GameScreen extends Screen implements MouseListener, MouseMotionListener, KeyListener {

    /*
     * TODO:
     * 
     * BUGS:
     * Otan kapios pektis xasei tote: otan ksekinaei to deytero animation
     * to toublaki pou xanei o pektis fenete na einai akoma sti 8esi tou
     * kai oxi sto deck.
     */
    final static int DIE_SIZE = 50;
    final static int DIE_SPACING = 10;
    final static int TILE_WIDTH = 75;
    final static int TILE_HEIGHT = 125;
    final static int TILE_SPACING = 15;
    final static int TILE_XOFFSET = 25;
    final static int TILE_YOFFSET = 25;
    final static int PLAYER_SPACING = 175;
    final static int PLAYER_XOFFSET = 25;
    final static int PLAYER_YOFFSET = 25;
    private Game game;
    private Font tinyFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.2));
    private Font dieFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.4));
    private Font tileFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.6));
    private String message;
    private boolean diceRollPlayAnimation = true;
    private DieFace diceRollPlayAnimationDieFace;
    private double diceRollPlayAnimationPercentage;
    private ImageIcon wormIcon;
    private boolean acquireTileAnimation = false;
    private Tile acquireTileAnimationTile;
    private double acquireTileAnimationPercentage;
    
    private boolean playerLosesTileAnimation = false;
    private Tile playerLosesTileAnimationTile;
    private double playerLosesTileAnimationPercentage;
    private boolean removeHighestTileAnimation = false;
    private Tile removeHighestTileAnimationTile;
    private double removeHighestTileAnimationPercentage;
    private boolean stealTileAnimation = false;
    private Player stealTileAnimationPlayer;
    private double stealTileAnimationPercentage;
    private Rectangle remainingDiceRectangle;
    private Rectangle playedDiceRectangle;
    private boolean gameOverAnimation = false;
    private double gameOverAnimationPercentage;
    private boolean rollingAnimation = false;

    public GameScreen(final Game game) {
        setFocusable(true);

        wormIcon = new ImageIcon(this.getClass().getResource("/images/skouliki.png"));

        this.game = game;
        setLayout(null);
        setPreferredSize(new Dimension((TILE_WIDTH + TILE_SPACING) * Tile.values().length - TILE_SPACING + 2 * TILE_XOFFSET, 800));
        tileFont = new Font("Comic Sans MS", 0, (int) (TILE_WIDTH * 0.7));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(tinyFont);
        exitButton.setBounds(getPreferredSize().width - 80, getPreferredSize().height - 40, 75, 35);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isOver() || JOptionPane.showConfirmDialog(null, "Exit game, are you sure?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    Frame.instance().exitGame();
                }
            }
        });
        add(exitButton);

        final JButton resetButton = new JButton("Reset");
        resetButton.setFont(tinyFont);
        resetButton.setBounds(getPreferredSize().width - 80, getPreferredSize().height - 80, 75, 35);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isOver() || JOptionPane.showConfirmDialog(null, "Reset game, are you sure?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    game.reset();
                    message = message = game.currentPlayer().getName() + " rolls the dice.";
                    repaint();
                }
            }
        });
        resetButton.setFocusable(false);
        add(resetButton);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
        message = game.currentPlayer().getName() + " rolls the dice.";
    }

    private void advanceRound() {
        game.advanceRound();
        if (game.isOver()) {
            gameOverAnimation();
        } else {
            message = game.currentPlayer().getName() + " rolls the dice!";
        }
        repaint();
    }

    @Override
    protected void paintScreen() {

        float dash[] = {5.0f};
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

        for (int i = 0; i < Tile.values().length; i++) {
            g2.drawRect(i * (TILE_WIDTH + TILE_SPACING) + TILE_XOFFSET, TILE_YOFFSET, TILE_WIDTH, TILE_HEIGHT);
        }

        for (int i = 0; i < game.getPlayers().length; i++) {
            Player player = game.getPlayers()[i];
            drawPlayerData(player, i * PLAYER_SPACING + PLAYER_XOFFSET, this.getHeight() - PLAYER_YOFFSET);
        }

        if (stealTileAnimation) {
            int index;
            for (index = 0; index < game.getPlayers().length; index++) {
                if (game.getPlayers()[index] == stealTileAnimationPlayer) {
                    break;
                }
            }
            int x = index * PLAYER_SPACING + PLAYER_XOFFSET + (stealTileAnimationPlayer.tileCount() - 1) * 5;
            int y = this.getHeight() - PLAYER_YOFFSET - (stealTileAnimationPlayer.tileCount() - 1) * 5 - TILE_HEIGHT - TILE_HEIGHT / 2;
            int dx = game.currentPlayerIndex() * PLAYER_SPACING + PLAYER_XOFFSET + game.currentPlayer().tileCount() * 5;
            int dy = this.getHeight() - PLAYER_YOFFSET - game.currentPlayer().tileCount() * 5 - TILE_HEIGHT - TILE_HEIGHT / 2;
            x += (dx - x) * stealTileAnimationPercentage;
            y += (dy - y) * stealTileAnimationPercentage;
            drawTile(stealTileAnimationPlayer.lastTile(), x, y);
        }

        for (Tile tile : game.getTiles()) {
            int x = tile.ordinal() * (TILE_WIDTH + TILE_SPACING) + TILE_XOFFSET;
            int y = TILE_YOFFSET;
            if (acquireTileAnimation && acquireTileAnimationTile == tile) {
                int index = game.currentPlayerIndex();
                int count = game.currentPlayer().tileCount();
                int px = index * PLAYER_SPACING + PLAYER_XOFFSET;
                int py = this.getHeight() - PLAYER_YOFFSET;
                int dx = px + 5 * count;
                int dy = py - TILE_HEIGHT - TILE_HEIGHT / 2 - 5 * count;
                x += (dx - x) * acquireTileAnimationPercentage;
                y += (dy - y) * acquireTileAnimationPercentage;
            }

            if (removeHighestTileAnimation && removeHighestTileAnimationTile == tile) {
                int dx = getWidth() / 2;
                int dy = getHeight();
                x += (dx - x) * removeHighestTileAnimationPercentage;
                y += (dy - y) * removeHighestTileAnimationPercentage;
            }

            drawTile(tile, x, y);
        }

        DiceRoll diceRoll = game.getDiceRoll();
        for (int i = 0; i < diceRoll.getPlayedDice().size(); i++) {
            Die die = diceRoll.getPlayedDice().get(i);
            drawDieFace(die.getFace(), i * (DIE_SIZE + DIE_SPACING) + PLAYER_XOFFSET, 2 * TILE_HEIGHT + TILE_YOFFSET);
        }


        if (game.playerAcquiresTile()) {
            int x = PLAYER_XOFFSET - DIE_SPACING;
            int y = 2 * TILE_HEIGHT + TILE_YOFFSET - DIE_SPACING;
            int w = (diceRoll.getPlayedDice().size() + 1) * (DIE_SIZE + DIE_SPACING) + DIE_SPACING;
            int h = DIE_SIZE + 2 * DIE_SPACING;
            playedDiceRectangle = new Rectangle(x, y, w, h);
//            g2.setColor(Color.BLACK);
//            g2.drawRect(x, y, w, h);
        } else {
            playedDiceRectangle = null;
        }

        if (!diceRoll.getPlayedDice().isEmpty()) {
            g2.setFont(tileFont);
            drawBorderedString(Integer.toString(diceRoll.score()), diceRoll.getPlayedDice().size() * (DIE_SIZE + DIE_SPACING) + PLAYER_XOFFSET, 2 * TILE_HEIGHT + TILE_YOFFSET + (int) (DIE_SIZE * 0.9), game.playerAcquiresTile() ? Color.GREEN : Color.RED, Color.BLACK);
        }

        drawRemainingDice();

        g2.setFont(tileFont);
        drawBorderedString(message, getWidth() / 2, TILE_YOFFSET + (int) (1.75 * TILE_HEIGHT), Color.YELLOW, Color.BLACK, true);

    }

    private void drawRemainingDice() {
        Die[] remainingDice = game.getDiceRoll().getRemainingDice().toArray(new Die[0]);

//        float dash[] = {3f};
//        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
//        g2.setColor(Color.WHITE);
        if (game.getDiceRoll().isRollable() && !game.getDiceRoll().hasRolled()) {
            int x = TILE_XOFFSET + 15 * (TILE_WIDTH + TILE_SPACING) - (remainingDice.length - 1) * (DIE_SIZE + DIE_SPACING) - DIE_SPACING;
            int y = 2 * TILE_HEIGHT + TILE_YOFFSET - DIE_SPACING;
            int w = remainingDice.length * (DIE_SIZE + DIE_SPACING) + DIE_SPACING;
            int h = DIE_SIZE + DIE_SPACING * 2;
            remainingDiceRectangle = new Rectangle(x, y, w, h);
//            g2.drawRect(x, y, w, h);
        } else {
            remainingDiceRectangle = null;
        }

        int playedDiceCount = game.getDiceRoll().getPlayedDice().size();
        for (int i = remainingDice.length - 1; i >= 0; i--) {
            Die die = remainingDice[i];
            int x = TILE_XOFFSET + 15 * (TILE_WIDTH + TILE_SPACING) - i * (DIE_SIZE + DIE_SPACING);
            int y = 2 * TILE_HEIGHT + TILE_YOFFSET;
            if (diceRollPlayAnimation && die.getFace() == diceRollPlayAnimationDieFace) {
                int dx = playedDiceCount * (DIE_SIZE + DIE_SPACING) + PLAYER_XOFFSET;
                int dy = 2 * TILE_HEIGHT + TILE_YOFFSET;
                x += (dx - x) * diceRollPlayAnimationPercentage;
                y += (dy - y) * diceRollPlayAnimationPercentage;
                playedDiceCount++;
            }
            drawDieFace(die.getFace(), x, y);
            if (game.getDiceRoll().hasRolled() && !rollingAnimation && game.getDiceRoll().getPlayedFaces().contains(die.getFace())) {
                g2.setColor(new Color(0x80ff0000, true));
                g2.setStroke(new BasicStroke(5f));
                g2.drawLine(x - 2, y - 2, x + DIE_SIZE + 2, y + DIE_SIZE + 2);
                g2.drawLine(x + DIE_SIZE + 2, y - 2, x - 2, y + DIE_SIZE + 2);
            }
        }
    }

    private void drawDieFace(DieFace dieFace, int x, int y) {
        g2.setStroke(new BasicStroke(3f));
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, DIE_SIZE, DIE_SIZE);
        g2.setColor(Color.BLACK);
        switch (dieFace) {
            case FACE_1:
                g2.drawRect(x + DIE_SIZE / 4 * 2 - 1, y + DIE_SIZE / 4 * 2 - 1, 3, 3);
                break;
            case FACE_2:
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                break;
            case FACE_3:
                g2.drawRect(x + DIE_SIZE / 4 * 2 - 1, y + DIE_SIZE / 4 * 2 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                break;
            case FACE_4:
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                break;
            case FACE_5:
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 1 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 1 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 3 - 1, y + DIE_SIZE / 4 * 3 - 1, 3, 3);
                g2.drawRect(x + DIE_SIZE / 4 * 2 - 1, y + DIE_SIZE / 4 * 2 - 1, 3, 3);
                break;
            case FACE_WORM:
                g2.drawImage(wormIcon.getImage(), x + 5, y + 5, DIE_SIZE - 10, DIE_SIZE - 10, null);
                break;
        }
        g2.drawRect(x, y, DIE_SIZE, DIE_SIZE);
//        g2.drawString(dieFace.toString(), x + (int) (DIE_SIZE * 0.25), y + DIE_SIZE - (int) (DIE_SIZE * 0.2));
    }

    private void drawTile(Tile tile, int x, int y) {
        g2.setStroke(new BasicStroke(2f));
        g2.setFont(tileFont);
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
        g2.drawLine(x + (int) (TILE_WIDTH * 0.07), y + TILE_HEIGHT / 2, x + TILE_WIDTH - (int) (TILE_WIDTH * 0.07), y + TILE_HEIGHT / 2);
        g2.drawString(Integer.toString(tile.value()), x + (int) (TILE_WIDTH * 0.07), y + TILE_HEIGHT / 2 - (int) (TILE_HEIGHT * 0.07));
        switch (tile.wormCount()) {
            case 1:
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 17, y + TILE_HEIGHT / 4 * 3 - 17, DIE_SIZE - 15, DIE_SIZE - 15, null);
                break;
            case 2:
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 30, y + TILE_HEIGHT / 4 * 3 - 27, DIE_SIZE - 20, DIE_SIZE - 20, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2, y + TILE_HEIGHT / 4 * 3 - 7, DIE_SIZE - 20, DIE_SIZE - 20, null);
                break;
            case 3:
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 30, y + TILE_HEIGHT / 4 * 3 - 10, DIE_SIZE - 23, DIE_SIZE - 23, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 + 5, y + TILE_HEIGHT / 4 * 3 - 2, DIE_SIZE - 23, DIE_SIZE - 23, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 14, y + TILE_HEIGHT / 4 * 3 - 29, DIE_SIZE - 23, DIE_SIZE - 23, null);
                break;
            case 4:
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 32, y + TILE_HEIGHT / 4 * 3 - 10, DIE_SIZE - 23, DIE_SIZE - 23, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 8, y + TILE_HEIGHT / 4 * 3, DIE_SIZE - 23, DIE_SIZE - 23, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 - 16, y + TILE_HEIGHT / 4 * 3 - 29, DIE_SIZE - 23, DIE_SIZE - 23, null);
                g2.drawImage(wormIcon.getImage(), x + TILE_WIDTH / 2 + 9, y + TILE_HEIGHT / 4 * 3 - 15, DIE_SIZE - 23, DIE_SIZE - 23, null);
                break;
        }
    }

    private void drawPlayerData(Player player, int x, int y) {
        g2.setFont(dieFont);
        drawBorderedString(player.getName() + (game.currentPlayer() == player ? "*" : ""), x, y, Color.WHITE, Color.BLACK);

        if (!player.hasTiles()) {
            float dash[] = {5.0f};
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y - TILE_HEIGHT - TILE_HEIGHT / 2, TILE_WIDTH, TILE_HEIGHT);
        }

        Iterator<Tile> tileIterator = player.tileIterator();
        int count = 0;
        while (tileIterator.hasNext()) {
            Tile tile = tileIterator.next();
            if (!(stealTileAnimation && stealTileAnimationPlayer == player && player.lastTile() == tile)) {
                int px = x;
                int py = y - TILE_HEIGHT - TILE_HEIGHT / 2;
                if (game.isOver()) {
                    px += (count % 2) * (int) (TILE_WIDTH * 0.85) + (count / 2) * 5;
                    py -= (count / 2) * (int) (TILE_HEIGHT * 0.9) + (count % 2) * 5;
                } else {
                    px += 5 * count;
                    py -= 5 * count;
                }
                if (playerLosesTileAnimation && game.currentPlayer() == player && player.lastTile() == tile) {
                    int tx = tile.ordinal() * (TILE_WIDTH + TILE_SPACING) + TILE_XOFFSET;
                    int ty = TILE_YOFFSET;
                    px += (tx - px) * playerLosesTileAnimationPercentage;
                    py += (ty - py) * playerLosesTileAnimationPercentage;
                }
                if (gameOverAnimation) {
                    int gx = x + (count % 2) * (int) (TILE_WIDTH * 0.85) + (count / 2) * 5;
                    int gy = y - TILE_HEIGHT - TILE_HEIGHT / 2 - ((count) / 2) * (int) (TILE_HEIGHT * 0.9) - (count % 2) * 5;
                    int nx = x + 5 * count;
                    int ny = y - TILE_HEIGHT - TILE_HEIGHT / 2 - 5 * count;
                    px = nx + (int) ((gx - nx) * gameOverAnimationPercentage);
                    py = ny + (int) ((gy - ny) * gameOverAnimationPercentage);
                }
                drawTile(tile, px, py);
                count++;
            }
        }

        if (gameOverAnimation) {
            int wormCount = (int) (player.wormCount() * gameOverAnimationPercentage);
            int ax = x + TILE_WIDTH / 2 + (count / 2) * 5;
            int ay = y - TILE_HEIGHT * 3 / 4;
            int gy = y - TILE_HEIGHT * 3 / 4 - ((count + 1) / 2) * (int) (TILE_HEIGHT * 0.9);
            ay += (gy - ay) * gameOverAnimationPercentage;
            drawBorderedString(wormCount + "", ax, ay, Color.GREEN, Color.BLACK, true);
        } else if (game.isOver()) {
            int gx = x + TILE_WIDTH / 2 + (count / 2) * 5;
            int gy = y - TILE_HEIGHT * 3 / 4 - ((count + 1) / 2) * (int) (TILE_HEIGHT * 0.9);
            drawBorderedString(player.wormCount() + "", gx, gy, Color.GREEN, Color.BLACK, true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (playedDiceRectangle != null && playedDiceRectangle.contains(e.getPoint())) {
            playerChoosesToStop();
        } else if (remainingDiceRectangle != null && remainingDiceRectangle.contains(e.getPoint())) {
            playerChoosesToRoll();
        } else if (game.getDiceRoll().hasRolled()) {
            final DiceRoll diceRoll = game.getDiceRoll();
            Die[] remainingDice = diceRoll.getRemainingDice().toArray(new Die[0]);
            for (int i = 0; i < remainingDice.length; i++) {
                final Die die = remainingDice[i];
                if (!diceRoll.getPlayedFaces().contains(die.getFace())) {
                    int x = TILE_XOFFSET + 15 * (TILE_WIDTH + TILE_SPACING) - i * (DIE_SIZE + DIE_SPACING);
                    int y = 2 * TILE_HEIGHT + TILE_YOFFSET;
                    if (x <= e.getX() && e.getX() <= (x + DIE_SIZE) && y <= e.getY() && e.getY() <= (y + DIE_SIZE)) {
                        playerChoseToPlayFace(die.getFace());
                    }
                }
            }
        }
    }

    private void stealTileAnimation() {
        Player loser = game.playerWhomLosesTile();
        message = "You acquire the tile from the top of " + loser.getName() + "'s stack (" + loser.lastTile().value() + ")...";
        repaint();
        stealTileAnimation = true;
        stealTileAnimationPlayer = loser;
        for (int step = 0; step < 25; step++) {
            stealTileAnimationPercentage = step / 25.0;
            repaint();
            delay(0.03);
        }
        stealTileAnimation = false;
    }

    private void playerChoosesToRoll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Player player = game.currentPlayer();
                message = "Rolling...";
                rollingAnimation = true;
                for (int count = 0; count < 25; count++) {
                    game.getDiceRoll().roll();
                    repaint();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                    }
                }
                rollingAnimation = false;
                if (game.getDiceRoll().possibleChoices().isEmpty()) {
                    message = "Busted!";
                    repaint();
                    delay(2);
                    if (game.getDiceRoll().possibleChoices().isEmpty()) {
                        removeHighestTileAnimation();
                    }
                    advanceRound();
                } else {
                    message = player.getName() + ", make your move.";
                    repaint();
                }
            }
        }).start();
    }

    private void removeHighestTileAnimation() {
        if (game.currentPlayer().hasTiles()) {
            playerLosesTileAnimation = true;
            message = game.currentPlayer().getName() + " loses top tile...";
            for (int step = 0; step < 25; step++) {
                playerLosesTileAnimationPercentage = step / 25.0;
                repaint();
                delay(0.03);
            }
            playerLosesTileAnimation = false;
        }
        Tile tile = game.getTiles().last();
        message = "Tile (" + tile.value() + ") is removed from the deck...";
        removeHighestTileAnimation = true;
        removeHighestTileAnimationTile = tile;
        for (int step = 0; step < 25; step++) {
            removeHighestTileAnimationPercentage = step / 25.0;
            repaint();
            delay(0.03);
        }
        removeHighestTileAnimation = false;
    }

    private void acquireTileAnimation() {
        Tile tile = game.tileAcquiredFromDeck();
        message = "You acquire tile " + tile.value() + " from the deck...";
        repaint();
        acquireTileAnimation = true;
        acquireTileAnimationTile = tile;
        for (int step = 0; step < 100; step++) {
            acquireTileAnimationPercentage = step / 100.0;
            repaint();
            delay(0.01);
        }
        acquireTileAnimation = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (playedDiceRectangle != null && playedDiceRectangle.contains(e.getPoint())) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else if (remainingDiceRectangle != null && remainingDiceRectangle.contains(e.getPoint())) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void gameOverAnimation() {
        message = "G A M E   O V E R";
        gameOverAnimation = true;
//        while (true)
        for (int step = 0; step < 25; step++) {
            gameOverAnimationPercentage = step / 25.0;
            repaint();
            delay(0.05);
        }
        gameOverAnimation = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = Character.toLowerCase(e.getKeyChar());
        if (remainingDiceRectangle != null && ch == 'r') {
            playerChoosesToRoll();
        } else if (playedDiceRectangle != null && ch == 's') {
            playerChoosesToStop();
        } else if (game.getDiceRoll().hasRolled() && ch >= '1' && ch <= '6') {
            playerChoseToPlayFace(DieFace.values()[ch - '1']);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void playerChoosesToStop() {
        new Thread() {
            @Override
            public void run() {
                if (game.playerAcquiresTileFromOpponent()) {
                    stealTileAnimation();
                } else if (game.playerAcquiresTileFromDeck()) {
                    acquireTileAnimation();
                }
                advanceRound();
            }
        }.start();
    }

    private void playerChoseToPlayFace(final DieFace face) {
        new Thread() {
            @Override
            public void run() {
                diceRollPlayAnimation = true;
                diceRollPlayAnimationDieFace = face;
                for (int step = 0; step < 50; step++) {
                    diceRollPlayAnimationPercentage = step / 50.0;
                    repaint();
                    delay(0.015);
                }
                diceRollPlayAnimation = false;
                game.getDiceRoll().play(face);

                if (!game.getDiceRoll().isRollable()) {
                    message = game.currentPlayer().getName() + ", you cannot continue rolling...";
                    repaint();
                    delay(2);
                    if (!game.playerAcquiresTile()) {
                        if (game.currentPlayer().hasTiles()) {
                            message = game.currentPlayer().getName() + ", you will lose the tile at the top of your stack (" + game.currentPlayer().lastTile().value() + ")...";
                            repaint();
                            delay(2);
                        }
                        removeHighestTileAnimation();
                    } else if (game.playerAcquiresTileFromOpponent()) {
                        stealTileAnimation();
                    } else if (game.playerAcquiresTileFromDeck()) {
                        acquireTileAnimation();
                    }
                    advanceRound();
                } else {
                    if (game.playerAcquiresTile()) {
                        message = game.currentPlayer().getName() + ", you can roll the remaining dice or stop.";
                    } else {
                        message = game.currentPlayer().getName() + ", please roll the remaining dice.";
                    }
                    repaint();
                }
            }
        }.start();
    }
}
