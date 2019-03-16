package de.g8keeper.rummikub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.TileSet;

public class DataSource {

    private static final String TAG = DataSource.class.getSimpleName();

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private String[] columnsPlayer = {
            DBHelper.PLAYER_ID,
            DBHelper.PLAYER_NAME
    };

    private String[] columnsGame = {
            DBHelper.GAME_ID,
            DBHelper.GAME_TITLE,
            DBHelper.GAME_START,
            DBHelper.GAME_END
    };

    private String[] columnsGamePlayers = {
            DBHelper.PLAYERS_GAME_ID,
            DBHelper.PLAYERS_PLAYER_ID,
            DBHelper.PLAYERS_TOKEN
    };

    private String[] columsLanes = {
            DBHelper.LANES_GAME_ID,
            DBHelper.LANES_POSITION,
            DBHelper.LANES_TILES
    };

    private String[] columnsTileSets = {
            DBHelper.TILESETS_GAME_ID,
            DBHelper.TILESETS_PLAYER_ID,
            DBHelper.TILESETS_TILES,
    };


    public DataSource(Context context) {

        Log.d(TAG, "DataSource erzeugt jetzt den dbHelper");
        dbHelper = new DBHelper(context);

    }

    public void open() {

        Log.d(TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        db = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: Referenz erhalten. Pfad zur DBHelper -> " + db.getPath());

    }

    public void close() {

        dbHelper.close();
        Log.d(TAG, "Datenbank mit DBHelper geschlossen");

    }


    /*
     **************************************************************************************
     */


    private Player cursorToPlayer(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.PLAYER_ID);
        int idName = cursor.getColumnIndex(DBHelper.PLAYER_NAME);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);

        Player player = new Player(id, name);

        return player;

    }

    public Player createPlayer(String name) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.PLAYER_NAME, name);

        long insertId = db.insert(DBHelper.TBL_PLAYER, null, values);

        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
                columnsPlayer,
                DBHelper.PLAYER_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();

        Player player = cursorToPlayer(cursor);

        cursor.close();

        return player;
    }

    public Player updatePlayer(long id, String name) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.PLAYER_NAME, name);


        db.update(DBHelper.TBL_PLAYER, values,
                DBHelper.PLAYER_ID + "=" + id, null);


        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
                columnsPlayer,
                DBHelper.PLAYER_ID + "=" + id,
                null, null, null, null);

        cursor.moveToFirst();
        Player player = cursorToPlayer(cursor);
        cursor.close();

        return player;
    }


    public void deletePlayer(Player player) {

        long id = player.getId();

        db.delete(DBHelper.TBL_PLAYER, DBHelper.PLAYER_ID + "=" + id, null);

        Log.d(TAG, "Player gelöscht! ID: " + id + " Inhalt: " + player.toString());

    }

    public List<Player> getAllPlayers() {

        List<Player> list = new ArrayList<>();

        Cursor cursor = db.query(DBHelper.TBL_PLAYER, columnsPlayer,
                null, null, null, null, null);

        cursor.moveToFirst();

        Player player;

        while (!cursor.isAfterLast()) {
            player = cursorToPlayer(cursor);
            list.add(player);
            cursor.moveToNext();
        }

        cursor.close();

        return list;

    }




    /*
     **************************************************************************************
     */


    private Game cursorToGame(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.GAME_ID);
        int idTitle = cursor.getColumnIndex(DBHelper.GAME_TITLE);
        int idStart = cursor.getColumnIndex(DBHelper.GAME_START);
        int idEnd = cursor.getColumnIndex(DBHelper.GAME_END);


        long index = cursor.getLong(idIndex);
        String title = cursor.getString(idTitle);
        long start = cursor.getLong(idStart);
        long end = cursor.getLong(idStart);


        Game game = new Game(index, title);

        return game;

    }

    public Game createGame(String title, long start, long end) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.GAME_TITLE, title);
        values.put(DBHelper.GAME_START, start);
        values.put(DBHelper.GAME_END, end);

        long gameId = db.insert(DBHelper.TBL_GAME, null, values);

        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
                columnsGame,
                DBHelper.GAME_ID + "=" + gameId,
                null, null, null, null);

        cursor.moveToFirst();

        Game game = cursorToGame(cursor);

        cursor.close();

        return game;
    }




    public void addTileSetToPlayer(Game game, Player player){

        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.TILESETS_GAME_ID, game.getId());
        values.put(DBHelper.TILESETS_PLAYER_ID, player.getId());
        values.put(DBHelper.TILESETS_TILES, player.getTileSet().toBytearray());

        db.insert(DBHelper.TBL_TILESETS, null, values);
    }


    public void updateTileSet(Game game, Player player) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.TILESETS_TILES, player.getTileSet().toBytearray());

        db.update(DBHelper.TBL_TILESETS, values,
                DBHelper.TILESETS_GAME_ID + "=" + game.getId(), null);

    }


    public void addLaneToGame(Game game, int position) {

        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.LANES_GAME_ID, game.getId());
        values.put(DBHelper.LANES_POSITION, position);


        db.insert(DBHelper.TBL_LANES, null, values);
    }


    public void updateLane(Game game, int position, Lane lane) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.LANES_POSITION, position);
        values.put(DBHelper.LANES_TILES, lane.toBytearray());


        db.update(DBHelper.TBL_LANES, values,
                DBHelper.LANES_GAME_ID + "=" + game.getId(), null);

    }


    public void removeLaneFromGame(Game game, int position) {
        db.delete(DBHelper.TBL_LANES,
                DBHelper.LANES_GAME_ID + " = " + game.getId() + " AND " +
                        DBHelper.LANES_POSITION + " = " + position,
                null
        );
    }

    public void addPlayerToGame(Game game, Player player) {
        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.PLAYERS_GAME_ID, game.getId());
        values.put(DBHelper.PLAYERS_PLAYER_ID, player.getId());

        db.insert(DBHelper.TBL_PLAYERS, null, values);
    }


    public void removePlayerFromGame(Game game, Player player) {

        db.delete(DBHelper.TBL_PLAYERS,
                DBHelper.PLAYERS_GAME_ID + " = " + game.getId() + " AND " +
                        DBHelper.PLAYERS_PLAYER_ID + " = " + player.getId(),
                null
        );

    }


//    public Player createGame(String title, long start, long end) {
//
//        ContentValues values = new ContentValues();
//        values.put(DBHelper.GAME_TITLE, title);
//        values.put(DBHelper.GAME_START, start);
//        values.put(DBHelper.GAME_END, end);
//
//        long insertId = db.insert(DBHelper.TBL_GAME,null, values);
//
//        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
//                columnsGame,
//                DBHelper.PLAYER_ID + "=" + insertId,
//                null,null,null,null);
//
//        cursor.moveToFirst();
//
//        Player player = cursorToPlayer(cursor);
//
//        cursor.close();
//
//        return player;
//    }
//
//
//    public Player updatePlayer(long id, String name) {
//
//        ContentValues values = new ContentValues();
//
//        values.put(DBHelper.PLAYER_NAME, name);
//
//
//        db.update(DBHelper.TBL_PLAYER, values,
//                DBHelper.PLAYER_ID + "=" + id, null);
//
//
//        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
//                columnsPlayer,
//                DBHelper.PLAYER_ID + "=" + id,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        Player player = cursorToPlayer(cursor);
//        cursor.close();
//
//        return player;
//    }
//
//
//
//
//    public void deletePlayer(Player player) {
//
//        long id = player.getId();
//
//        db.delete(DBHelper.TBL_PLAYER, DBHelper.PLAYER_ID + "=" + id, null);
//
//        Log.d(TAG, "Player gelöscht! ID: " + id + " Inhalt: " + player.toString());
//
//    }
//
//    public List<Player> getAllPlayers() {
//
//        List<Player> list = new ArrayList<>();
//
//        Cursor cursor = db.query(DBHelper.TBL_PLAYER, columnsPlayer,
//                null, null, null, null, null);
//
//        cursor.moveToFirst();
//
//        Player player;
//
//        while (!cursor.isAfterLast()) {
//            player = cursorToPlayer(cursor);
//            list.add(player);
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//
//        return list;
//
//    }

    /*
     **************************************************************************************
     */


}