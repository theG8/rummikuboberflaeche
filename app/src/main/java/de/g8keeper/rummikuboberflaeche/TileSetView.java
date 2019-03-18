package de.g8keeper.rummikuboberflaeche;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;


public class TileSetView extends LinearLayout implements ITileDropTarget {

    private Context context;
    private TileSet myTileSet;


    public TileSetView(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {
        this.context = context;

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(getResources().getColor(R.color.tile_set_background));

        int p = (int) getResources().getDimension(R.dimen.padding_tile_set);
        setPadding(p, p, p, p);

        myTileSet = new TileSet();

        this.setOnDragListener(new TileOnDragListener(this));


        refreshUI();
    }


    private void synchronizeLane() {

        int childCount = this.getChildCount();

        myTileSet.clear();

        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                myTileSet.addTile(((TileView) this.getChildAt(i)).getTile());
            }
        }
    }

    public void refreshUI() {

        this.removeAllViews();

        if (myTileSet != null) {

            for (Tile tile : myTileSet) {
                this.addView(TileView.newInstance(getContext(), tile));
            }
        }

    }

    public void setTileSet(TileSet tileSet) {

        myTileSet = tileSet;
        refreshUI();

    }

    public TileSet getTileSet() {
        return myTileSet;
    }


    @Override
    public int getIndexAtPosition(int x, int y) {
        for (int i = 0; i < this.getChildCount(); i++) {

            View view = this.getChildAt(i);
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

            if (rect.contains(x, y)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void synchronize() {
        synchronizeLane();
    }

    @Override
    public LinearLayout getLayout() {
        return this;
    }
}
