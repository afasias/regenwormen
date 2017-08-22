package com.giannistsakiris.regenwormen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class TextUI {
    
    class RestartException extends Exception { }
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Game game = null;
    
    private String readLine( String prompt ) throws IOException, RestartException {
        while (true) {
            System.out.print(prompt+" ");
            String line = in.readLine().trim();
            if (line.equals("quit")) {
                System.out.println("Bye-bye!");
                System.exit(0);
            } else if (game != null && line.equals("restart")) {
                throw new RestartException();
            } else if (game != null && line.equals("?")) {
                printGameState();
                printDiceRollState();
            } else {
                return line;
            }
        }
    }
    
    private void printGameState() {
        System.out.println("-------------------------");
        for (Player player : game.getPlayers()) {
            System.out.print(player.getName()+": ");
            if (player.hasTiles()) {
                for (int i = 0; i < player.tileCount(); i++) {
                    System.out.print('[');
                }
                System.out.print(player.lastTile().value()+"]");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
        System.out.print("Available tiles: ");
        for (Tile tile : game.getTiles()) {
            System.out.print("["+tile.value()+"] ");
        }
        System.out.println();
        System.out.println("-------------------------");
        System.out.println(game.currentPlayer().getName()+" is up!");
    }
    
    private void printDiceRollState() {
        System.out.print("Dice collected so far:");
        for (Die die : game.getDiceRoll().getPlayedDice()) {
            System.out.print(" [" + die.getFace() + "]");
        }
        System.out.println(" --> " + game.getDiceRoll().score() + (game.getDiceRoll().isValid() ? "*" : ""));
        if (game.getDiceRoll().hasRolled()) {
            System.out.print("Dice rolled:");
        } else {
            System.out.print("Dice yet to be rolled:");
        }
        for (Die die : game.getDiceRoll().getRemainingDice()) {
            System.out.print(" [" + die.getFace() + "]");
        }
        System.out.println();
    }
    
    private void playGame() throws Exception {
        
        while (! game.getTiles().isEmpty()) {
            
            printGameState();
            
            boolean playerLoses = false;
            
            dice_roll_loop:
            while (game.getDiceRoll().isRollable()) {
                
                if (game.getDiceRoll().getPlayedDice().size() > 0) {
                    
                    printDiceRollState();

                    while (true) {
                        String input = readLine("Do you wish to roll the remaining dice? (y/n)");
                        if (input.equalsIgnoreCase("n")) {
                            break dice_roll_loop;
                        } else if (input.equalsIgnoreCase("y")) {
                            break;
                        }
                    }
                }
                
                System.out.print("Rolling... ");

                game.getDiceRoll().roll();
                
                for (Die die : game.getDiceRoll().getRemainingDice()) {
                    System.out.print("["+die.getFace()+"] ");
                }
                System.out.println();
                
                Collection<DieFace> possibleChoices = game.getDiceRoll().possibleChoices();
                
                if (possibleChoices.isEmpty()) {
                    System.out.println("BUSTED!");
                    playerLoses = true;
                    break;
                }
                
                DieFace chosenFace;
                do {
                    String prompt = "Choose one of the following:";
                    for (DieFace choice : possibleChoices) {
                        prompt += " ["+choice+"]";
                    }
                    prompt += " -->";

                    chosenFace = DieFace.fromString(readLine(prompt));
                    if (chosenFace == null || ! possibleChoices.contains(chosenFace)) {
                        System.out.println("Invalid input. Try again!");
                    }
                } while (chosenFace == null || ! possibleChoices.contains(chosenFace));
                
                game.getDiceRoll().play(chosenFace);
            }
            
            if (! playerLoses) {
                if (! game.getDiceRoll().isValid()) {
                    System.out.println("Player loses because the roll does not contain a '*' ...");
                    playerLoses = true;
                } else {
                    int score = game.getDiceRoll().score();
                    
                    Player screwedPlayer = null;
                    for (Player player : game.getPlayers()) {
                        if (player != game.currentPlayer()) {
                            if (player.hasTiles() && player.lastTile().value() == score) {
                                screwedPlayer = player;
                            }
                        }
                    }
                    
                    if (screwedPlayer != null) {
                        Tile tile = screwedPlayer.popTile();
                        System.out.println("Player steals tile ["+tile.value()+"] from "+screwedPlayer.getName());
                        game.currentPlayer().pushTile(tile);
                    } else {
                        Tile winTile = null;
                        for (Tile tile : game.getTiles()) {
                            if (tile.value() <= score) {
                                winTile = tile;
                            }
                        }
                        if (winTile != null) {
                            System.out.println("Player acquires tile: ["+winTile.value()+"]");
                            game.getTiles().remove(winTile);
                            game.currentPlayer().pushTile(winTile);
                        } else {
                            System.out.println("Player loses due to insufficient collected points...");
                            playerLoses = true;
                        }
                    }
                }
            }
            
            if (playerLoses) {
                if (game.currentPlayer().hasTiles()) {
                    Tile playerTopTile = game.currentPlayer().popTile();
                    System.out.println(game.currentPlayer().getName()+" returns back the top tile from his stack: ["+playerTopTile.value()+"]");
                    game.getTiles().add(playerTopTile);
                }
                Tile deckTopTile = game.getTiles().last();
                System.out.println("Removing the highest-valued tile from the deck: ["+deckTopTile.value()+"]");
                game.getTiles().remove(deckTopTile);
            }
       
            game.advanceRound();
        }
        
        System.out.println("------------------");
        System.out.println("Game Over!!");
        System.out.println("------------------");
        for (Player player : game.getPlayers()) {
            System.out.print(player.getName()+": "+player.wormCount()+" worm(s) --> ");
            Iterator<Tile> tileIterator = player.tileIterator();
            while (tileIterator.hasNext()) {
                Tile tile = tileIterator.next();
                System.out.print("["+tile.value()+"/"+tile.wormCount()+"} ");
            }
            System.out.println();
        }
        System.out.println("------------------");
    }
    
    public void run() throws Exception {
        
        int playerCount;
        do {
            String playerCountStr = readLine("Number of players (2-7): [default=2]");
            playerCount = playerCountStr.isEmpty() ? 2 : Integer.valueOf(playerCountStr);
        } while (playerCount < 2 || playerCount > 7);
        
        String[] names = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            String name = readLine("Name of Player #"+(i+1)+":");
            names[i] = name.isEmpty() ? "Player #"+(i+1) : name;
        }
        
        
        play_game_loop:
        while (true) {
            game = new Game(playerCount,names);
            
            try {
                playGame();
            } catch (RestartException ex) {
                System.out.println("Starting a new game!");
                continue;
            }
            
            while (true) {
                String playAgainStr = readLine("Do you wish to play again (y/n)?");
                if (playAgainStr.equalsIgnoreCase("n")) {
                    System.out.println("Bye-bye!");
                    break play_game_loop;
                } else if (playAgainStr.equalsIgnoreCase("y")) {
                    System.out.println("Starting a new game!");
                    continue play_game_loop;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("RegenWormen -- programmed by Giannis Tsakiris");
        new TextUI().run();
    }
}
