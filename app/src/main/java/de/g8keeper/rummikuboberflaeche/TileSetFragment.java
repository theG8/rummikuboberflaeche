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


import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;


public class TileSetFragment extends Fragment {

    private static final String TAG = TileSetFragment.class.getSimpleName();

//    private static View draggedViewParent = null;
//    private static int draggedTileIndex = -1;


    private LinearLayout mLayout;

    private TileSet myTiles;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        LinearLayout view = new LinearLayout(getContext());


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setMinimumWidth(container.getWidth());

        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_tile_set);
        view.setPadding(pad, pad, pad, pad);



        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.tile_set_background));

        view.setOnDragListener(new TileSetOnDragListener());




        for(Tile tile: myTiles){
            view.addView(TileView.newInstance(getContext(),tile));
        }

        mLayout = view;
        return view;

    }

    public static TileSetFragment newInstance(TileSet tileSet) {
        TileSetFragment fragment = new TileSetFragment();
        fragment.setTileSet(tileSet);

        return fragment;
    }

    public LinearLayout getLayout() {
        return mLayout;
    }

    private void setTileSet(TileSet tileSet) {
        if (myTiles == null) {
            myTiles = tileSet;
        }

    }

    public TileSet getTileSet() {
        return myTiles;
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


    class TileSetOnDragListener implements View.OnDragListener {
        private final String TAG = TileSetOnDragListener.class.getSimpleName();

        View draggedView = null;


        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            int x, y, index;

            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    draggedView = (View) event.getLocalState();

                    if(v == draggedView.getParent()) {
                        Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + draggedView);

                        x = (int) event.getX();
                        y = (int) event.getY();


                        MainActivity.draggedViewParent = v;
                        MainActivity.draggedTileIndex = getIndexAtPosition(x, y);

                        Log.d(TAG, "onDrag: dVP: " + MainActivity.draggedViewParent.toString() + " dTIndex: " + MainActivity.draggedTileIndex);
                    }
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:

                    x = (int) event.getX();
                    y = (int) event.getY();

                    index = getIndexAtPosition(x, y);

                    if (((LinearLayout) draggedView.getParent()).indexOfChild(draggedView) != index) {

                        ((LinearLayout) draggedView.getParent()).removeView(draggedView);

                        mLayout.addView(draggedView, index);
                    }

                    break;

                case DragEvent.ACTION_DROP:


                    x = (int) event.getX();
                    y = (int) event.getY();

                    index = getIndexAtPosition(x, y);

                    Log.d(TAG, "onDrag: ACTION_DROP -> " + draggedView + " move to index " + index + " " + mLayout.getId());

                    break;


                case DragEvent.ACTION_DRAG_ENDED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED -> " + draggedView + " getResult(): " +  event.getResult());


                    if(draggedView.getParent() == null){
                        Log.d(TAG, "onDrag: getParent() == null put back to " + MainActivity.draggedViewParent);
                        ((LinearLayout) MainActivity.draggedViewParent).addView(draggedView, MainActivity.draggedTileIndex);
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED -> " + draggedView + " (" + v + ")");

                    if (draggedView.getParent() == null) {
                        ((LinearLayout) v).addView(draggedView);
                    }

                    break;
                case DragEvent.ACTION_DRAG_EXITED:


//                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED -> " + draggedView + " (" + v + ")");

                    ((LinearLayout) v).removeView(draggedView);
                    break;
            }

            return true;
        }
    }

}
