package com.example.sacudeycome.presentador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Conexiones extends AppCompatActivity {

    public boolean chequearConexionInternet() {
        Log.d("oncreate","antes de chequear conexion0");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("oncreate","antes de chequear conexion1");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("oncreate","antes de chequear conexion2");
        return networkInfo != null && networkInfo.isConnected();
    }
}
