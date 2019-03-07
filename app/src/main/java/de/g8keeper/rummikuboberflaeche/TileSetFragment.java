package de.g8keeper.rummikuboberflaeche;

import android.graphics.Point;
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

import de.g8keeper.rummikub.TileSet;




public class TileSetFragment extends Fragment {

    private static final String TAG = TileSetFragment.class.getSimpleName();

    private LinearLayout mLayout;
    private TileSet myTiles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        final View view = new LinearLayout(getContext());
        LinearLayout view = new LinearLayout(getContext());


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_standard);
        view.setPadding(pad ,pad,pad,pad);

        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.tile_set_background));

        view.setOnDragListener(new MyOnDragListener());

        mLayout = view;
        return view;

    }

    public static TileSetFragment newInstance(TileSet tileSet) {
        TileSetFragment fragment = new TileSetFragment();
        fragment.setTileSet(tileSet);

        return fragment;
    }

    public LinearLayout getLayout(){
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


    private void printPositions(){

        Log.d(TAG, "printPositions: ChildCount = " + mLayout.getChildCount());

        for (int i = 0; i < mLayout.getChildCount();i++){

            View view = mLayout.getChildAt(i);

            Log.d(TAG, "printPositions: position " + i + " -> " + view.toString() + " " + view.getClipBounds());

            Rect r = new Rect(view.getLeft(),view.getTop(), view.getRight(),view.getBottom());
            Point offset = new Point();

            Log.d(TAG, "                  r-bevore: " + r.toString());
            mLayout.getChildVisibleRect(mLayout.getChildAt(i),r,offset);
            Log.d(TAG, "                         r: " + r.toString() + " offset: " + offset.toString());





        }

    }





    class MyOnDragListener implements View.OnDragListener{
        private final String TAG = MyOnDragListener.class.getSimpleName();


        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            View view = (View) event.getLocalState();



            switch(action){
                case DragEvent.ACTION_DRAG_ENTERED:



                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED -> " + view);

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED -> " + view);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "onDrag: ACTION_DROP -> " + view);
                    printPositions();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED -> " + view);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION -> " + view + " -> " +
                            ((int)event.getX()) + "/" + ((int)event.getY()));

                    break;
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + view);
                    break;

            }

            return true;
        }
    }

}
