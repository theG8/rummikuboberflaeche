package de.g8keeper.rummikub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.g8keeper.rummikub.Player;

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
            DBHelper.PLAYERS_PLAYER_ID
    };

    private String[] columsLanes = {
            DBHelper.LANES_GAME_ID,
            DBHelper.LANES_POSITION,
            DBHelper.LANES_TILES
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

        long index = cursor.getInt(idIndex);
        String name = cursor.getString(idName);

        Player player = new Player(name);
        player.setId(index);
        return player;

    }



    public Player createPlayer(String name) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.PLAYER_NAME, name);

        long insertId = db.insert(DBHelper.TBL_PLAYER,null, values);

        Cursor cursor = db.query(DBHelper.TBL_PLAYER,
                columnsPlayer,
                DBHelper.PLAYER_ID + "=" + insertId,
                null,null,null,null);

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



//    public ShoppingMemo updateShoppingMemo(long id, String newProduct, int newQuantity, boolean newChecked) {
//
//        int iNewChecked = newChecked ? 1 : 0;
//        ContentValues values = new ContentValues();
//
//        values.put(DBHelper.COL_PRODUCT, newProduct);
//        values.put(DBHelper.COL_QUANTITY, newQuantity);
//        values.put(DBHelper.COL_CHECKED, iNewChecked);
//
//        dbHelper.update(DBHelper.TBL_PLAYERS, values,
//                DBHelper.PLAYERS_ID + "=" + id, null);
//
//
//        Cursor cursor = dbHelper.query(DBHelper.TBL_PLAYERS, columns,
//                DBHelper.PLAYERS_ID + "=" + id,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        ShoppingMemo sm = cursorToShoppingMemo(cursor);
//        cursor.close();
//
//        return sm;
//    }
//
//
//
//
//    private ShoppingMemo cursorToShoppingMemo(Cursor cursor) {
//        int idIndex = cursor.getColumnIndex(DBHelper.PLAYERS_ID);
//        int idProduct = cursor.getColumnIndex(DBHelper.COL_PRODUCT);
//        int idQuantity = cursor.getColumnIndex(DBHelper.COL_QUANTITY);
//        int idChecked = cursor.getColumnIndex(DBHelper.COL_CHECKED);
//
//        long index = cursor.getInt(idIndex);
//        String product = cursor.getString(idProduct);
//        int quantity = cursor.getInt(idQuantity);
//        boolean checked = (cursor.getInt(idChecked) != 0);
//
//        ShoppingMemo shoppingMemo = new ShoppingMemo(product, quantity, index, checked);
//
//        return shoppingMemo;
//
//    }
//
//
//    public List<ShoppingMemo> getAllShoppingMemos() {
//
//        List<ShoppingMemo> shoppingMemoList = new ArrayList<>();
//        Cursor cursor = dbHelper.query(DBHelper.TBL_PLAYERS, columns,
//                null, null, null, null, null);
//
//        cursor.moveToFirst();
//
//        ShoppingMemo shoppingMemo;
//
//        while (!cursor.isAfterLast()) {
//            shoppingMemo = cursorToShoppingMemo(cursor);
//            shoppingMemoList.add(shoppingMemo);
//            Log.d(TAG, "ID: " + shoppingMemo.getId() +
//                    " Inhalt: " + shoppingMemo.toString());
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//
//        return shoppingMemoList;
//
//    }


}
