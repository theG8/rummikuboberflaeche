package de.g8keeper.rummikuboberflaeche;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
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
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        view.setLayoutParams(params);

        int pad = getResources().getDimensionPixelOffset(R.dimen.padding_standard);
        view.setPadding(pad, pad, pad, pad);

        view.setOrientation(LinearLayout.HORIZONTAL);

        view.setBackgroundColor(getContext().getColor(R.color.tile_set_background));

        view.setOnDragListener(new MyOnDragListener());

        view.setOnClickListener(v -> {for (int i = 0; i < mLayout.getChildCount(); i++) {

            View child = mLayout.getChildAt(i);
            Rect rect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            Log.d(TAG, "position " + i + " -> " + child.toString() + " " + rect);

        }});

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


    class MyOnDragListener implements View.OnDragListener {
        private final String TAG = MyOnDragListener.class.getSimpleName();
        ImageView dummyView = null ;
        int dragedTileIndex;

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            int x, y, index;

            String description;

//            String text = event.getClipData().toString();
            View dragedView = (View) event.getLocalState();



            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    String text = event.getClipDescription().toString();
                    description = event.getClipDescription().getLabel().toString();
                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + dragedView + " description: " + description +" text: " + text);

                    x = (int) event.getX();
                    y = (int) event.getY();

                    dragedTileIndex = getIndexAtPosition(x, y);

                    break;

                case DragEvent.ACTION_DRAG_LOCATION:

                    x = (int) event.getX();
                    y = (int) event.getY();
                    index = getIndexAtPosition(x, y);
                    description = event.getClipDescription().getLabel().toString();

                    if (mLayout.toString().equals(description)) {
                        if (index != -1) {
                            mLayout.removeView(dragedView);
                            mLayout.addView(dragedView, index);
                        }
                    }


//                    Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION -> " + dragedView + " -> " +
//                            x + "/" + y + " index: " + index + " dummyView: " + mLayout.indexOfChild(dummyView));
//                    if (dummyView != null) {
//                        if (index != -1 && index != dragedTileIndex && mLayout.indexOfChild(dummyView) != index ) {
//                            mLayout.removeView(dummyView);
//                            mLayout.addView(dummyView, index);
//                        }
//                    } else {
//                        dummyView = new ImageView(getContext());
//                        dummyView.setLayoutParams(dragedView.getLayoutParams());
//                        if(index != -1){
//                            mLayout.addView(dummyView,index);
//                        } else {
//                            mLayout.addView(dummyView);
//                        }
//                    }

                    break;

                case DragEvent.ACTION_DROP:

                    x = (int) event.getX();
                    y = (int) event.getY();



                    index = getIndexAtPosition(x, y);

                    description = event.getClipDescription().getLabel().toString();

                    if (mLayout.toString().equals(description)) {
//
//                    View vMove = mLayout.getChildAt(index);
//
//
//
//                    dragedView.animate().
//                            x(vMove.getX()).
//                            y(vMove.getY()).
//                            setDuration(1000).
//                            start();


                        mLayout.removeView(dragedView);
                        mLayout.addView(dragedView, index);
                    }
//                    Log.d(TAG, "onDrag: ACTION_DROP -> " + dragedView + " move to index " + index);

                    break;


//                case DragEvent.ACTION_DRAG_ENDED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED -> " + dragedView);
//                    break;

//                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED -> " + dragedView);
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED -> " + dragedView);
//                    break;
            }

            return true;
        }
    }

}
