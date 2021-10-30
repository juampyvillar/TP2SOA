package com.example.sacudeycome;

import android.app.Application;

import java.util.Timer;

public class MiAplicacion extends Application {
    private String token;
    private String token_refresh;
    Timer tiempo;

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

    public Timer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Timer tiempo) {
        this.tiempo = tiempo;
    }

    public void actualizarToken_refresh(){
        //solicitud al servidor y actualizar token
    }
}
