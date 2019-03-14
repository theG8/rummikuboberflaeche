package de.g8keeper.rummikuboberflaeche;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.database.DataSource;

public class PlayersActivity extends AppCompatActivity {
    private static final String TAG = PlayersActivity.class.getSimpleName();

    DataSource dataSource;

    PlayerAdapter adapter;

    List<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        dataSource = new DataSource(this);


        Button btnAdd = findViewById(R.id.fab_add);


        ListView lvPlayers = findViewById(R.id.lv_players);
        View vHeader = getLayoutInflater().inflate(R.layout.listview_players_header, null);

        adapter = new PlayerAdapter(this,R.layout.listview_players_listitem, players);

        lvPlayers.addHeaderView(vHeader);
        lvPlayers.setAdapter(adapter);









    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "Datenquelle wird geöffnet");
        dataSource.open();

        adapter.addAll(dataSource.getAllPlayers());

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "Datenquelle wird geschlossen");
        dataSource.close();

    }

    public void onButtonClick(View view) {

        switch (view.getId()){
            case R.id.fab_add:

                Player player = dataSource.createPlayer("Seb");

                Log.d(TAG, "onButtonClick: player created: " + player);

                players.add(player);

                adapter.notifyDataSetChanged();
                break;
        }
    }
}
