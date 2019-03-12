package de.g8keeper.rummikuboberflaeche;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;

import java.util.Random;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TilePool;
import de.g8keeper.rummikub.TileSet;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static View draggedViewParent = null;
    public static int draggedTileIndex = -1;



    private TileSetFragment fragTileSet;

    private LaneFragment laneFragment;
    private LaneFragment laneFragment2;
    private LinearLayout llTileSet;
    private LinearLayout llPlayground;
    private ScrollView svVertical;
    private HorizontalScrollView svHorizontal;
    private HorizontalScrollView scTileSet;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svVertical = findViewById(R.id.sv_vertical);
        svHorizontal = findViewById(R.id.sv_horizontal);
        scTileSet = findViewById(R.id.sv_tile_set);
        llTileSet = findViewById(R.id.ll_tile_set);

        llPlayground = findViewById(R.id.ll_playground);


        llPlayground.setOnDragListener(new ScrollOnDragListener());


        // ******************************************************************************************
        // Testcode:                                                                                *
        // ******************************************************************************************


        TilePool tilePool = new TilePool();
        TileSet tiles = new TileSet();

        for (int i = 0; i < 14; i++) {

            tiles.addTile(tilePool.getTile());

        }

        fragTileSet = TileSetFragment.newInstance(tiles);

        getSupportFragmentManager().beginTransaction().
                add(R.id.ll_tile_set, fragTileSet, "fragTileSet").
                commit();



        Lane lane = new Lane();

        lane.addTile(new Tile(Color.RED,10));
        lane.addTile(new Tile(Color.RED,3));
        lane.addTile(new Tile(Color.RED,8));
        lane.addTile(new Tile(Color.RED,2));
        lane.addTile(new Tile(Color.RED,1));
        lane.addTile(new Tile(Color.RED,5));
        lane.addTile(new Tile(Color.RED,7));

        laneFragment = LaneFragment.newInstance(lane);


        llPlayground.post(() -> {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.ll_playground, laneFragment, "laneFragment").
                    commit();

        });

        llPlayground.post(() -> {
            llPlayground.addView(getSpace());

        });


        lane = new Lane();

        lane.addTile(new Tile(Color.BLUE,2));
        lane.addTile(new Tile(Color.BLUE,7));
        lane.addTile(new Tile(Color.BLUE,12));
        lane.addTile(new Tile(Color.BLUE,4));
        lane.addTile(new Tile(Color.BLUE,3));

        lane.addTile(new Tile(Color.BLUE,9));

        laneFragment2 = LaneFragment.newInstance(lane);


        llPlayground.post(() -> {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.ll_playground, laneFragment2, "laneFragment2").
                    commit();

        });

        llPlayground.post(() -> {
            llPlayground.addView(getSpace());

        });
    }

    private Space getSpace() {
        Space space = new Space(this);
        space.setId(new Random().nextInt());
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));
        return space;
    }


    class ScrollOnDragListener implements View.OnDragListener {
        private final String TAG = ScrollOnDragListener.class.getSimpleName();

        private int scrollBounds = 20;
        int dragedTileIndex;

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            int x, y, index;
            int scrollBorder = 50;
            Rect visibleRect = new Rect();
            Rect virtualRect = new Rect();
            Point offset = new Point();
            Point pos = new Point();

//            String text = event.getClipData().toString();
            View dragedView = (View) event.getLocalState();


            switch (action) {

                case DragEvent.ACTION_DRAG_LOCATION:
                    x = (int) event.getX();
                    y = (int) event.getY();

                    llPlayground.getGlobalVisibleRect(visibleRect, offset);

                    virtualRect.set(0, 0, visibleRect.right - visibleRect.left, visibleRect.bottom - visibleRect.top);
                    pos.set(x - (visibleRect.left - offset.x), y - (visibleRect.top - offset.y));

                    Log.d(TAG, "position: " + pos.x + ", " + pos.y + " visRect: " + visibleRect + " ofs: " + offset + " vir: " + virtualRect);

                    // left
                    if (pos.x < scrollBorder) {
                        svHorizontal.arrowScroll(ScrollView.FOCUS_LEFT);
                    }

                    // right
                    if (pos.x > virtualRect.right - scrollBorder) {
                        svHorizontal.arrowScroll(ScrollView.FOCUS_RIGHT);
                    }

                    // top
                    if (pos.y < scrollBorder) {
                        svVertical.arrowScroll(ScrollView.FOCUS_UP);
                    }

                    // bottom
                    if (pos.y > virtualRect.bottom - scrollBorder) {
                        svVertical.arrowScroll(ScrollView.FOCUS_DOWN);
                    }

                    break;


            }

            return true;
        }
    }
}
