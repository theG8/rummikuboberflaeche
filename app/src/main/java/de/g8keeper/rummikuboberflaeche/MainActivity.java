package de.g8keeper.rummikuboberflaeche;

import android.content.ClipData;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TileSetFragment fragTileSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFloatingActionButton();


        fragTileSet = TileSetFragment.newInstance(new TileSet());


                getSupportFragmentManager().beginTransaction().
                add(R.id.ll_playground, fragTileSet,"Test").commit();


    }

    private void initFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener((view) -> {

//            LinearLayout llPlayground = findViewById(R.id.ll_playground);

            TileView tileView = TileView.newInstance(
                    this,
                    new Tile(Color.RED, 12),
                    120
            );

            fragTileSet.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLUE, 2),
                    120
            );

            fragTileSet.getLayout().addView(tileView);

            tileView = TileView.newInstance(
                    this,
                    new Tile(Color.BLACK, 7),
                    120
            );

            fragTileSet.getLayout().addView(tileView);



        });

    }
}
