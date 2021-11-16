//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    //creating columns
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "geomapping";
    private static final String COL1 = "ID";
    private static final String COL2 = "address";
    private static final String COL3 = "lattitude";
    private static final String COL4 = "longitude";

    //constructor to make it
    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }
    //default oncreate
    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, "  + COL3 + " TEXT, "  + COL4+ " TEXT)";
        db.execSQL(createTable);

    }
    //on upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //fetches result and returns the cursor
    public Cursor fetch(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //adds the data into the table
    public boolean addData(String address, String latitude, String longitude){
        if(address.length() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, address);
            contentValues.put(COL3, latitude);
            contentValues.put(COL4, longitude);
            //Log.d(TAG, "addData: Adding " + address + ", " + latitude + ", " + longitude + " to " + TABLE_NAME);
            long result = db.insert(TABLE_NAME, null, contentValues);
            //if date as inserted incorrectly it will return -1
            if (result == -1) {
                return false;
            } return true;
        }else{
            return false;
        }
    }
    //updates the database with the new entry
    public void update(String id, String address, String latitude, String longitude) {
        if(address.length() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, address);
            contentValues.put(COL3, latitude);
            contentValues.put(COL4, longitude);
            db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        }
    }
    //deletes a row
    public void delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{id});
    }
    //gets number of elements in database currently
    public long getSize(){
        SQLiteDatabase db = this.getReadableDatabase();
        long size = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return size;
    }
    //checks if the address already exists in the database
    public boolean ifExists(String add){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT address FROM " + TABLE_NAME + " WHERE address =?";
        Cursor data = db.rawQuery(query, new String[]{add});
        if (data.getCount()>0)
                return true;
        return false;
    }

}
