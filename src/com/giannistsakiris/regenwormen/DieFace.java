package com.giannistsakiris.regenwormen;

public enum DieFace {
    FACE_1,
    FACE_2,
    FACE_3,
    FACE_4,
    FACE_5,
    FACE_WORM;
    
    public int value() {
        if (this == FACE_WORM) {
            return 5;
        } else {
            return this.ordinal() + 1;
        }
    }
    
    public String toString() {
        if (this == FACE_WORM) {
            return "*";
        } else {
            return Integer.toString(value());
        }
    }
    
    public static DieFace fromString( String st ) {
        for (DieFace face : values()) {
            if (st.equals(face.toString())) {
                return face;
            }
        }
        return null;
    }
}
