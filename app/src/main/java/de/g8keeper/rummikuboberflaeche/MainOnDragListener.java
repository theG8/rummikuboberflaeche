package de.g8keeper.rummikuboberflaeche;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;


public class MainOnDragListener implements View.OnDragListener {

    public static final int DRAG_ACTION_UNKNOWN = 0;
    public static final int DRAG_ACTION_FROM_TILESET = 1;
    public static final int DRAG_ACTION_FROM_LANE = 2;

    private static final String TAG = MainOnDragListener.class.getSimpleName();
    private static View draggedTileParent;
    private static ITileDragDrop draggedTileParentFragment;
    private static int draggedTileIndex;
    private static int dragAction;


    private ITileDragDrop parentFragment;
    private boolean isTileSet;
    private View draggedView;


    static {
        draggedTileParentFragment = null;
        draggedTileParent = null;
        draggedTileIndex = -1;
        dragAction = DRAG_ACTION_UNKNOWN;
    }


    public MainOnDragListener(ITileDragDrop parentFragment) {

        if (parentFragment instanceof TileSetFragment) {
            isTileSet = true;
        } else if (parentFragment instanceof LaneFragment) {
            isTileSet = false;
        } else {
            throw new IllegalArgumentException(TAG +
                    " parentFragment != TileSetFragment && parentFragment != LaneFragment");
        }

        this.parentFragment = parentFragment;
        this.draggedView = null;
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {

        int action = event.getAction();
        int x, y, index;

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:

                draggedView = (View) event.getLocalState();

                if (v == draggedView.getParent()) {


                    x = (int) event.getX();
                    y = (int) event.getY();


                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + draggedView + "(" + x  + ", " + y + ")");

                    draggedTileParentFragment = parentFragment;
                    draggedTileParent = draggedTileParentFragment.getLayout();
//                    draggedTileParent = v;
                    draggedTileIndex = parentFragment.getIndexAtPosition(x, y);

                    dragAction = isTileSet ? DRAG_ACTION_FROM_TILESET : DRAG_ACTION_FROM_LANE;

                    Log.d(TAG, "dragAction: " + dragAction + " TileParent: " + draggedTileParentFragment.getLayout().toString() +
                            " TileIndex: " + draggedTileIndex);
                }
                break;

            case DragEvent.ACTION_DRAG_LOCATION:

                x = (int) event.getX();
                y = (int) event.getY();

                index = parentFragment.getIndexAtPosition(x, y);
                if(!isTileSet){

                    if (((LinearLayout) draggedView.getParent()).indexOfChild(draggedView) != index) {

                        ((LinearLayout) draggedView.getParent()).removeView(draggedView);

                        parentFragment.getLayout().addView(draggedView, index);
                    }
                } else {

                    if(dragAction == DRAG_ACTION_FROM_TILESET) {

                        if (((LinearLayout) draggedView.getParent()).indexOfChild(draggedView) != index) {

                            ((LinearLayout) draggedView.getParent()).removeView(draggedView);

                            parentFragment.getLayout().addView(draggedView, index);
                        }
                    }
                }



                break;

            case DragEvent.ACTION_DROP:


                x = (int) event.getX();
                y = (int) event.getY();

                index = parentFragment.getIndexAtPosition(x, y);

                Log.d(TAG, "onDrag: ACTION_DROP -> " + draggedView + " move to index " +
                        index + " " + parentFragment.getLayout().getId());

                break;


            case DragEvent.ACTION_DRAG_ENDED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED -> " + draggedView + " getResult(): " +  event.getResult());


                if (draggedView.getParent() == null) {
                    Log.d(TAG, "onDrag: getParent() == null put back to " +
                            draggedTileParentFragment.getLayout().toString());

                    draggedTileParentFragment.getLayout().
                            addView(draggedView, draggedTileIndex);
                }

                break;

            case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED -> " + draggedView + " (" + v + ")");

                if (dragAction != DRAG_ACTION_UNKNOWN) {
                    if (!isTileSet) {

                        if (draggedView.getParent() == null) {
                            ((LinearLayout) v).addView(draggedView);
                        }

                    } else {

                        if (dragAction == DRAG_ACTION_FROM_TILESET && draggedView.getParent() == null) {
                            ((LinearLayout) v).addView(draggedView);
                        }
                    }
                }

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                //                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED -> " + draggedView + " (" + v + ")");

                if (dragAction != DRAG_ACTION_UNKNOWN) {


                        ((LinearLayout) v).removeView(draggedView);


                }


                break;
        }

        return true;
    }
}
