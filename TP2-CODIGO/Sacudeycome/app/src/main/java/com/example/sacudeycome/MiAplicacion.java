package com.example.sacudeycome;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class MiAplicacion extends Application {
    private String token;
    private String token_refresh;
    protected static long topeMinutos= 1;
    private double tiempoInicio;
    private String usuario;

    private static final String URI_ACTUALIZAR = "http://so-unlam.net.ar/api/api/refresh";
    public IntentFilter filtro;
    private ReceptorOperacion receiver = new MiAplicacion.ReceptorOperacion();


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

    public double getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(double tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public void actualizarToken_refresh(){
        //solicitud al servidor y actualizar token
        chequearConexionInternet();
        configurarBroadcastReceiver();

            Log.d("actualizartoken","Biennnnn2");
            Intent i = new Intent(MiAplicacion.this, ServicesHttp_POST.class);
            i.putExtra("metodo","PUT");
            i.putExtra("uri", URI_ACTUALIZAR);
            startService(i);

    }

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

    private void chequearConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Log.d( "ChequeoInternet","Hay Conexion a Internet ");

        }else{
            Log.d( "ChequeoInternet","No hay Conexion a Internet ");
        }

    }
    private void configurarBroadcastReceiver(){
        filtro = new IntentFilter("com.example.intentservice.intent.action.RUN");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver,filtro);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public class ReceptorOperacion extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){
            try{
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.d("RespuestaServer","33333 " + datosJson.toString());
                if(datosJson.get("success").toString().equals("true")){
                    String token =  new String();
                    String token_refresh =new String();
                    token=datosJson.get("token").toString();
                    token_refresh=datosJson.get("token_refresh").toString();
                    setToken(token);
                    setToken_refresh(token_refresh);
                    setTiempoInicio(SystemClock.elapsedRealtime()); //valor actual como inicio
                    unregisterReceiver(receiver);
                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login/Registro incorrecto", Toast.LENGTH_SHORT).show();
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
