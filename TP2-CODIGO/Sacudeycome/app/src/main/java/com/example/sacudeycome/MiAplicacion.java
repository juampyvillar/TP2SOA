package com.example.sacudeycome;

import android.app.Application;

import java.util.Timer;

public class MiAplicacion extends Application {
    private String token;
    private String token_refresh;
    protected static long topeMinutos=12;
    private long tiempoTranscurridoRefresh;
    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/refresh";

    public static long getTopeMinutos() {
        return topeMinutos;
    }

    public static void setTopeMinutos(long topeMinutos) {
        MiAplicacion.topeMinutos = topeMinutos;
    }

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
    }

    public long tiempoTranscurridoRefresh() {
        return tiempoTranscurridoRefresh;
    }

    public void setTiempoTranscurridoRefresh(long tiempoTranscurridoRefresh) {
        this.tiempoTranscurridoRefresh = tiempoTranscurridoRefresh;
    }

    public void actualizarTiempoTranscurrido(){
        if(tiempoTranscurridoRefresh >= topeMinutos){
            actualizarToken_refresh();
            tiempoTranscurridoRefresh=0;
            return;
        }
        if(tiempoTranscurridoRefresh>0) {
            tiempoTranscurridoRefresh+=((System.currentTimeMillis()-tiempoTranscurridoRefresh)/1000/60);
        }


    }
}
