package de.g8keeper.rummikub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "rummikub_db";

    public static final int DB_VERSION = 3;


    public static final String TBL_PLAYER = "tblPlayer";
    public static final String PLAYER_ID = "_id";
    public static final String PLAYER_NAME = "name";


    public static final String TBL_GAME = "tblGame";
    public static final String GAME_ID = "_id"; //INTEGER
    public static final String GAME_TITLE = "titel"; //TEXT
    public static final String GAME_START = "start"; //DATETIME
    public static final String GAME_END = "end"; //DATETIME



    public static final String TBL_PLAYERS = "tblPlayers";
    public static final String PLAYERS_GAME_ID = "id_game"; //INTEGER
    public static final String PLAYERS_PLAYER_ID = "id_player";
    public static final String PLAYERS_TOKEN = "token";


    public static final String TBL_LANES = "tblLanes";

    public static final String LANES_GAME_ID = "id_game";
    public static final String LANES_POSITION = "pos";
    public static final String LANES_TILES = "tiles";


    public static final String TBL_TILESETS = "tblTileSets";

    public static final String TILESETS_GAME_ID = "id_game";
    public static final String TILESETS_PLAYER_ID = "id_player";
    public static final String TILESETS_TILES = "tiles";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DbHelper hat die Datenbank angelegt: " + getDatabaseName());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        // tblPlayer:
        String tblPlayer = mkTBL(TBL_PLAYER,
                mkCOL(PLAYER_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"),
                mkCOL(PLAYER_NAME, "TEXT NOT NULL")
        );


        // tblGame:
        String tblGame = mkTBL(TBL_GAME,
                mkCOL(GAME_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"),
                mkCOL(GAME_TITLE, "TEXT NOT NULL"),
                mkCOL(GAME_START, "INTEGER DEFAULT 0"),
                mkCOL(GAME_END, "INTEGER DEFAULT 0")
        );

        // tblGamePlayers:
        String tblPlayers = mkTBL(TBL_PLAYERS,
                mkCOL(PLAYERS_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(PLAYERS_PLAYER_ID, "INTEGER NOT NULL"),
                mkCOL(PLAYERS_TOKEN, "BOOLEAN NOT NULL DEFAULT 0"),
                mkFK(PLAYERS_GAME_ID, TBL_GAME, GAME_ID),
                mkFK(PLAYERS_PLAYER_ID, TBL_PLAYER, PLAYER_ID)
        );

        // tblLanes:
        String tblLanes = mkTBL(TBL_LANES,

                mkCOL(LANES_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(LANES_POSITION, "INTEGER NOT NULL"),
                mkCOL(LANES_TILES, "BLOB"),
                mkFK(LANES_GAME_ID, TBL_GAME, GAME_ID),
                mkPK(LANES_GAME_ID + ", " + LANES_POSITION)
        );

        // tblTileSets
        String tblTileSets = mkTBL(TBL_TILESETS,

                mkCOL(TILESETS_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(TILESETS_PLAYER_ID, "INTEGER NOT NULL"),
                mkCOL(TILESETS_TILES, "BLOB"),
                mkFK(TILESETS_GAME_ID, TBL_GAME, GAME_ID),
                mkFK(TILESETS_PLAYER_ID, TBL_PLAYER, PLAYER_ID),
                mkPK(TILESETS_GAME_ID + ", " + TILESETS_PLAYER_ID)
        );




        try {

            Log.d(TAG, "onCreate: " + tblPlayer);
            db.execSQL(tblPlayer);
            Log.d(TAG, "onCreate: " + tblGame);
            db.execSQL(tblGame);
            Log.d(TAG, "onCreate: " + tblPlayers);
            db.execSQL(tblPlayers);
            Log.d(TAG, "onCreate: " + tblTileSets);
            db.execSQL(tblTileSets);
            Log.d(TAG, "onCreate: " + tblLanes);
            db.execSQL(tblLanes);


        } catch (RuntimeException e) {
            Log.e(TAG, "Fehler beim Anlegen der Datenbank: ", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();


        String tblLanes = dpTBL(TBL_LANES);
        String tblTileSets = dpTBL(TBL_TILESETS);
        String tblPlayers = dpTBL(TBL_PLAYERS);
        String tblGame = dpTBL(TBL_GAME);
        String tblPlayer = dpTBL(TBL_PLAYER);


        try {

            Log.d(TAG, "onUpgrade: " + tblLanes);
            db.execSQL(tblLanes);
            Log.d(TAG, "onUpgrade: " + tblTileSets);
            db.execSQL(tblTileSets);
            Log.d(TAG, "onUpgrade: " + tblPlayers);
            db.execSQL(tblPlayers);
            Log.d(TAG, "onUpgrade: " + tblGame);
            db.execSQL(tblGame);
            Log.d(TAG, "onUpgrade: " + tblPlayer);
            db.execSQL(tblPlayer);



            onCreate(db);

            Log.d(TAG, "Update Erfolgreich");
        } catch (RuntimeException e) {
            Log.e(TAG, "Fehler beim Update der Datenbank: ", e);
        }

    }




/*
    little helpers....
 */

    private static String mkCOL(String column, String config) {
        return column + " " + config;
    }

    private static String mkPK(String column) {
        return "PRIMARY KEY (" + column + ")";
    }

    private static String mkFK(String column, String fTable, String fColumn) {
        return "FOREIGN KEY (" + column + ") REFERENCES " + fTable + "(" + fColumn + ")";
    }

    private static String mkTBL(String table, String... config) {
        StringBuilder stmt = new StringBuilder();

        if (config.length == 0) {
            throw new IllegalArgumentException("config is empty");

        }

        stmt.append("CREATE TABLE ");
        stmt.append(table);
        stmt.append(" (");

        for (int i = 0; i < config.length; i++) {
            stmt.append(config[i]);

            if (i != config.length - 1) {
                stmt.append(", ");
            }
        }


        stmt.append(");\n");

        return stmt.toString();

    }

    private static String dpTBL(String table) {
        return "DROP TABLE IF EXISTS " + table + ";\n";
    }

}
