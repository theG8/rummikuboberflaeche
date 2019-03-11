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
import android.widget.TableLayout;
import android.widget.TableRow;


import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;


public class TileSetFragment extends Fragment {

    private static final String TAG = TileSetFragment.class.getSimpleName();

    private static View dragedViewParent = null;
    private static int dragedTileIndex = -1;

    private TableLayout mLayout;

    private TableRow tableRow1;
    private TableRow tableRow2;

    private TileSet myTiles;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        TableLayout view = new TableLayout(getContext());


        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_standard);
        view.setPadding(pad, pad, pad, pad);



        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.tile_set_background));

        view.setOnDragListener(new MyOnDragListener());


        tableRow1 = new TableRow(getContext());
        tableRow2 = new TableRow(getContext());

//        tableRow1.setLayoutParams(params);
//        tableRow2.setLayoutParams(params);
        tableRow1.setOrientation(LinearLayout.HORIZONTAL);
        tableRow2.setOrientation(LinearLayout.HORIZONTAL);

        tableRow1.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.tile_width) +
                getResources().getDimensionPixelSize(R.dimen.tile_margin));
        tableRow1.setMinimumHeight((int) (tableRow1.getMinimumWidth() * 1.4));

        tableRow2.setMinimumHeight(tableRow1.getMinimumHeight());
        tableRow2.setMinimumWidth(tableRow1.getMinimumWidth());



        view.addView(tableRow1);
        view.addView(tableRow2);

        for(Tile tile: myTiles){
            tableRow1.addView(TileView.newInstance(getContext(),tile));
        }

        mLayout = view;
        return view;

    }

    public static TileSetFragment newInstance(TileSet tileSet) {
        TileSetFragment fragment = new TileSetFragment();
        fragment.setTileSet(tileSet);

        return fragment;
    }

    public TableLayout getLayout() {
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
