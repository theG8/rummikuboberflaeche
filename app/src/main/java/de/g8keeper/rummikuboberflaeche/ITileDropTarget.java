package de.g8keeper.rummikuboberflaeche;

import android.view.View;
import android.widget.LinearLayout;

public interface ITileDropTarget {

    int getIndexAtPosition(int x, int y);

    void synchronize();

    LinearLayout getLayout();


}
