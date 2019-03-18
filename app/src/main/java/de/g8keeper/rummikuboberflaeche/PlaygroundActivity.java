package de.g8keeper.rummikuboberflaeche;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.Random;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TilePool;
import de.g8keeper.rummikub.TileSet;
import de.g8keeper.rummikub.Turn;
import de.g8keeper.rummikub.database.DataSource;


public class PlaygroundActivity extends AppCompatActivity {

    private static final String TAG = PlaygroundActivity.class.getSimpleName();

    public static View draggedViewParent = null;
    public static int draggedTileIndex = -1;

    private Game mGame;
    private Turn mTurn;

    private FragmentTileSet fragTileSet;

    private FragmentLane mFragmentLane;
    private FragmentLane mFragmentLane2;

    private ScrollView svVertical;
    private HorizontalScrollView svHorizontal;
    private LinearLayout llPlayground;

    private HorizontalScrollView svTileSet;
    private LinearLayout llTileSet;
    private TileSetView tileSetView;

    private DataSource mDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);

        mDataSource = new DataSource(this);

//        long gameID = getIntent().getLongExtra("game",-1L);

        mGame = getIntent().getParcelableExtra("game");

        if(mGame != null){
            mGame.setDataSource(mDataSource);

            Log.d(TAG, "onCreate: " + mGame.toString(true));

            for(Player player :mGame.getPlayers()){
                Log.d(TAG, "\t" + player.toStringWithTiles());
            }

            AlertDialog dialogAdd = createGameDialog();
            dialogAdd.show();

        } else {
            finish();
        }

        svVertical = findViewById(R.id.sv_vertical);
        svHorizontal = findViewById(R.id.sv_horizontal);
        svTileSet = findViewById(R.id.sv_tile_set);
        llTileSet = findViewById(R.id.ll_tile_set);

        llPlayground = findViewById(R.id.ll_playground);

        llPlayground.setOnDragListener(new ScrollOnDragListener());


        tileSetView = new TileSetView(this);
        llTileSet.addView(tileSetView);
        tileSetView.setOnDragListener(new TileOnDragListener(tileSetView));


//        testCode();
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    private void setUpLanes(){

        LaneView laneView;



        for(Lane lane: mTurn.lanes()){

            laneView = new LaneView(this);
            laneView.setTileSet(lane);
            llPlayground.addView(laneView);
            Log.d(TAG, "setUpLanes: Added Lane: " + lane);
        }

        //leerreihe
        laneView = new LaneView(this);
        llPlayground.addView(laneView);
        Log.d(TAG, "setUpLanes: Added empty Lane");

    }

    private void setUpTileSet(){

        tileSetView.setTileSet(mTurn.tileSet());
//        tileSetView.addView(tileSetView);


    }



    private Space getSpace() {
        Space space = new Space(this);
        space.setId(new Random().nextInt());
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));
        return space;
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab_playground_button:

                Log.d(TAG, "Turn: " + mTurn);
                break;
        }

    }



    private void testCode(){
        // ******************************************************************************************
        // Testcode:                                                                                *
        // ******************************************************************************************


        TilePool tilePool = new TilePool();
        TileSet tiles = new TileSet();

        for (int i = 0; i < 14; i++) {

            tiles.addTile(tilePool.getTile());

        }

        fragTileSet = FragmentTileSet.newInstance(tiles);

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

        mFragmentLane = FragmentLane.newInstance(lane);


        llPlayground.post(() -> {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.ll_playground, mFragmentLane, "mFragmentLane").
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

        mFragmentLane2 = FragmentLane.newInstance(lane);


        llPlayground.post(() -> {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.ll_playground, mFragmentLane2, "mFragmentLane2").
                    commit();

        });

        llPlayground.post(() -> {
            llPlayground.addView(getSpace());

        });
    }


    private AlertDialog createGameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_game_main, null);


        final TextView tvPlayers = view.findViewById(R.id.dlg_game_tv_players);

        tvPlayers.setText("");
        int i = 1;
        for(Player player: mGame.getPlayers()){
            tvPlayers.append(i++ + ":\t" +player.getName() + ",\n");
        }


        String positiveText = "";

        if (mGame.state() == Game.STATE_NOT_STARTED){
            positiveText = "Spiel starten";
        } else if (mGame.state() == Game.STATE_RUNNING){
            positiveText = "weiterspielen";
        }

        builder.setView(view).
                setTitle(mGame.getTitle())
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mGame.state() == Game.STATE_NOT_STARTED) {
                            mGame.startGame();

                        }

                        mTurn = mGame.getTurn();

                        setUpTileSet();
                        setUpLanes();

                    }
                }).
                setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });


        return builder.create();
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

//                    Log.d(TAG, "position: " + pos.x + ", " + pos.y + " visRect: " + visibleRect + " ofs: " + offset + " vir: " + virtualRect);

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
