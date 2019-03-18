package de.g8keeper.rummikuboberflaeche;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.PersistableBundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Move;
import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TilePool;
import de.g8keeper.rummikub.TileSet;
import de.g8keeper.rummikub.database.DataSource;


public class PlaygroundActivity extends AppCompatActivity {

    private static final String TAG = PlaygroundActivity.class.getSimpleName();

    public static View draggedViewParent = null;
    public static int draggedTileIndex = -1;

    private Game mGame;
    private Move mMove;

    private FragmentTileSet fragTileSet;

    private FragmentLane mFragmentLane;
    private FragmentLane mFragmentLane2;

    private ScrollView svVertical;
    private HorizontalScrollView svHorizontal;
    private LinearLayout llPlayground;

    private HorizontalScrollView svTileSet;
    private LinearLayout llTileSet;
    private TileSetView tileSetView;

    private TextView tvPlayer;

    private DataSource mDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        Log.d("DEBUG", "onCreate: ");
        mDataSource = new DataSource(this);

//        long gameID = getIntent().getLongExtra("game",-1L);

        mGame = getIntent().getParcelableExtra("game");


        if (mGame != null) {
            mGame.setDataSource(mDataSource);

            Log.d(TAG, "onCreate: " + mGame.toString(true));

            for (Player player : mGame.getPlayers()) {
                Log.d(TAG, "\t" + player.toStringWithTiles());
            }

            AlertDialog dialogAdd = createGameDialog();
            dialogAdd.show();

        } else {
            finish();
        }

        tvPlayer = findViewById(R.id.tv_playground_player);

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
        Log.d("DEBUG", "onResume: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUG", "onPause: ");

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("DEBUG", "onSaveInstanceState: ");
    }

    private void setUpLanes() {

        LaneView laneView;


        // adding empty lane for dropping
//        mMove.lanes().add(new Lane());

        llPlayground.removeAllViews();

        checkForOneEmptyLane();

        for (Lane lane : mMove.lanes()) {

            laneView = new LaneView(this);
            laneView.setTileSet(lane);
            llPlayground.addView(laneView);
            Log.d(TAG, "setUpLanes: Added Lane: " + lane);
        }

//        //leerreihe
//        laneView = new LaneView(this);
//
//        llPlayground.addView(laneView);
//        Log.d(TAG, "setUpLanes: Added empty Lane");

    }

    private void setUpTileSet() {

        tileSetView.setTileSet(mMove.tileSet());
//        tileSetView.addView(tileSetView);
    }

    private void checkForOneEmptyLane() {
        List<Integer> indexList = new ArrayList<>();


        for (int i = 0; i < mMove.lanes().size(); i++) {
            if (mMove.lanes().get(i).isEmpty()) {
                indexList.add(i);
            }
        }

        if (indexList.size() == 0) {
            // add a empty lane
            mMove.lanes().add(new Lane());
        } else if (indexList.size() > 1) {
            for (int i = 0; i < indexList.size() - 1; i++) {
                mMove.lanes().remove(indexList.get(i));
            }
        }

    }

    private void removeEmptyLanes() {
        List<Integer> indexList = new ArrayList<>();


        for (int i = 0; i < mMove.lanes().size(); i++) {
            if (mMove.lanes().get(i).isEmpty()) {
                indexList.add(i);
            }
        }


        for (int i = 0; i < indexList.size(); i++) {
            mMove.lanes().remove(indexList.get(i));
        }

    }

    private Space getSpace() {
        Space space = new Space(this);

        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));
        return space;
    }

    private void startMove() {
        mMove = mGame.getNextMove();

        String playerName = mGame.getPlayers().get(mGame.getActualPlayer()).getName();

        tvPlayer.setText(playerName);

        setUpTileSet();
        setUpLanes();
    }


    private void makeMove() {

        Log.d("DEBUG", "try to make move (" + mMove + ") -> \n\n");

        removeEmptyLanes();

        int result = mGame.makeMove(mMove);

        switch(result){
            case Game.MOVE_RESULT_INVALID:

                Toast.makeText(this, "Zug ist nicht gültig!", Toast.LENGTH_SHORT).show();

                startMove();

                break;
            case Game.MOVE_RESULT_VALID:

                Toast.makeText(this, "Zug ist gültig!", Toast.LENGTH_SHORT).show();

                startMove();

                break;
            case Game.MOVE_RESULT_WIN:
                Toast.makeText(this, "Du hast gewonnen", Toast.LENGTH_SHORT).show();
                break;
        }

        Log.d("DEBUG", "move-result: " + result + " -> \n\n");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dump:

                String str = mDataSource.dumpAllTables();
                Log.d("DEBUG", "DUMP:\n\n" + str);
                break;

            case R.id.btn_gamedata:
                StringBuilder sb = new StringBuilder("GAMEDATA:\n\n");
                sb.append(mGame.toString(true));
                sb.append("\n\n");
                sb.append(mMove.toString());
                sb.append("\n\n");

                Log.d("DEBUG", sb.toString());

                break;

            case R.id.fab_playground_button:

                makeMove();
                break;
        }

    }




    private AlertDialog createGameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_game_main, null);


        final TextView tvPlayers = view.findViewById(R.id.dlg_game_tv_players);

        tvPlayers.setText("");
        int i = 1;
        for (Player player : mGame.getPlayers()) {
            tvPlayers.append(i++ + ":\t" + player.getName() + ",\n");
        }


        String positiveText = "";

        if (mGame.state() == Game.STATE_NOT_STARTED) {
            positiveText = "Spiel starten";
        } else if (mGame.state() == Game.STATE_RUNNING) {
            positiveText = "weiterspielen";
        }

        builder.setView(view).
                setTitle(mGame.getTitle())
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mGame.state() == Game.STATE_NOT_STARTED) {
                            mGame.startGame();

                        }

                        startMove();

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
