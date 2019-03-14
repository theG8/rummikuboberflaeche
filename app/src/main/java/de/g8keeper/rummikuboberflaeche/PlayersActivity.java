package de.g8keeper.rummikuboberflaeche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import de.g8keeper.rummikub.database.RummikubDataSource;

public class PlayersActivity extends AppCompatActivity {
    private static final String TAG = PlayersActivity.class.getSimpleName();

    RummikubDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        dataSource = new RummikubDataSource(this);

    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "Datenquelle wird geöffnet");
        dataSource.open();


    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "Datenquelle wird geschlossen");
        dataSource.close();

    }

}
