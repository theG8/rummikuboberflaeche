package de.g8keeper.rummikuboberflaeche;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;


public class TileOnDragListener implements View.OnDragListener {

    public static final int DRAG_ACTION_UNKNOWN = 0;
    public static final int DRAG_ACTION_FROM_TILESET = 1;
    public static final int DRAG_ACTION_FROM_LANE = 2;

    private static final String TAG = TileOnDragListener.class.getSimpleName();

    private static ITileDropTarget draggedTileParentFragment;
    private static int draggedTileIndex;
    private static int dragAction;


    private ITileDropTarget parentFragment;
    private boolean isTileSet;
    private View draggedView;


    static {
        draggedTileParentFragment = null;
        draggedTileIndex = -1;
        dragAction = DRAG_ACTION_UNKNOWN;
    }


    public TileOnDragListener(ITileDropTarget parentFragment) {

        if (parentFragment instanceof TileSetView) {
            isTileSet = true;
        } else if (parentFragment instanceof FragmentLane) {
            isTileSet = false;
        } else {
            throw new IllegalArgumentException(TAG +
                    " parentFragment != FragmentTileSet && parentFragment != FragmentLane");
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


                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED -> " + draggedView + "(" + x + ", " + y + ")");

                    draggedTileParentFragment = parentFragment;

                    draggedTileIndex = draggedTileParentFragment.getIndexAtPosition(x, y);

                    dragAction = isTileSet ? DRAG_ACTION_FROM_TILESET : DRAG_ACTION_FROM_LANE;


                }
                break;

            case DragEvent.ACTION_DRAG_LOCATION:

                x = (int) event.getX();
                y = (int) event.getY();

                index = parentFragment.getIndexAtPosition(x, y);
                if (!isTileSet) {

                    if (((LinearLayout) draggedView.getParent()).indexOfChild(draggedView) != index) {

                        removeView((View) draggedView.getParent());

                        addView(parentFragment.getLayout(),index);
                    }
                } else {

                    if (dragAction == DRAG_ACTION_FROM_TILESET) {

                        if (((LinearLayout) draggedView.getParent()).indexOfChild(draggedView) != index) {

                            removeView((View) draggedView.getParent());

                            addView(parentFragment.getLayout(),index);
                        }
                    }
                }


                break;

            case DragEvent.ACTION_DROP:

                x = (int) event.getX();
                y = (int) event.getY();

                index = parentFragment.getIndexAtPosition(x, y);

                Log.d(TAG, "onDrag: ACTION_DROP -> " + draggedView + ":");


                if(draggedTileParentFragment == parentFragment &&
                        draggedTileIndex != index) {

                    draggedTileParentFragment.synchronize();

                } else {

                    draggedTileParentFragment.synchronize();
                    parentFragment.synchronize();

                }

                break;


            case DragEvent.ACTION_DRAG_ENDED:

                if (parentFragment.equals(draggedTileParentFragment)) {

                    if (draggedView.getParent() == null) {

                        addView(draggedTileParentFragment.getLayout(), draggedTileIndex);
                    }


                }
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
//
                if (dragAction != DRAG_ACTION_UNKNOWN) {

                    if (!isTileSet) {

                        if (draggedView.getParent() == null) {

                            addView(v);
                        }

                    } else {

                        if (dragAction == DRAG_ACTION_FROM_TILESET && draggedView.getParent() == null) {

                            addView(v);
                        }
                    }
                }

                break;

            case DragEvent.ACTION_DRAG_EXITED:

                if (dragAction != DRAG_ACTION_UNKNOWN) {

                    removeView(v);
                }

                break;
        }

        return true;
    }


    private void addView(View container, int index) {

        ((LinearLayout) container).addView(draggedView, index);

    }

    private void addView(View container) {

        addView(container, -1);


    }

    private void removeView(View container) {

        ((LinearLayout) container).removeView(draggedView);

    }

}
