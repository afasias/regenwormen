package com.giannistsakiris.regenwormen;

public enum Tile {
    TILE_21,
    TILE_22,
    TILE_23,
    TILE_24,
    TILE_25,
    TILE_26,
    TILE_27,
    TILE_28,
    TILE_29,
    TILE_30,
    TILE_31,
    TILE_32,
    TILE_33,
    TILE_34,
    TILE_35,
    TILE_36;
    
    public int value() {
        return this.ordinal() + 21;
    }
    
    public int wormCount() {
        return this.ordinal() / 4 + 1;
    }
}
