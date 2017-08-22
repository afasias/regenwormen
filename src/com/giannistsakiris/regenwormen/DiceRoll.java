package com.giannistsakiris.regenwormen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

public class DiceRoll {
    
    final static int DEFAULT_DICE_COUNT = 8;
    
    private List<Die> playedDice = new ArrayList<>();
    private Set<Die> remainingDice = new HashSet<>();
    private List<DieFace> playedFaces = new ArrayList<>();
    
    private boolean rolled = false;
    
    public DiceRoll() {
        this(DEFAULT_DICE_COUNT);
    }
    
    public DiceRoll( int diceCount ) {
        for (int i = 0; i < diceCount; i++) {
            remainingDice.add(new Die());
        }
    }
    
    public void reset() {
        remainingDice.addAll(getPlayedDice());
        playedDice.clear();
        playedFaces.clear();
        rolled = false;
    }
    
    public void roll() {
//        if (remainingDice.size() == 8) {
//            int count = 0;
//            for (Die die : remainingDice) {
//                if (count < 5) {
//                    die.setFace(DieFace.FACE_WORM);
//                } else {
//                    die.setFace(DieFace.FACE_1);
//                }
//                count++;
//            }
//        } else
        for (Die die : getRemainingDice()) {
            die.roll();
        }
        rolled = true;
    }
    
    public Collection<DieFace> possibleChoices() {
        Collection<DieFace> choices = new TreeSet<>();
        for (Die die : getRemainingDice()) {
            choices.add(die.getFace());
        }
        for (Die die : getPlayedDice()) {
            choices.remove(die.getFace());
        }
        return choices;
    }

    public void play( DieFace chosenFace ) {
        Set<Die> diceToRemove = new HashSet<>();
        for (Die die : getRemainingDice()) {
            if (die.getFace() == chosenFace) {
                diceToRemove.add(die);
                getPlayedDice().add(die);
            }
        }
        for (Die die : diceToRemove) {
            getRemainingDice().remove(die);
        }
        getPlayedFaces().add(chosenFace);
        rolled = false;
    }
    
    public boolean isRollable() {
        return getRemainingDice().size() > 0 && getPlayedFaces().size() < DieFace.values().length; 
    }
    
    public int score() {
        int score = 0;
        for (Die die : getPlayedDice()) {
            score += die.getFace().value();
        }
        return score;
    }
    
    public boolean isValid() {
        return getPlayedFaces().contains(DieFace.FACE_WORM);
    }
    
    public boolean hasRolled() {
        return rolled;
    }

    public List<Die> getPlayedDice() {
        return playedDice;
    }

    public Set<Die> getRemainingDice() {
        return remainingDice;
    }

    public List<DieFace> getPlayedFaces() {
        return playedFaces;
    }
    
}
