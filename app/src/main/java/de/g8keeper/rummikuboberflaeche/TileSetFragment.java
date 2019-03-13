package de.g8keeper.rummikuboberflaeche;


import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;


public class TileSetFragment extends Fragment implements ITileDragDrop{

    private static final String TAG = TileSetFragment.class.getSimpleName();

//    private static View draggedViewParent = null;
//    private static int draggedTileIndex = -1;


    private LinearLayout mLayout;

    private TileSet myTileSet;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        LinearLayout view = new LinearLayout(getContext());
        mLayout = view;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setMinimumWidth(container.getWidth());

        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_tile_set);
        view.setPadding(pad, pad, pad, pad);



        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.tile_set_background));


        view.setOnDragListener(new MainOnDragListener(this));




        refreshUI();

        return view;

    }

    private void synchronizeTileSet(){

        int childCount = mLayout.getChildCount();

        myTileSet.clear();

        if(childCount > 0) {
            for (int i = 0; i< childCount;i++){
                myTileSet.addTile(((TileView) mLayout.getChildAt(i)).getTile());
            }
        }
    }

    private void refreshUI(){

        mLayout.removeAllViews();

        if(myTileSet != null) {

            for (Tile tile : myTileSet) {
                mLayout.addView(TileView.newInstance(getContext(), tile));
            }
        }
    }

    public static TileSetFragment newInstance(TileSet tileSet) {
        TileSetFragment fragment = new TileSetFragment();
        fragment.setTileSet(tileSet);

        return fragment;
    }



    private void setTileSet(TileSet tileSet) {
        if (myTileSet == null) {
            myTileSet = tileSet;
        }

    }

    public TileSet getTileSet() {
        return myTileSet;
    }

    @Override
    public LinearLayout getLayout(){
        return mLayout;
    }

    @Override
    public int getIndexAtPosition(int x, int y) {

        for (int i = 0; i < mLayout.getChildCount(); i++) {

            View view = mLayout.getChildAt(i);
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            Log.d(TAG, "position " + i + " -> " + view.toString() + " " + rect);
            if (rect.contains(x, y)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void synchronize() {

        synchronizeTileSet();

    }
}
