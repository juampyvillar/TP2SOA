package com.example.sacudeycome;

import android.app.Application;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class MiAplicacion extends Application {
    private String token;
    private String token_refresh;
    protected static long topeMinutos=10;
    private double tiempoInicio;

    private double tiempoTranscurridoRefresh;
    private static final String URI_ACTUALIZAR = "http://so-unlam.net.ar/api/api/refresh";


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_refresh() {
        return token_refresh;
    }

    public void setToken_refresh(String token_refresh) {
        this.token_refresh = token_refresh;
    }


    public void actualizarToken_refresh(){
        //solicitud al servidor y actualizar token

            Log.d("actualizartoken","Biennnnn2");
            Intent i = new Intent(MiAplicacion.this, ServicesHttp_POST.class);
            i.putExtra("metodo","PUT");
            i.putExtra("uri", URI_ACTUALIZAR);
            startService(i);

    }


    public double getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(double tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

//    public void actualizarTiempoTranscurrido(){
//        if(tiempoTranscurridoRefresh >= topeMinutos){
//            actualizarToken_refresh();
//            tiempoTranscurridoRefresh=0;
//            return;
//        }
//        if(tiempoTranscurridoRefresh>=0) {
//            long endTime = SystemClock.elapsedRealtime();
//            long elapsedMilliSeconds = endTime - tiempoInicio;
//            double elapsedSeconds = elapsedMilliSeconds / 1000.0;
//            double elapsedMinutos = elapsedSeconds / 60.0;
//            tiempoTranscurridoRefresh+=((SystemClock.elapsedRealtime()/60000)-tiempoTranscurridoRefresh);
//            Log.d("tiempo:","MiAplicacion: " + SystemClock.elapsedRealtime());
//            Log.d("tiempo:","MiAplicacion: tiempo transcurrido: " + tiempoTranscurridoRefresh);
//
//        }

        public void actualizarTiempoTranscurrido(){

                double tiempoActual = SystemClock.elapsedRealtime();
                double elapsedMilliSeconds = tiempoActual - tiempoInicio;
                double elapsedSeconds = elapsedMilliSeconds / 1000.0;
                double elapsedMinutos = elapsedSeconds / 60.0;
                if(elapsedMinutos>topeMinutos){
                    actualizarToken_refresh();
                    tiempoInicio=tiempoActual;

            }
            Log.d("tiempo:","MiAplicacion: " + tiempoInicio);
           Log.d("tiempo:","MiAplicacion: tiempo transcurrido: " + elapsedMinutos);

    }
}
