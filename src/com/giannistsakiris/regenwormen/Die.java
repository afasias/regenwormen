package com.giannistsakiris.regenwormen;

public class Die {
    
    private static java.util.Random random = new java.util.Random();
    
    private DieFace face;
    
    public Die() {
        roll();
    }
    
    final public DieFace roll() {
//        return face = DieFace.FACE_WORM;
//        return face = DieFace.values()[random.nextInt(DieFace.values().length-3)+3];
        return face = DieFace.values()[random.nextInt(DieFace.values().length)];
    }
    
    // remove this
    public void setFace( DieFace face ) {
        this.face = face;
    }
    
    public DieFace getFace() {
        return face;
    }
}
