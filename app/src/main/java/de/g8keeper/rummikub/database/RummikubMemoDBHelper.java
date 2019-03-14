package de.g8keeper.rummikub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


public class RummikubMemoDBHelper extends SQLiteOpenHelper {

    private static final String TAG = RummikubMemoDBHelper.class.getSimpleName();

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



    public static final String TBL_GAME_PLAYERS = "tblGamePlayers";
    public static final String GAME_PLAYERS_GAME_ID = "id_game"; //INTEGER
    public static final String GAME_PLAYERS_PLAYER_ID = "id_player";



    public RummikubMemoDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DbHelper hat die Datenbank angelegt: " + getDatabaseName());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();


        // tblPlayer:
        sql.append(
                mkTBL(TBL_PLAYER, new String[]{
                        mkCOL(PLAYER_ID, "INTEGER AUTOINCREMENT"),
                        mkCOL(PLAYER_NAME, "TEXT NOT NULL"),
                        mkPK(PLAYER_ID)
                })
        ).append("\n");


        // tblGame:
        sql.append(
                mkTBL(TBL_GAME, new String[]{
                        mkCOL(GAME_ID, "INTEGER AUTOINCREMENT"),
                        mkCOL(GAME_TITLE, "TEXT NOT NULL"),
                        mkCOL(GAME_START, "DATETIME"),
                        mkCOL(GAME_END, "DATETIME"),
                        mkPK(GAME_ID)
                })
        ).append("\n");


        // tblGamePlayers:
        sql.append(
                mkTBL(TBL_GAME_PLAYERS, new String[]{
                        mkCOL(GAME_PLAYERS_GAME_ID, "INTEGER NOT NULL"),
                        mkCOL(GAME_PLAYERS_PLAYER_ID, "INTEGER NOT NULL"),
                        mkFK(GAME_PLAYERS_GAME_ID, TBL_GAME, GAME_ID),
                        mkFK(GAME_PLAYERS_PLAYER_ID, TBL_PLAYER, PLAYER_ID)
                })
        ).append("\n");


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

        sql.append(dpTBL(TBL_GAME_PLAYERS));
        sql.append(dpTBL(TBL_GAME));
        sql.append(dpTBL(TBL_PLAYER));


        Log.d(TAG, "onUpdate exec -> ");
        Log.d(TAG, sql.toString());

        try {

            db.execSQL(sql.toString());
            onCreate(db);

        } catch (RuntimeException e) {
            Log.e(TAG, "Fehler beim Upgrade der Datenbank: ", e);
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

        for (String s : config) {
            stmt.append(s).append(", ");
        }
        stmt.delete(stmt.lastIndexOf(","),stmt.length()+1);
        stmt.append(");\n");

        return stmt.toString();

    }

    private static String dpTBL(String table) {
        return "DROP TABLE IF EXISTS " + table + ";\n";
    }

}
