package de.g8keeper.rummikuboberflaeche;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.database.DataSource;

public class GamesActivity extends AppCompatActivity {
    private static final String TAG = PlayersActivity.class.getSimpleName();


    DataSource mDataSource;

    GamesAdapter mAdapter;

    List<Game> games = new ArrayList<>();
    private ListView mLvGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        mDataSource = new DataSource(this);


        Button btnAdd = findViewById(R.id.fab_games_add);


        mLvGames = findViewById(R.id.lv_games);
        View vHeader = getLayoutInflater().inflate(R.layout.listview_games_header, null);

        mAdapter = new GamesAdapter(this, R.layout.listview_games_listitem, games);

        mLvGames.addHeaderView(vHeader);
        mLvGames.setAdapter(mAdapter);
        mLvGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + position + " id: " + id);
                Game game = mAdapter.getItem(position - 1);

                if (game.hasPlayers()) {

                    Intent intent = new Intent(getBaseContext(), PlaygroundActivity.class);
                    intent.putExtra("game", game.getId());
                    startActivity(intent);

                } else {



                }


            }
        });

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

    private void showAllEntries() {
        mAdapter.clear();
        mAdapter.addAll(mDataSource.getAllGames());
        mAdapter.notifyDataSetChanged();
    }


    public void onButtonClick(View view) {

        switch (view.getId()) {
            case R.id.fab_games_add:

                AlertDialog dialogAdd = createAddGameDialog();
                dialogAdd.show();
                break;
        }
    }


    private AlertDialog createAddGameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_game, null);

        final EditText etTitle = view.findViewById(R.id.dlg_games_add_name);

        builder.setView(view).
                setTitle("Spieler hinzufügen").
                setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String titleString = etTitle.getText().toString();

                        if (TextUtils.isEmpty(titleString)) {
                            Toast.makeText(GamesActivity.this, "Bitte Name eingeben", Toast.LENGTH_SHORT).show();
                        }

                        Game game = mDataSource.createGame(titleString, -1, -1);
                        games.add(game);

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


    private AlertDialog createEditGamesDialog(Game game) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_game, null);

        final EditText etTitle = view.findViewById(R.id.dlg_games_add_name);
        etTitle.setText(game.getTitle());

        builder.setView(view).
                setTitle("Name ändern").
                setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String titleString = etTitle.getText().toString();

                        if (TextUtils.isEmpty(titleString)) {
                            Toast.makeText(GamesActivity.this, "Titel darf nicht leer sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            game.setTitle(titleString);
                            mDataSource.updateGame(game);
                        }


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
        mLvGames.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        mLvGames.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int selCount = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if (checked) {
                    selCount++;
                } else {
                    selCount--;
                }


                String cabTitel = selCount + " " + getString(R.string.cab_checked_string);
                mode.setTitle(cabTitel);
                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_games_contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem item = menu.findItem(R.id.cab_games_change);
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

                SparseBooleanArray touchedGamesPosition = mLvGames.getCheckedItemPositions();

                switch (item.getItemId()) {

                    case R.id.cab_games_delete: // löschen wurde geklickt

                        for (int i = 0; i < touchedGamesPosition.size(); i++) {
                            boolean isChecked = touchedGamesPosition.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedGamesPosition.keyAt(i);
                                Game game = (Game) mLvGames.
                                        getItemAtPosition(positionInListView);

                                Log.d(TAG, "Position im ListView: " + positionInListView + " Inhalt: " + game.toString());
                                mDataSource.deleteGame(game);
                            }
                        }

                        showAllEntries();

                        // beendet mehrfachauswahl-modus
                        mode.finish();
                        break;

                    case R.id.cab_games_change:

                        for (int i = 0; i < touchedGamesPosition.size(); i++) {
                            boolean isChecked = touchedGamesPosition.valueAt(i);
                            if (isChecked) {
                                int positionInListView = touchedGamesPosition.keyAt(i);
                                Game game = (Game) mLvGames.
                                        getItemAtPosition(positionInListView);

                                AlertDialog editGameDialog = createEditGamesDialog(game);
                                editGameDialog.show();


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
