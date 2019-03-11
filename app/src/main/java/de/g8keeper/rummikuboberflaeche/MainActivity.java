package de.g8keeper.rummikuboberflaeche;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TileSetFragment fragTileSet_1;
    private TileSetFragment fragTileSet_2;
    private LinearLayout llPlayground;
    public ScrollView svVertical;
    public HorizontalScrollView svHorizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svVertical = findViewById(R.id.sv_vertical);
        svHorizontal = findViewById(R.id.sv_horizontal);
        llPlayground = findViewById(R.id.ll_playground);



        llPlayground.setOnDragListener(new ScrollOnDragListener());
        // ******************************************************************************************
        // Testcode:                                                                                *
        // ******************************************************************************************

        initFloatingActionButton();

        fragTileSet_1 = TileSetFragment.newInstance(new TileSet());
        fragTileSet_2 = TileSetFragment.newInstance(new TileSet());

        getSupportFragmentManager().beginTransaction().
                add(R.id.ll_playground, fragTileSet_1, "fragTileSet_1").commit();

        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));

        llPlayground.post(() -> {
            llPlayground.addView(space);
            getSupportFragmentManager().beginTransaction().
                    add(R.id.ll_playground, fragTileSet_2, "fragTileSet_2").
                    commit();
        });


    }

    private void initFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener((view) -> {


            TileView tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 12)

            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLUE, 2)
            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLACK, 7)
            );

            fragTileSet_1.getLayout().addView(tileView);


            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.YELLOW, 3)

            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 4)

            );

            fragTileSet_1.getLayout().addView(tileView);
            tileView = TileView.newInstance(
                    this,
                    new Tile(true)

            );

            fragTileSet_1.getLayout().addView(tileView);

            //*******************************************

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 12)

            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLUE, 2)

            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLACK, 7)

            );

            fragTileSet_2.getLayout().addView(tileView);


            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.YELLOW, 3)

            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 4)
            );

            fragTileSet_2.getLayout().addView(tileView);
            tileView = TileView.newInstance(
                    this,
                    new Tile(true)
            );

            fragTileSet_2.getLayout().addView(tileView);


        });

    }

    class ScrollOnDragListener implements View.OnDragListener {
        private final String TAG = ScrollOnDragListener.class.getSimpleName();

        private int scrollBounds = 50;
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

                    virtualRect.set(0,0,visibleRect.right - visibleRect.left, visibleRect.bottom - visibleRect.top);
                    pos.set(x - (visibleRect.left - offset.x), y - (visibleRect.top - offset.y));

                    Log.d(TAG, "position: " + pos.x + ", " + pos.y + " visRect: " + visibleRect + " ofs: " + offset + " vir: " + virtualRect);

                    // left
                    if (pos.x < scrollBorder){
                        svHorizontal.arrowScroll(ScrollView.FOCUS_LEFT);
                    }

                    // right
                    if (pos.x > virtualRect.right - scrollBorder){
                        svHorizontal.arrowScroll(ScrollView.FOCUS_RIGHT);
                    }

                    // top
                    if (pos.y < scrollBorder) {
                        svHorizontal.arrowScroll(ScrollView.FOCUS_UP);
                    }

                    // bottom
                    if (pos.y > virtualRect.bottom - scrollBorder){
                        svHorizontal.arrowScroll(ScrollView.FOCUS_DOWN);
                    }

                    break;


            }

            return true;
        }
    }
}
