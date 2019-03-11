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
import de.g8keeper.rummikub.TileSet;


public class LaneFragment extends Fragment {

    private static final String TAG = LaneFragment.class.getSimpleName();

    private static View dragedViewParent = null;
    private static int dragedTileIndex = -1;

    private LinearLayout mLayout;
    private Lane myLane;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        LinearLayout view = new LinearLayout(getContext());


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

        view.setOnDragListener(new MyOnDragListener());

        myLane.sort();

        for(Tile tile: myLane){
            view.addView(TileView.newInstance(getContext(),tile));
        }

        mLayout = view;
        return view;

    }

    public static LaneFragment newInstance(Lane lane) {
        LaneFragment fragment = new LaneFragment();
        fragment.setLane(lane);

        return fragment;
    }

    public LinearLayout getLayout() {
        return mLayout;
    }

    private void setLane(Lane lane) {
        if (myLane == null) {
            myLane = lane;
        }

    }

    public Lane getMyLane() {
        return myLane;
    }


    private int getIndexAtPosition(int x, int y) {

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


    class MyOnDragListener implements View.OnDragListener {
        private final String TAG = MyOnDragListener.class.getSimpleName();

        View dragedView = null;


        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            int x, y, index;

            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    dragedView = (View) event.getLocalState();

                    if(v == dragedView.getParent()) {
                        Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + dragedView);

                        x = (int) event.getX();
                        y = (int) event.getY();


                        dragedViewParent = v;
                        dragedTileIndex = getIndexAtPosition(x, y);

                        Log.d(TAG, "onDrag: dVP: " + dragedViewParent.toString() + " dTIndex: " + dragedTileIndex);
                    }
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:

                    x = (int) event.getX();
                    y = (int) event.getY();

                    index = getIndexAtPosition(x, y);

                    if (((LinearLayout) dragedView.getParent()).indexOfChild(dragedView) != index) {

                        ((LinearLayout) dragedView.getParent()).removeView(dragedView);

                        mLayout.addView(dragedView, index);
                    }

                    break;

                case DragEvent.ACTION_DROP:


                    x = (int) event.getX();
                    y = (int) event.getY();

                    index = getIndexAtPosition(x, y);

                    Log.d(TAG, "onDrag: ACTION_DROP -> " + dragedView + " move to index " + index + " " + mLayout.getId());

                    break;


                case DragEvent.ACTION_DRAG_ENDED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED -> " + dragedView + " getResult(): " +  event.getResult());


                    if(dragedView.getParent() == null){
                        Log.d(TAG, "onDrag: getParent() == null put back to " + dragedViewParent);
                        ((LinearLayout)dragedViewParent).addView(dragedView,dragedTileIndex);
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED -> " + dragedView + " (" + v + ")");

                    if (dragedView.getParent() == null) {
                        ((LinearLayout) v).addView(dragedView);
                    }

                    break;
                case DragEvent.ACTION_DRAG_EXITED:


//                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED -> " + dragedView + " (" + v + ")");

                    ((LinearLayout) v).removeView(dragedView);
                    break;
            }

            return true;
        }
    }

}
