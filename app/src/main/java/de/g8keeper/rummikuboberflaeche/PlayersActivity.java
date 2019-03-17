package de.g8keeper.rummikuboberflaeche;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.database.DataSource;

public class PlayersActivity extends AppCompatActivity {
    private static final String TAG = PlayersActivity.class.getSimpleName();




    DataSource mDataSource;

    PlayerAdapter mAdapter;

    List<Player> players = new ArrayList<>();
    private ListView mLvPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        mDataSource = new DataSource(this);


        Button btnAdd = findViewById(R.id.fab_add);


        mLvPlayers = findViewById(R.id.lv_players);
        View vHeader = getLayoutInflater().inflate(R.layout.listview_players_header, null);

        mAdapter = new PlayerAdapter(this, R.layout.listview_players_listitem, players);

        mLvPlayers.addHeaderView(vHeader);
        mLvPlayers.setAdapter(mAdapter);

        inizializeContextualActionBar();

    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "Datenquelle wird geöffnet");
        mDataSource.open();

        showAllEntries();


    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "Datenquelle wird geschlossen");
        mDataSource.close();

    }

    private void showAllEntries(){
        mAdapter.clear();
        mAdapter.addAll(mDataSource.getAllPlayers());
        mAdapter.notifyDataSetChanged();
    }


    public void onButtonClick(View view) {

        switch (view.getId()) {
            case R.id.fab_add:

                AlertDialog dialogAdd = createAddPlayerDialog();
                dialogAdd.show();
                break;
        }
    }



    private AlertDialog createAddPlayerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_player, null);

        final EditText etName = view.findViewById(R.id.dlg_add_name);

        builder.setView(view).
                setTitle("Spieler hinzufügen").
                setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameString = etName.getText().toString();

                        if(TextUtils.isEmpty(nameString)){
                            Toast.makeText(PlayersActivity.this, "Bitte Name eingeben", Toast.LENGTH_SHORT).show();
                        }

                        Player player = mDataSource.createPlayer(nameString);
                        players.add(player);

                        mAdapter.notifyDataSetChanged();

                    }
                }).
                setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }


    private AlertDialog createEditPlayerDialog(Player player) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_player, null);

        final EditText etName = view.findViewById(R.id.dlg_add_name);
        etName.setText(player.getName());

        builder.setView(view).
                setTitle("Name ändern").
                setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String nameString = etName.getText().toString();

                        if(TextUtils.isEmpty(nameString)){
                            Toast.makeText(PlayersActivity.this, "Name darf nicht leer sein", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mDataSource.updatePlayer(player);


                        showAllEntries();

                    }
                }).
                setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }


    private void inizializeContextualActionBar() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mLvPlayers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        mLvPlayers.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int selCount = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if (checked) {
                    selCount++;
                } else {
                    selCount--;
                }



//                View view = mAdapter.getView(position-1, null, null).findViewById(R.id.li_tv_name);
//
//                Log.d(TAG, "onItemCheckedStateChanged: " + ((TextView) view).getText());
//                runOnUiThread(() -> ((TextView) view).setText("CHECKED"));



//                .setBackgroundColor();




                String cabTitel = selCount + " " + getString(R.string.cab_checked_string);
                mode.setTitle(cabTitel);
                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_players_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem item = menu.findItem(R.id.cab_change);
                if (selCount == 1) {
                    item.setVisible(true);
                } else {
                    item.setVisible(false);
                }

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                boolean returnValue = true;

                SparseBooleanArray touchedPlayersPosition = mLvPlayers.getCheckedItemPositions();

                switch (item.getItemId()) {

                    case R.id.cab_delete: // löschen wurde geklickt

                        for (int i = 0; i < touchedPlayersPosition.size(); i++) {
                            boolean isChecked = touchedPlayersPosition.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedPlayersPosition.keyAt(i);
                                Player player = (Player) mLvPlayers.
                                        getItemAtPosition(positionInListView);

                                Log.d(TAG, "Position im ListView: " + positionInListView + " Inhalt: " + player.toString());
                                mDataSource.deletePlayer(player);
                            }
                        }

                        showAllEntries();

                        // beendet mehrfachauswahl-modus
                        mode.finish();
                        break;

                    case R.id.cab_change:

                        for (int i = 0; i < touchedPlayersPosition.size(); i++) {
                            boolean isChecked = touchedPlayersPosition.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedPlayersPosition.keyAt(i);
                                Player player = (Player) mLvPlayers.
                                        getItemAtPosition(positionInListView);

                                AlertDialog editPlayerDialog = createEditPlayerDialog(player);
                                editPlayerDialog.show();



                            }
                        }

                        mode.finish();
                        break;

                    default:

                        return returnValue = false;

                }


                return returnValue;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selCount = 0;

            }
        });

    }



}
