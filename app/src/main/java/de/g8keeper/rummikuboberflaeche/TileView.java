package de.g8keeper.rummikuboberflaeche;

import android.content.Context;
import android.view.View;

public class TileView extends View {

    private static final String TAG = TileView.class.getSimpleName();

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public TileView(Context context) {
        super(context);
    }



}
