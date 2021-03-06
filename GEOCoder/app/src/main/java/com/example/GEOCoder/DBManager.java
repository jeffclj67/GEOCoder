package com.example.GEOCoder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    //constructor
    public DBManager(Context c){
        context = c;
    }

    public DBManager open() throws SQLException{
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }


    public void insert(String name, String desc, String addr){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LAT, name);
        contentValues.put(DatabaseHelper.LONG, desc);
        contentValues.put(DatabaseHelper.ADD, addr);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public Cursor fetch(){
        String[] columns = new String[] {DatabaseHelper._ID,
                DatabaseHelper.LAT, DatabaseHelper.LONG, DatabaseHelper.ADD};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    // searching function. search from the address column and return the entire entry
    public Cursor searchAddr(String addr){
        String[] columns = new String[] {DatabaseHelper._ID,
                DatabaseHelper.LAT, DatabaseHelper.LONG, DatabaseHelper.ADD};

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                columns,
                DatabaseHelper.ADD +" LIKE '%" + addr + "%'",
                null,
                null,
                null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc, String addr){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.LAT, name);
        contentValues.put(DatabaseHelper.LONG, desc);
        contentValues.put(DatabaseHelper.ADD, addr);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID +
                " = " + _id, null);
        return i;
    }

    public void delete(long _id){
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + " = " + _id, null);
    }



}