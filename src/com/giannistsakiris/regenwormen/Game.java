package com.giannistsakiris.regenwormen;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Game {

    private Player[] players;
    private int currentPlayerIndex = 0;
    private SortedSet<Tile> tiles;
    private DiceRoll diceRoll = new DiceRoll();

    public Game(int playerCount, String[] playerNames) {

        if (playerCount < 2) {
            throw new RuntimeException("At least 2 players required");
        }
        if (playerCount > 7) {
            throw new RuntimeException("At most 7 players are allowed");
        }

        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = i < playerNames.length ? playerNames[i] : ("Player #" + (i + 1));
            players[i] = new Player(name);
        }

        tiles = new TreeSet<>();
        for (Tile tile : Tile.values()) {
            tiles.add(tile);
        }
  
        //REMOVE THIS LATER:
//        Tile[] tiles1 = Tile.values();
//        for (int i = 1; i < tiles1.length; i++) {
//            Tile tile = tiles1[i];
//            tiles.remove(tile);
//            players[i%2].pushTile(tile);
//        }
    }

    public void reset() {
        for (Player player : players) {
            player.clearTiles();
        }
        tiles.clear();
        tiles.addAll(Arrays.asList(Tile.values()));
        diceRoll.reset();
        currentPlayerIndex = 0;
    }

    public void advanceRound() {
        boolean playerLoses = false;
        if (diceRoll.hasRolled() && diceRoll.possibleChoices().isEmpty()) {
            playerLoses = true;
        } else {
            Player loser = playerWhomLosesTile();
            if (loser != null) {
                Tile tile = loser.popTile();
                currentPlayer().pushTile(tile);
            } else {
                Tile tile = tileAcquiredFromDeck();
                if (tile != null) {
                    tiles.remove(tile);
                    currentPlayer().pushTile(tile);
                } else {
                    playerLoses = true;
                }
            }
        }

        if (playerLoses) {
            if (currentPlayer().hasTiles()) {
                Tile tile = currentPlayer().popTile();
                tiles.add(tile);
            }
            tiles.remove(tiles.last());
        }
        
        currentPlayerIndex++;
        if (!(currentPlayerIndex < players.length)) {
            currentPlayerIndex = 0;
        }
        
        diceRoll.reset();
    }

    public Player currentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player[] getPlayers() {
        return players;
    }

    public SortedSet<Tile> getTiles() {
        return tiles;
    }

    public DiceRoll getDiceRoll() {
        return diceRoll;
    }

    public boolean playerAcquiresTile() {
        return playerAcquiresTileFromDeck() || playerAcquiresTileFromOpponent();
    }

    public Tile tileAcquiredFromDeck() {
        if (!diceRoll.isValid()) {
            return null;
        }
        int score = diceRoll.score();
        Tile acquiredTile = null;
        for (Tile tile : tiles) {
            if (tile.value() <= score && (acquiredTile == null || acquiredTile.value() < tile.value())) {
                acquiredTile = tile;
            }
        }
        return acquiredTile;
    }

    public boolean playerAcquiresTileFromDeck() {
        return tileAcquiredFromDeck() != null;
    }

    public Player playerWhomLosesTile() {
        if (!diceRoll.isValid()) {
            return null;
        }
        int score = diceRoll.score();
        for (Player player : players) {
            if (player != currentPlayer() && player.hasTiles() && player.lastTile().value() == score) {
                return player;
            }
        }
        return null;
    }

    public boolean playerAcquiresTileFromOpponent() {
        return playerWhomLosesTile() != null;
    }

    public boolean isOver() {
        return tiles.isEmpty();
    }

    public int currentPlayerIndex() {
        return currentPlayerIndex;
    }
}
