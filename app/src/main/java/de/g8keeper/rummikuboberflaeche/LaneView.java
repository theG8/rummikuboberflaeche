package de.g8keeper.rummikuboberflaeche;

import android.content.Context;
import android.view.ViewGroup;

import de.g8keeper.rummikub.Lane;




public class LaneView extends TileSetView {

    private Context context;

    public LaneView(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {
        this.context = context;


        setBackgroundColor(getResources().getColor(R.color.lane_background));

        int p = (int) getResources().getDimension(R.dimen.padding_tile_set);
        setPadding(p, p, p, p);

        setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.tile_width) +
                getResources().getDimensionPixelSize(R.dimen.tile_margin));
        setMinimumHeight((int) (getMinimumWidth() * 1.4));

        this.setOnDragListener(new TileOnDragListener(this));


        refreshUI();
    }


}
