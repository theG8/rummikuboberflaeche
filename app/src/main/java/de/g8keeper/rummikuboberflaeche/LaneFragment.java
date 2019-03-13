package de.g8keeper.rummikuboberflaeche;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Tile;


public class LaneFragment extends Fragment implements ITileDragDrop {

    private static final String TAG = LaneFragment.class.getSimpleName();

//    private static View draggedViewParent = null;
//    private static int draggedTileIndex = -1;

    private LinearLayout mLayout;
    private Lane myLane;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        LinearLayout view = new LinearLayout(getContext());
        mLayout = view;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_standard);
        view.setPadding(pad, pad, pad, pad);

        view.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.tile_width) +
                getResources().getDimensionPixelSize(R.dimen.tile_margin));
        view.setMinimumHeight((int) (view.getMinimumWidth() * 1.4));

        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.lane_background));


        view.setOnDragListener(new MainOnDragListener(this));


        refreshUI();

        return view;

    }

    private void synchronizeLane(){

        int childCount = mLayout.getChildCount();

        myLane.clear();

        if(childCount > 0) {
            for (int i = 0; i< childCount;i++){
                myLane.addTile(((TileView) mLayout.getChildAt(i)).getTile());
            }
        }
    }

    private void refreshUI(){

        mLayout.removeAllViews();

        for (Tile tile : myLane) {
            mLayout.addView(TileView.newInstance(getContext(), tile));
        }

    }


    public static LaneFragment newInstance(Lane lane) {
        LaneFragment fragment = new LaneFragment();
        fragment.setLane(lane);

        return fragment;
    }


    private void setLane(Lane lane) {
        if (myLane == null) {
            myLane = lane;
        }

    }

    public Lane getLane() {
        return myLane;
    }


    @Override
    public LinearLayout getLayout() {
        return mLayout;
    }

    @Override
    public int getIndexAtPosition(int x, int y) {

//        Log.d(TAG, "printPositions: ChildCount = " + mLayout.getChildCount());

        for (int i = 0; i < mLayout.getChildCount(); i++) {

            View view = mLayout.getChildAt(i);
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
//            Log.d(TAG, "position " + i + " -> " + view.toString() + " " + rect);
            if (rect.contains(x, y)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void synchronize() {
        Log.d(TAG, "synchronize!");
        synchronizeLane();
    }
}