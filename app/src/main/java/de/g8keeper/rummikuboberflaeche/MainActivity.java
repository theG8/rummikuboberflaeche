package de.g8keeper.rummikuboberflaeche;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TileSetFragment fragTileSet_1;
    private TileSetFragment fragTileSet_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFloatingActionButton();

        fragTileSet_1 = TileSetFragment.newInstance(new TileSet());
        fragTileSet_2 = TileSetFragment.newInstance(new TileSet());

        getSupportFragmentManager().beginTransaction().
                add(R.id.ll_playground, fragTileSet_1,"fragTileSet_1").
                add(R.id.ll_playground, fragTileSet_2,"fragTileSet_2").
                commit();



    }

    private void initFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener((view) -> {




            TileView tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 12),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLUE, 2),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLACK, 7),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);


            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.YELLOW, 3),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 4),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);
            tileView = TileView.newInstance(
                    this,
                    new Tile(true),
                    120
            );

            fragTileSet_1.getLayout().addView(tileView);

            //*******************************************

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 12),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLUE, 2),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLACK, 7),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);


            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.YELLOW, 3),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 4),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);
            tileView = TileView.newInstance(
                    this,
                    new Tile(true),
                    120
            );

            fragTileSet_2.getLayout().addView(tileView);


        });

    }
}
