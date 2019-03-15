package de.g8keeper.rummikub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public static final String DB_NAME = "rummikub_db";

    public static final int DB_VERSION = 2;


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


    public static final String TBL_LANES = "tblLanes";
    public static final String LANES_GAME_ID = "id_game";
    public static final String LANES_POSITION = "pos";
    public static final String LANES_TILES = "mTiles";


    public static final String TBL_TILESETS = "tblTileSets";
    public static final String TILESETS_GAME_ID = "id_game";
    public static final String TILESETS_PLAYER_ID = "id_player";
    public static final String TILESETS_TILES = "mTiles";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DbHelper hat die Datenbank angelegt: " + getDatabaseName());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();


        // tblPlayer:
        sql.append(mkTBL(TBL_PLAYER,
                mkCOL(PLAYER_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"),
                mkCOL(PLAYER_NAME, "TEXT NOT NULL")
        )).append("\n");


        // tblGame:
        sql.append(mkTBL(TBL_GAME,
                mkCOL(GAME_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"),
                mkCOL(GAME_TITLE, "TEXT NOT NULL"),
                mkCOL(GAME_START, "DATETIME"),
                mkCOL(GAME_END, "DATETIME")
        )).append("\n");


        // tblGamePlayers:
        sql.append(mkTBL(TBL_PLAYERS,
                mkCOL(PLAYERS_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(PLAYERS_PLAYER_ID, "INTEGER NOT NULL"),
                mkFK(PLAYERS_GAME_ID, TBL_GAME, GAME_ID),
                mkFK(PLAYERS_PLAYER_ID, TBL_PLAYER, PLAYER_ID)
        )).append("\n");

        // tblLanes:
        sql.append(mkTBL(TBL_LANES,
                mkCOL(LANES_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(LANES_POSITION, "INTEGER NOT NULL"),
                mkCOL(LANES_TILES, "BLOB NOT NULL"),
                mkFK(LANES_GAME_ID, TBL_GAME, GAME_ID),
                mkPK(LANES_GAME_ID + ", " + LANES_POSITION)
        ));

        // tblTileSets
        sql.append(mkTBL(TBL_TILESETS,
                mkCOL(TILESETS_GAME_ID, "INTEGER NOT NULL"),
                mkCOL(TILESETS_PLAYER_ID, "INTEGER NOT NULL"),
                mkCOL(TILESETS_TILES, "BLOB"),
                mkFK(TILESETS_GAME_ID, TBL_GAME, GAME_ID),
                mkFK(TILESETS_PLAYER_ID, TBL_PLAYER, PLAYER_ID),
                mkPK(TILESETS_GAME_ID + ", " + TILESETS_PLAYER_ID)
                ));


        Log.d(TAG, "onCreate exec -> ");
        Log.d(TAG, sql.toString());

        try {

            db.execSQL(sql.toString());

        } catch (RuntimeException e) {
            Log.e(TAG, "Fehler beim Anlegen der Datenbank: ", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();


        sql.append(dpTBL(TBL_LANES));
        sql.append(dpTBL(TBL_TILESETS));
        sql.append(dpTBL(TBL_PLAYERS));
        sql.append(dpTBL(TBL_GAME));
        sql.append(dpTBL(TBL_PLAYER));


        Log.d(TAG, "onUpdate exec -> ");
        Log.d(TAG, sql.toString());

        try {

            db.execSQL(sql.toString());
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
