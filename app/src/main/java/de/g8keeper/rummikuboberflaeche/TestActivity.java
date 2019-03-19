package de.g8keeper.rummikuboberflaeche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.Move;
import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.database.DataSource;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = TestActivity.class.getSimpleName();

    DataSource dataSource;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dataSource = new DataSource(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        dataSource.close();
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tAddGame:

                game = dataSource.createGame("Testspiel", -1, -1);

                Log.d(TAG, "created game: " + game.toString());

                break;

            case R.id.tGetGame:

                game = dataSource.getGame(1);
                game.setDataSource(dataSource);
//                game.loadGameData();

                Log.d(TAG, "got game: " + game.toString());

                break;

            case R.id.tToFullString:

                Log.d(TAG, game.toString(true));

                break;

            case R.id.tAddPlayer:

                Player player = dataSource.getPlayer(1);

                dataSource.addPlayerToGame(game, player);

                Log.d(TAG, "added player " + player + " to " + game.toString());
                break;

            case R.id.tAddAllPlayers:

                List<Player> players = dataSource.getAllPlayers();

                players.get(0).getTileSet().addTile(new Tile(Color.RED, 3));
                players.get(0).getTileSet().addTile(new Tile(Color.RED, 12));

                for (Player p : players) {
                    game.addPlayer(p);
                }

                Log.d(TAG, "added players " + players + " to " + game.toString());

                break;

            case R.id.tGetTurn:

                Move move = game.getNextMove();
                Log.d(TAG, "got move " + move + " from " + game + "...");

                move.tileSet().removeTile(0);
                Log.d(TAG, "removed tile1 form move -> " + move);


                move = game.getNextMove();
                Log.d(TAG, "got new move " + move + " from " + game);


                break;

            case R.id.btn_testspiel:

//                dataSource.createTestspiel();

                Log.d("DEBUG", dataSource.dumpAllTables());


                break;

//            case R.id.tAddLanes:
//
//                Lane lane;
//
//                lane = new Lane();
//
//                lane.addTile(new Tile(Color.BLACK, 4));
//                lane.addTile(new Tile(Color.BLACK, 5));
//                lane.addTile(new Tile(Color.BLACK, 6));
//                lane.addTile(new Tile(Color.BLACK, 7));
//
//                game.addLane(lane);
//
//                lane = new Lane();
//
//                lane.addTile(new Tile(Color.BLUE, 9));
//                lane.addTile(new Tile(Color.BLUE, 10));
//                lane.addTile(new Tile(Color.BLUE, 11));
//                lane.addTile(new Tile(Color.BLUE, 12));
//
//                game.addLane(lane);
//
//                break;
        }


    }
}
