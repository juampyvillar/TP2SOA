package com.example.sacudeycome.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "metrica2.db";
    private static final String TABLA = "Metrica2";
    private static final int DB_VERSION = 1;
    public MyOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLA + "(" +
                "nombre TEXT PRIMARY KEY ," +
                "valor INTEGER," +
                "franja TEXT ) " );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLA);
        onCreate(db);
    }

    public void insertar(String metrica, int valor, String rango){
        SQLiteDatabase bd=getWritableDatabase();
        String values="(" +  "'" + metrica  + "'" + "," + valor + "," + "'" + rango + "'" + ")";
        if(bd != null){
            Log.d("DEBUG","Esto entra a insertar y se rompe amigo");
            bd.execSQL(" INSERT OR REPLACE INTO " + TABLA + " VALUES " + values );
            bd.close();
        }
    }

    public void actualizar (String metrica, int valor){
        SQLiteDatabase bd=getWritableDatabase();
        String update = " UPDATE " + TABLA + " SET valor = " + valor + " WHERE nombre = " + "'" + metrica + "'"+ ";";
        if(bd != null){
            Log.d("DEBUG","Esto entra a insertar y se rompe amigo");
            bd.execSQL(update);
            bd.close();
        }
    }
}