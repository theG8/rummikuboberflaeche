package de.g8keeper.rummikub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Color;
import de.g8keeper.rummikub.Game;
import de.g8keeper.rummikub.Lane;
import de.g8keeper.rummikub.Player;
import de.g8keeper.rummikub.Tile;
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
            DBHelper.GAME_END,
            DBHelper.GAME_ACTPLAYER
    };

    private String[] columnsGamePlayers = {
            DBHelper.PLAYERS_GAME_ID,
            DBHelper.PLAYERS_PLAYER_ID,
//            DBHelper.PLAYERS_TOKEN
    };

    private String[] columnsLanes = {

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

//        Log.d(TAG, "DataSource erzeugt jetzt den dbHelper");
        dbHelper = new DBHelper(context);

    }

    public void open() {

//        Log.d(TAG, "Eine Referenz auf die Datenbank wird angefragt.");

        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
//            Log.d(TAG, "open: Referenz erhalten. Pfad zur DBHelper -> " + db.getPath());
        } else {
//            Log.d(TAG, "open: DB ist bereits geöffnet");
        }
    }

    public void close() {

        if (db.isOpen()) {
            dbHelper.close();
//            Log.d(TAG, "close: Datenbank mit DBHelper geschlossen");
        } else {
//            Log.d(TAG, "close: Datenbank ist bereits geschlossen");
        }

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

        open();
        long insertId = db.insert(DBHelper.TBL_PLAYER, null, values);
        close();

        Player player = getPlayer(insertId);

        Log.d(TAG, "createPlayer: " + player + " wurde erstellt");
        return player;

    }

    public Player getPlayer(long id) {

        open();
        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
                columnsPlayer,
                DBHelper.PLAYER_ID + "=" + id,
                null, null, null, null);


        cursor.moveToFirst();

        Player player = cursorToPlayer(cursor);

        cursor.close();
        close();

        Log.d(TAG, "getPlayer: " + player + " wurde geladen");
        return player;
    }

    public void updatePlayer(Player player) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.PLAYER_NAME, player.getName());

        open();

        db.update(DBHelper.TBL_PLAYER, values,
                DBHelper.PLAYER_ID + "=" + player.getId(), null);

        close();

        Log.d(TAG, "updatePlayer: " + player + " wurde geupdated");

    }


    public void deletePlayer(Player player) {

        long id = player.getId();

        open();

        db.delete(DBHelper.TBL_PLAYER, DBHelper.PLAYER_ID + "=" + id, null);

        close();
        Log.d(TAG, "deletePlayer: " + player + " wurde gelöscht");

    }

    public List<Player> getAllPlayers() {

        List<Player> list = new ArrayList<>();

        open();
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
        close();
        Log.d(TAG, "getAllPlayers: " + list + " wurde geladen");

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
        int idActPlayer = cursor.getColumnIndex(DBHelper.GAME_ACTPLAYER);

        long index = cursor.getLong(idIndex);
        String title = cursor.getString(idTitle);
        long start = cursor.getLong(idStart);
        long end = cursor.getLong(idEnd);
        int posActPlayer = cursor.getInt(idActPlayer);

        Game game = new Game(index, title, start, end);

        game.setActualPlayer(posActPlayer);
        game.setDataSource(this);

        return game;

    }

    public Game createGame(String title, long start, long end) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.GAME_TITLE, title);
        values.put(DBHelper.GAME_START, start);
        values.put(DBHelper.GAME_END, end);
        values.put(DBHelper.GAME_ACTPLAYER, 0);

        open();

        long gameId = db.insert(DBHelper.TBL_GAME, null, values);

        Cursor cursor = db.query(DBHelper.TBL_GAME,
                columnsGame,
                DBHelper.GAME_ID + "=" + gameId,
                null, null, null, null);


        cursor.moveToFirst();

        Game game = cursorToGame(cursor);

        cursor.close();
        close();
        Log.d(TAG, "createGame: " + game + " wurde erstellt");

        return game;
    }

    public Game getGame(long id) {

        open();

        Cursor cursor = db.query(DBHelper.TBL_GAME,
                columnsGame,
                DBHelper.GAME_ID + "=" + id,
                null, null, null, null);


        cursor.moveToFirst();

        Game game = cursorToGame(cursor);


        game.setPlayers(getGamePlayers(game));


        game.setLanes(getGameLanes(game));

        game.buildPool();


        cursor.close();
        close();

        Log.d(TAG, "getGame: " + game + " wurde geladen");
        return game;
    }


    public void updateGame(Game game) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.GAME_TITLE, game.getTitle());
        values.put(DBHelper.GAME_START, game.getStartTime());
        values.put(DBHelper.GAME_END, game.getEndTime());
        values.put(DBHelper.GAME_ACTPLAYER, game.getActualPlayer());

        open();

        db.update(DBHelper.TBL_GAME, values,
                DBHelper.GAME_ID + "=" + game.getId(), null);

        close();


        deleteGameLanes(game);
        deleteGameTileSets(game);

        for (Player player : game.getPlayers()) {
            addTileSetToPlayer(game, player);
        }

        int pos = 0;
        for (Lane lane : game.getLanes()) {
            addLaneToGame(game, lane, pos++);
        }


        Log.d(TAG, "updateGame: " + game + " wurde geupdated");
    }


    public void deleteGameLanes(Game game) {
        open();

        db.delete(DBHelper.TBL_LANES, DBHelper.LANES_GAME_ID + " = " + game.getId(), null);

        close();
        Log.d(TAG, "deleteGameLanes: Lanes von " + game + " wurden gelöscht");
    }

    public void deleteGamePlayers(Game game) {
        open();

        db.delete(DBHelper.TBL_PLAYERS, DBHelper.PLAYERS_GAME_ID + " = " + game.getId(), null);

        close();
        Log.d(TAG, "deleteGamePlayers: Players von " + game + " wurden gelöscht");
    }

    public void deleteGameTileSets(Game game) {
        open();

        db.delete(DBHelper.TBL_TILESETS, DBHelper.TILESETS_GAME_ID + " = " + game.getId(), null);

        close();
        Log.d(TAG, "deleteGameTileSets: TileSets von " + game + " wurden gelöscht");
    }


    public void deleteGame(Game game) {

        long id = game.getId();



        deleteGameLanes(game);
        deleteGameTileSets(game);
        deleteGamePlayers(game);
        open();
        db.delete(DBHelper.TBL_GAME, DBHelper.GAME_ID + "=" + id, null);
        close();

        Log.d(TAG, "deleteGame: " + game + " wurde gelöscht");

    }

    public List<Game> getAllGames() {

        List<Game> list = new ArrayList<>();

        open();

        Cursor cursor = db.query(DBHelper.TBL_GAME, columnsGame,
                null, null, null, null, null);


        int idID = cursor.getColumnIndex(DBHelper.GAME_ID);
        cursor.moveToFirst();

        Game game;
        long gameID;


        while (!cursor.isAfterLast()) {
            gameID = cursor.getLong(idID);

            game = getGame(gameID);
            list.add(game);
            cursor.moveToNext();
        }

        cursor.close();
        close();

        Log.d(TAG, "getAllGames: " + list + " wurden geladen");

        return list;

    }


    /*
     **************************************************************************************
     */


//    public int setPosActPlayer(Game game){
//        int idToken, count, token;
//
//        open();
//
//        Cursor cursor = db.query(DBHelper.TBL_PLAYERS, columnsGamePlayers,
//                DBHelper.PLAYERS_GAME_ID + " = " + game.getId(), null, null, null, null);
//
//
//        idToken = cursor.getColumnIndex(DBHelper.PLAYERS_TOKEN);
//        count = 0;
//
//        cursor.moveToFirst();
//
//        while(!cursor.isAfterLast()){
//            if(count == game.getActualPlayer()) {
//
//            }
//            cursor.moveToNext();
//        }
//
//
//        cursor.close();
//        close();
//
//        return 0;
//    }

    public List<Player> getGamePlayers(Game game) {

        List<Player> players = new ArrayList<>();
        int idPlayer, idToken, token;

        Player player;
        long id;

//        int count;

        open();

        Cursor cursor = db.query(DBHelper.TBL_PLAYERS, columnsGamePlayers,
                DBHelper.PLAYERS_GAME_ID + " = " + game.getId(), null, null, null, null);


        idPlayer = cursor.getColumnIndex(DBHelper.PLAYERS_PLAYER_ID);
//        idToken = cursor.getColumnIndex(DBHelper.PLAYERS_TOKEN);

        cursor.moveToFirst();

//        count = 0;

        while (!cursor.isAfterLast()) {
            id = cursor.getLong(idPlayer);

            player = getPlayer(id);

            loadGamePlayerTileSet(game, player);

            players.add(player);

//            token = cursor.getInt(idToken);
//            if(token != 0){
//                game.setActualPlayer(count);
//            }

//            count++;
            cursor.moveToNext();
        }

        cursor.close();
        close();

        Log.d(TAG, "getGamePlayers: " + players + " wurden geladen");

        return players;

    }

    public List<Lane> getGameLanes(Game game) {
        List<Lane> lanes = new ArrayList<>();

        Lane lane;

        open();

        Cursor cursor = db.query(DBHelper.TBL_LANES, new String[]{DBHelper.LANES_TILES},
                DBHelper.LANES_GAME_ID + " = " + game.getId(), null, null, null, DBHelper.LANES_POSITION + " ASC");


        int idTiles = cursor.getColumnIndex(DBHelper.LANES_TILES);

        cursor.moveToFirst();
        int id = cursor.getColumnIndex(DBHelper.LANES_TILES);
        while (!cursor.isAfterLast()) {
            lane = new Lane(cursor.getBlob(id));

            lanes.add(lane);

            cursor.moveToNext();
        }

        cursor.close();
        close();

        Log.d(TAG, "getGameLanes: " + lanes + " wurden geladen");
        return lanes;

    }

//    public int getGameActualPlayer(Game game) {
//
//        int playerID = 0;
//
//        open();
//
//        Cursor cursor = db.query(DBHelper.TBL_PLAYERS, new String[]{DBHelper.PLAYERS_PLAYER_ID},
//                DBHelper.PLAYERS_GAME_ID + " = " + game.getId() + " AND " +
//                        DBHelper.PLAYERS_TOKEN + " = 1",
//                null, null, null, null);
//
//
//        cursor.moveToFirst();
//        int idID = cursor.getColumnIndex(DBHelper.PLAYERS_PLAYER_ID);
//
//        if (!cursor.isAfterLast()) {
//            playerID = cursor.getInt(0);
//        }
//
//        cursor.close();
//        close();
//
//        Log.d(TAG, "getGameActualPlayer: " + playerID + " wurde geladen");
//
//        return playerID;
//    }

    private void loadGamePlayerTileSet(Game game, Player player) {

        open();

        Cursor cursor = db.query(DBHelper.TBL_TILESETS, new String[]{DBHelper.TILESETS_TILES},
                DBHelper.TILESETS_GAME_ID + " = " + game.getId() + " AND " +
                        DBHelper.TILESETS_PLAYER_ID + " + " + player.getId(),
                null, null, null, null);


        cursor.moveToFirst();

        TileSet tileSet;
        Log.d(TAG, "loadGamePlayerTileSet: cursor.count: " + cursor.getCount());

        int id = cursor.getColumnIndex(DBHelper.TILESETS_TILES);

        if (!cursor.isAfterLast()) {
            tileSet = new TileSet(cursor.getBlob(id));
            player.setTileSet(tileSet);
            Log.d(TAG, "loadGamePlayerTileSet: " + tileSet + " wurde an " + player + " angehängt");
        } else {
            Log.d(TAG, "loadGamePlayerTileSet: kein TileSet gefunden");
        }


        cursor.close();
        close();

    }


    public void addTileSetToPlayer(Game game, Player player) {

        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.TILESETS_GAME_ID, game.getId());
        values.put(DBHelper.TILESETS_PLAYER_ID, player.getId());
        values.put(DBHelper.TILESETS_TILES, player.getTileSet().toBytearray());

        open();

        db.insert(DBHelper.TBL_TILESETS, null, values);

        Log.d(TAG, "addTileSetToPlayer: " + player.getName() + ": " + player.getTileSet() + " von " + game);
        close();

    }


    public void addLaneToGame(Game game, Lane lane, int position) {

        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.LANES_GAME_ID, game.getId());
        values.put(DBHelper.LANES_POSITION, position);
        values.put(DBHelper.LANES_TILES, lane.toBytearray());


        open();

        db.insert(DBHelper.TBL_LANES, null, values);

        Log.d(TAG, "addLaneToGame: " + lane + "(pos: " + position + ") -> " + game);
        close();
    }


    public void addPlayerToGame(Game game, Player player) {
        ContentValues values;

        values = new ContentValues();
        values.put(DBHelper.PLAYERS_GAME_ID, game.getId());
        values.put(DBHelper.PLAYERS_PLAYER_ID, player.getId());

        open();

        db.insert(DBHelper.TBL_PLAYERS, null, values);
        Log.d(TAG, "addPlayerToGame: " + player + " -> " + game);

        close();

        //TODO ?!
        addTileSetToPlayer(game,player);


    }

    /***********************************************************************************************
     *
     */


    public String dumpAllTables(){
        StringBuilder str = new StringBuilder();

        str.append(dumpTable(DBHelper.TBL_PLAYER));
        str.append(dumpTable(DBHelper.TBL_GAME));
        str.append(dumpTable(DBHelper.TBL_PLAYERS));
        str.append(dumpTable(DBHelper.TBL_TILESETS));
        str.append(dumpTable(DBHelper.TBL_LANES));

        return str.toString();

    }



    public String dumpTable(String table){
        StringBuilder str = new StringBuilder();

        String[] columns= new String[0];
        switch(table){
            case DBHelper.TBL_GAME:
                columns = columnsGame;
                break;
            case DBHelper.TBL_PLAYER:
                columns = columnsPlayer;
                break;
            case DBHelper.TBL_PLAYERS:
                columns = columnsGamePlayers;
                break;
            case DBHelper.TBL_LANES:
                columns = columnsLanes;
                break;
            case DBHelper.TBL_TILESETS:
                columns = columnsTileSets;
                break;
        }


        open();

        Cursor cursor = db.query(table,
                columns,"",
                null, null, null, null);


        str.append("Dump of " + table + ":\n");


        int rows = cursor.getCount();
        int cols = cursor.getColumnCount();

        cursor.moveToFirst();

        if(!cursor.isAfterLast()) {
            for (int r = 0; r < rows; r++) {
                cursor.moveToPosition(r);
                str.append(r + ":\t");
                for (int c = 0; c < cols; c++) { // <- c++.... hehe B)
                    if(cursor.getType(c) != Cursor.FIELD_TYPE_BLOB) {
                        str.append(cursor.getString(c) + "\t");
                    } else {
                        TileSet ts = new TileSet(cursor.getBlob(c));
                        str.append(ts.toString() + "\t");
                    }
                }
                str.append("\n");

            }
        } else {
            str.append(" table is empty!\n");
        }

        cursor.close();
        close();

        str.append("************************************************\n\n");
        return str.toString();
    }

    public void createTestspiel(){

        Log.d("DEBUG", dumpAllTables());

        Game game = getGame(1);
        game.setDataSource(this);

        Player p1 = createPlayer("Seb");
        Player p2 = createPlayer("Billy");


        p1.getTileSet().addTile(new Tile(Color.RED,3));
        p1.getTileSet().addTile(new Tile(Color.RED,4));
        p1.getTileSet().addTile(new Tile(Color.RED,5));
        p1.getTileSet().addTile(new Tile(Color.RED,6));

        p1.getTileSet().addTile(new Tile(Color.BLUE,9));
        p1.getTileSet().addTile(new Tile(Color.BLUE,10));
        p1.getTileSet().addTile(new Tile(Color.BLUE,11));

        p1.getTileSet().addTile(new Tile(Color.YELLOW,6));
        p1.getTileSet().addTile(new Tile(Color.YELLOW,7));
        p1.getTileSet().addTile(new Tile(Color.YELLOW,8));

        p1.getTileSet().addTile(new Tile(Color.BLACK,1));
        p1.getTileSet().addTile(new Tile(Color.BLACK,2));
        p1.getTileSet().addTile(new Tile(Color.BLACK,12));
        p1.getTileSet().addTile(new Tile(Color.BLACK,13));

        p1.getTileSet().addTile(new Tile(true));

        game.addPlayer(p1);

        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());
        p2.getTileSet().addTile(new Tile());

        game.addPlayer(p2);

        Lane lane = new Lane();

        lane.addTile(new Tile(Color.YELLOW,3));
        lane.addTile(new Tile(Color.YELLOW,4));
        lane.addTile(new Tile(Color.YELLOW,5));

        game.getLanes().add(lane);

        game.setmStartTime();
        updateGame(game);


        Log.d("DEBUG", dumpAllTables());

    }

}