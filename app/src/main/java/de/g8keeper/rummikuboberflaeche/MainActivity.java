package de.g8keeper.rummikuboberflaeche;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Tile;
import de.g8keeper.rummikub.TileSet;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView sv = findViewById(R.id.sv_scroll_view);

        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d(TAG, "onScrollChange: x: " + scrollX + " y: " + scrollY + " oldx: " + oldScrollX + " oldy: " + oldScrollY);
            }
        });


        initFloatingActionButton();

    }

    private void initFloatingActionButton() {

        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener((view) -> {

            LinearLayout llhLane = findViewById(R.id.llh_lane);
//
            ScrollView sv = findViewById(R.id.sv_scroll_view);

            TileSetFragment tileSet = TileSetFragment.newInstance(new TileSet());
            LinearLayout llScroll = findViewById(R.id.ll_scroll);

            llScroll.setOnClickListener(v-> Toast.makeText(this, "llScroll geklickt", Toast.LENGTH_SHORT).show());



            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(llScroll.getId(), tileSet, "test");
            transaction.commit();


            TileFragment tf1 = TileFragment.newInstance(new Tile(Color.RED,5));
            TileFragment tf2 = TileFragment.newInstance(new Tile(Color.YELLOW,11));
            TileFragment tf3 = TileFragment.newInstance(new Tile(Color.BLACK,2));
            TileFragment tf4 = TileFragment.newInstance(new Tile(Color.BLUE,13));
            TileFragment tf5 = TileFragment.newInstance(new Tile(true));

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


            fragmentTransaction.add(tileSet.getId(),tf1 , tf1.toString());
            fragmentTransaction.add(tileSet.getId(),tf2 , tf2.toString());
            fragmentTransaction.add(tileSet.getId(),tf3 , tf3.toString());
            fragmentTransaction.add(tileSet.getId(),tf4 , tf4.toString());
            fragmentTransaction.add(tileSet.getId(),tf5 , tf5.toString());

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();




        });

    }
}
