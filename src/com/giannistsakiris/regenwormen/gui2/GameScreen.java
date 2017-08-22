package com.giannistsakiris.regenwormen.gui2;

import com.giannistsakiris.regenwormen.*;
import java.awt.Dimension;
import javax.swing.JComponent;

public class GameScreen extends JComponent {

    public GameScreen( Game game ) {
        setLayout(null);
        setPreferredSize(new Dimension(1024,800));
    }

}
