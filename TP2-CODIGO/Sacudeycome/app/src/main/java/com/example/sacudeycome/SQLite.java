package com.example.sacudeycome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.sacudeycome.ShakeDetector;

public class SQLite  {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SQLite() {}

    /* Inner class that defines the table contents */
    public static class SQLentry implements BaseColumns {
        public static final String TABLE_NAME = "Metrica";
        public static final String COLUMN_NAME_TITLE = "Nombre de la metrica";
        public static final String COLUMN_NAME_TITLE2 = "Valor";
        public static final String COLUMN_NAME_TITLE3 = "Franja Horaria";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
        // If you change the database schema, you must increment the database version.
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SQLentry.TABLE_NAME + " (" +
                        SQLentry._ID + " INTEGER PRIMARY KEY," +
                        SQLentry.COLUMN_NAME_TITLE + " TEXT," +
                        SQLentry.COLUMN_NAME_TITLE2 + " TEXT," +
                        SQLentry.COLUMN_NAME_TITLE3 + " TEXT," +
                        SQLentry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SQLentry.TABLE_NAME;


    public static class SQLHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "SacudeyCome.db";

        public SQLHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }






}

