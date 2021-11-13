package com.example.GEOCoder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Table name:
    public static final String TABLE_NAME = "COUNTRIES";

    //Table columns:
    public static final String _ID = "_id";
    public static final String LAT = "subject";
    public static final String LONG = "description";
    public static final String ADD = "address";

    //Database information
    static final String DB_NAME = "MASTER_ANDROID_APP.DB";

    //Database version:
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAT +
            " TEXT NOT NULL, " + LONG + " TEXT, " + ADD + " TEXT);";


    //Constructor:
    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Executing the query
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
