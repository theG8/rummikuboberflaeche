package de.g8keeper.rummikub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RummikubDataSource {

    private static final String TAG = RummikubDataSource.class.getSimpleName();

    private SQLiteDatabase db;
    private RummikubMemoDBHelper dbHelper;

//    private String[] columns = {
//            RummikubMemoDBHelper.PLAYERS_ID,
//            RummikubMemoDBHelper.COL_PRODUCT,
//            RummikubMemoDBHelper.COL_QUANTITY,
//            RummikubMemoDBHelper.COL_CHECKED
//    };


    public RummikubDataSource(Context context) {

        Log.d(TAG, "DataSource erzeugt jetzt den dbHelper");
        dbHelper = new RummikubMemoDBHelper(context);

    }


    public void open() {

        Log.d(TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        db = dbHelper.getWritableDatabase();
        Log.d(TAG, "open: Referenz erhalten. Pfad zur DB -> " + db.getPath());

    }


    public void close() {

        dbHelper.close();
        Log.d(TAG, "Datenbank mit DBHelper geschlossen");

    }


//    public ShoppingMemo createShoppingMemo(String product, int quantity) {
//
//        ContentValues values = new ContentValues();
//        values.put(RummikubMemoDBHelper.COL_PRODUCT, product);
//        values.put(RummikubMemoDBHelper.COL_QUANTITY, quantity);
//
//        long insertId = db.insert(RummikubMemoDBHelper.TBL_PLAYERS, null, values);
//
//        Cursor cursor = db.query(RummikubMemoDBHelper.TBL_PLAYERS, columns,
//                RummikubMemoDBHelper.PLAYERS_ID + "=" + insertId,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//
//        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
//
//        cursor.close();
//
//        return shoppingMemo;
//
//    }
//
//    public ShoppingMemo updateShoppingMemo(long id, String newProduct, int newQuantity, boolean newChecked) {
//
//        int iNewChecked = newChecked ? 1 : 0;
//        ContentValues values = new ContentValues();
//
//        values.put(RummikubMemoDBHelper.COL_PRODUCT, newProduct);
//        values.put(RummikubMemoDBHelper.COL_QUANTITY, newQuantity);
//        values.put(RummikubMemoDBHelper.COL_CHECKED, iNewChecked);
//
//        db.update(RummikubMemoDBHelper.TBL_PLAYERS, values,
//                RummikubMemoDBHelper.PLAYERS_ID + "=" + id, null);
//
//
//        Cursor cursor = db.query(RummikubMemoDBHelper.TBL_PLAYERS, columns,
//                RummikubMemoDBHelper.PLAYERS_ID + "=" + id,
//                null, null, null, null);
//
//        cursor.moveToFirst();
//        ShoppingMemo sm = cursorToShoppingMemo(cursor);
//        cursor.close();
//
//        return sm;
//    }
//
//    public void deleteShoppingMemo(ShoppingMemo shoppingMemo) {
//
//        long id = shoppingMemo.getId();
//
//        db.delete(RummikubMemoDBHelper.TBL_PLAYERS, RummikubMemoDBHelper.PLAYERS_ID + "=" + id, null);
//
//        Log.d(TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + shoppingMemo.toString());
//
//    }
//
//
//    private ShoppingMemo cursorToShoppingMemo(Cursor cursor) {
//        int idIndex = cursor.getColumnIndex(RummikubMemoDBHelper.PLAYERS_ID);
//        int idProduct = cursor.getColumnIndex(RummikubMemoDBHelper.COL_PRODUCT);
//        int idQuantity = cursor.getColumnIndex(RummikubMemoDBHelper.COL_QUANTITY);
//        int idChecked = cursor.getColumnIndex(RummikubMemoDBHelper.COL_CHECKED);
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
//        Cursor cursor = db.query(RummikubMemoDBHelper.TBL_PLAYERS, columns,
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
