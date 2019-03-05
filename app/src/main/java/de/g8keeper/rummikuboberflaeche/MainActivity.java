package de.g8keeper.rummikuboberflaeche;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



                initFloatingActionButton();

    }

    private void initFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener((view) -> {



            TileView tile1 = findViewById(R.id.tile_test1);
            tile1.setTile(new Tile(Color.RED,5));
            TileView tile2 = findViewById(R.id.tile_test2);
            tile2.setTile(new Tile(Color.BLUE,11));
            TileView tile3 = findViewById(R.id.tile_test3);
            tile3.setTile(new Tile(Color.YELLOW,10));
            TileView tile4 = findViewById(R.id.tile_test4);
            tile4.setTile(new Tile(Color.BLACK,3));
            TileView tile5 = findViewById(R.id.tile_test5);
            tile5.setTile(new Tile(true));



        });

    }
}
