package com.giannistsakiris.regenwormen;

import java.util.Iterator;
import java.util.Stack;

public class Player {
    
    private String name;
    private Stack<Tile> tiles = new Stack<>();
    
    public Player( String name ) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
//    public Stack<Tile> getTiles() {
//        return tiles;
//    }
    
    public int wormCount() {
        int wormCount = 0;
        for (Tile tile : tiles) {
            wormCount += tile.wormCount();
        }
        return wormCount;
    }

    public Tile popTile() {
        return tiles.pop();
    }

    void pushTile(Tile tile) {
        tiles.push(tile);
    }

    public boolean hasTiles() {
        return !tiles.empty();
    }

    public Tile lastTile() {
        return tiles.lastElement();
    }

    public int tileCount() {
        return tiles.size();
    }

    public Iterator<Tile> tileIterator() {
        return tiles.iterator();
    }
    
    @Override
    public String toString() {
        return name;
    }

    void clearTiles() {
        tiles.clear();
    }
}
